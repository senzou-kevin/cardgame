package events;

import aiaction.AIAction;
import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import gamelogic.UnitMove;
import gamelogic.UnitMoveSender;
import resourcesManage.ObjectIOC;
import resourcesManage.ThreadManager;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 *
 * {
 *   messageType = “initalize”
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		ThreadManager.getInstance().CreateThreadPool();
		AIAction.getInstance().setGameState(gameState);
		AIAction.getInstance().setOut(out);
		if(gameState.getMode()==1){
			return;
		}
		//initiate the IOC
		ObjectIOC.getInstance().Initialization();
		// this executes the command demo, comment out this when implementing your solution
		initializeHealthAndMana(out,gameState);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		// new board
		gameState.getBoard().drawBoard(out);
		// new a humanActor in the board
		newAvatar(out,gameState,0);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// new a aiActor in the board
		newAvatar(out,gameState,1);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		//draw the card three times, and add them in player's and ai's hands
		initializeCardsInHands(out,gameState);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		//bind a certain gameState.
		((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).bindGameState(gameState);
		//start MoveThread
		ThreadManager.getInstance().EventProcessorByThread((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender"));
	}

	/**
	 * Initialize ai's and human's health and mana
	 * @param out
	 * @param gameState
	 */
	public void initializeHealthAndMana(ActorRef out,GameState gameState){
		// initialize human's health and mana
		BasicCommands.setPlayer1Health(out,gameState.getHumanPlayer());
		BasicCommands.setPlayer1Mana(out, gameState.getHumanPlayer());

		//initialize aiHuman's health and mana
		BasicCommands.setPlayer2Health(out,gameState.getAiPlayer());
		BasicCommands.setPlayer2Mana(out,gameState.getAiPlayer());
	}

	/**
	 * at the beginning of the game, ai and human player should
	 * have 3 cards in hands.
	 * @param out
	 * @param gameState
	 */
	public void initializeCardsInHands(ActorRef out,GameState gameState){
		for(int i=1;i<=3;i++){
			//human draw three cards at the beginning
			Card card = gameState.drawCard(0);
			gameState.addCardsToHands(card, 0);
			BasicCommands.drawCard(out,card,i,0);
			try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
			//ai also draw three cards at the beginning
			//but don't show on the screen
			Card aiCard = gameState.drawCard(1);
			gameState.addCardsToHands(aiCard,1);
		}
	}

	/**
	 * Generate either HumanAvatar (Unit) or AiAvatar (Unit) on the map,
	 * and then store the Unit to GameState
	 * */
	public void newAvatar(ActorRef out, GameState gameState,int mode){
		Unit avatar = new Unit();
		Tile tile = new Tile();
		// set a HumanUnit in the board
		if(mode == 0){
			// set Tile in the Unit
			avatar = gameState.getHumanAvatar();
			tile = gameState.getBoard().getTileByBoard(1,2);
			tile.setUnit(avatar);
			// store Avatar in the GameState
			gameState.setHumanCurrentUnits(avatar);
			try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		}
		// set a AiUnit in the board
		if(mode == 1){
			avatar = gameState.getAiAvatar();
			tile = gameState.getBoard().getTileByBoard(7,2);
			tile.setUnit(avatar);
			// store Avatar in the GameState
			gameState.setAiCurrentUnits(avatar);
			try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		}
		// set Tile/Attack/Health in Unit
		avatar.setPositionByTile(tile);
		avatar.setTile(tile);
		avatar.setHealth(20);
		avatar.setMaxHealth(20);
		avatar.setAttack(2);
		// draw HumanAvatar
		BasicCommands.drawUnit(out, avatar, tile);
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out,avatar,20);
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out,avatar,2);
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
	}
}


