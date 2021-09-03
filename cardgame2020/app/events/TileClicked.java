package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Abilities;
import gamelogic.Summon;
import gamelogic.UnitMove;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.getMode()==1){
			return;
		}
		//Cancel all highlighted things
		BasicCommands.unHighlighting(out, gameState);

		try{Thread.sleep(50);}catch (Exception e){}
		//get tile
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		Tile clickedTile = gameState.getBoard().getTileByBoard(tilex,tiley);
		// get currentCard

		// play card
		if(gameState.getCurrentCard() != null){
			// get currentCard
			Card currentCard = gameState.getCurrentCard();
			//If any Card is used, handCards will be reset
			if(((Summon)ObjectIOC.getInstance().getBean("Summon")).summonOrSpell(out,gameState,currentCard,clickedTile, gameState.getMode())
				// Summon.getInstance().summonOrSpell(out,gameState,currentCard,clickedTile, gameState.getMode())
			){
				// Reset cards
				BasicCommands.resetCardsInHands(out,gameState,0);
			}
			// delete currentCard
			gameState.setCurrentCard(null);
		// unit move
		}else if(gameState.getCurrentUnit()!=null){
			if(gameState.getHumanCurrentUnits().contains(clickedTile.getUnit()))
			{
				((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 0);
				((UnitMove)ObjectIOC.getInstance().getBean("UnitMove")).GetMoveScope(out,clickedTile,0);
				//UnitMove.getInstance().GetMoveScope(out,clickedTile,0);
			}
			else if(clickedTile.getUnit()==null)
			{
				((UnitMove)ObjectIOC.getInstance().getBean("UnitMove")).UnitMoveAndAttackCom(out,gameState.getCurrentUnit().getTile(),clickedTile);
				//UnitMove.getInstance().UnitMoveAndAttackCom(out,gameState.getCurrentUnit().getTile(),clickedTile);
			}
			else if(gameState.getAiCurrentUnits().contains(clickedTile.getUnit()))
			{
				((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 0);
				((UnitMove)ObjectIOC.getInstance().getBean("UnitMove")).UnitMoveAndAttackCom(out,gameState.getCurrentUnit().getTile(),clickedTile);
				//UnitMove.getInstance().UnitMoveAndAttackCom(out,gameState.getCurrentUnit().getTile(),clickedTile);
			}
		}
		else if(gameState.getCurrentUnit()==null)
		{
			if(clickedTile.getUnit()==null){
				System.out.println("你点击的格子上没有unit");
			}
			else{
				System.out.println("你点击的格子上有unit");
				((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, clickedTile.getUnit(), 0);
			}

			((UnitMove)ObjectIOC.getInstance().getBean("UnitMove")).GetMoveScope(out,clickedTile,0);
			//UnitMove.getInstance().GetMoveScope(out,clickedTile,0);
		}
	}

	/**
	 *Player uses Spell Card or summons Unit,
	 * deletes handCard and costs mana.
	 * */
	public boolean summonOrSpell(ActorRef out,GameState gameState,Card currentCard,Tile tile,int mode){
		// If a card is currently selected
		if(currentCard == null){
			return false;
		}
		if(gameState.getCurrentTiles().contains(tile)){
			// player select a unitCard
			if(currentCard.getUnit()!= null){
				//player summon unit
				summonUnit(out,gameState,tile,currentCard,mode);
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			}
			// player select a spellCard
			else if(currentCard.getSpell()!= null){
				//player play spell
				currentCard.getSpell().playSpell(out,gameState,tile);
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}else {
			return false;
		}
		//cost mana
		if(mode == 0){ costMana(out,gameState,currentCard,mode); }
		if(mode == 1){ costMana(out,gameState,currentCard,mode); }
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		// delete currentCard
		gameState.setCurrentCard(null);
		// clear currentTiles
		gameState.getCurrentTiles().clear();
		return true;
	}

	/**
	 *Cost Mana and set new Mana
	 * */
	public void costMana(ActorRef out,GameState gameState,Card currentCard,int mode){
		// mode 0: delete currentCard in HumanCardsInHands and cost mana
		if(mode == 0){
			gameState.getP1CardsInHands().remove(currentCard);
			Player Humanplayer = gameState.getHumanPlayer();
			// Cost mana
			int newMana = Humanplayer.getMana() - currentCard.getManacost();
			Humanplayer.setMana(newMana);
			BasicCommands.setPlayer1Mana(out, Humanplayer);
		}
		// mode 1: delete currentCard in AiCardsInHands and cost mana
		if(mode == 1){
			gameState.getAiCardsInHands().remove(currentCard);
			Player Ai = gameState.getAiPlayer();
			// Cost mana
			int newMana = Ai.getMana() - currentCard.getManacost();
			Ai.setMana(newMana);
			BasicCommands.setPlayer2Mana(out, Ai);
		}
	}

	/**
	 *Draw the Unit on the clickedTile
	 * and store the Unit in the GameState.
	 * */
	public Unit summonUnit(ActorRef out,GameState gameState,Tile clickedTile,Card currentCard,int mode) {
		//Get unit
		Unit unit = currentCard.getUnit();

		// Store unit in the GameState
		if(mode == 0){ gameState.setHumanCurrentUnits(unit);}
		if(mode == 1){ gameState.setAiCurrentUnits(unit); }
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		// set Tile/Position in the Unit
		unit.setPosition(new Position(clickedTile.getXpos(),clickedTile.getYpos(),clickedTile.getTilex(),clickedTile.getTiley()));
		unit.setTile(clickedTile);
		unit.setPositionByTile(clickedTile);
        clickedTile.setUnit(unit);
		//play Summon Animation
		EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(out, ef, unit.getTile());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		// Draw the unit on the clickedTile
		BasicCommands.drawUnit(out, unit, clickedTile);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		//Set attack/health
		BasicCommands.setUnitHealth(out,unit,currentCard.getBigCard().getHealth());
		unit.setHealth(currentCard.getBigCard().getHealth());
		unit.setMaxHealth(currentCard.getBigCard().getHealth());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out,unit,currentCard.getBigCard().getAttack());
		unit.setAttack(currentCard.getBigCard().getAttack());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		// Delete card
		BasicCommands.deleteCard(out, currentCard.getHandPosition());
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

		// Delete currentCard
		gameState.setCurrentCard(null);
		// Cancel highlighted tile
		BasicCommands.drawScope(out,gameState,gameState.getCurrentTiles(),0);
		return unit;
	}
}
