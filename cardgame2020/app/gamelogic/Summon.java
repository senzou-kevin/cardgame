package gamelogic;

import akka.actor.ActorRef;
import annotation.IOCService;
import commands.BasicCommands;
import commands.HighlightScope;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Random;

/**
 * This object includes all the methods related to summon units.
 * Both Ai and human share those methods, but execute different codes
 * according to mode. mode=0 means human, mode=1 means AI.
 */
@IOCService(name ="Summon")
public class Summon {
    private HighlightScope highlightScope = new HighlightScope();


    public void playCard(ActorRef out, GameState gameState,int handPosition,int mode){
        Card card = new Card();
        Unit playerUnit = new Unit();

        ArrayList<Unit> unitsOnBoard=new ArrayList<>();
        int mana = 0;

        //mode 0: Humanplayer get card based on position
        if(mode == 0){
            card = gameState.getP1CardsInHands().get(handPosition-1);
            mana = gameState.getHumanPlayer().getMana();
            //playerUnit = gameState.getHumanAvatar();
            unitsOnBoard=gameState.getHumanCurrentUnits();
        }
        //mode 1: Aiplayer get card based on position
        if(mode == 1){
            card = gameState.getAiCardsInHands().get(handPosition);
            mana = gameState.getAiPlayer().getMana();

            unitsOnBoard=gameState.getAiCurrentUnits();
        }
        //Set handPosition in the card
        card.setHandPosition(handPosition);
        //If player have enough mana, then player can use card
        if(mana >= card.getManacost()){
            // play unitCard
            if(card.getUnit() != null){
                highlightScope.highlightUnitCard(out,gameState,card,handPosition,unitsOnBoard);
            }
            if(card.getSpell() != null){
                highlightScope.highlightSpell(out,gameState,card,handPosition,card.getSpell().getKind());
            }
        }
    }

    public ArrayList<Card> checkAvailableCard(GameState gameState){
        int mana = gameState.getAiPlayer().getMana();
        ArrayList<Card> aiCardsInHands = gameState.getAiCardsInHands();
        ArrayList<Card> aiCards = new ArrayList<>();

        for(Card card:aiCardsInHands){
            if(mana>=card.getManacost()){
                aiCards.add(card);
                mana -= card.getManacost();
            }
        }
        return aiCards;
    }


    public int position(GameState gameState){
        Random r=new Random();
        int position = r.nextInt(gameState.getCurrentTiles().size());
        return position;
    }

    public boolean summonOrSpell(ActorRef out, GameState gameState, Card currentCard, Tile tile, int mode){
        // If a card is currently selected
        if(currentCard == null){
            return false;
        }
        if(gameState.getCurrentTiles().contains(tile)){
            // player select a unitCard
            if(currentCard.getUnit()!= null){
                //player summon unit
                summonUnit(out,gameState,tile,currentCard,mode);
                //Abilities
                ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).summonAbilities(out,gameState,currentCard.getUnit());
                try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
            }
            // player select a spellCard
            else if(currentCard.getSpell()!= null){
                //player play spell
                currentCard.getSpell().playSpell(out,gameState,tile);
                //Abilities
                ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).spellAbilities(out,gameState,mode);
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
        //Cancel all highlighted things
        BasicCommands.unHighlighting(out, gameState);
        return true;
    }

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

        //Set Unit int the Tile
        clickedTile.setUnit(unit);

        //only human delete cards in hands
        if(gameState.getMode()==0) {
            // Delete card
            BasicCommands.deleteCard(out, currentCard.getHandPosition());
            try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        // Delete currentCard
        gameState.setCurrentCard(null);
        // Cancel highlighted tile
        BasicCommands.drawScope(out,gameState,gameState.getCurrentTiles(),0);

        return unit;
    }
}
