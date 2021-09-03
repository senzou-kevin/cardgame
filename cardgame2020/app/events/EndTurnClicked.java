package events;

import aiaction.AIAction;
import annotation.IOCResource;
import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Attack;
import gamelogic.EndTurn;
import gamelogic.Summon;
import gamelogic.UnitMoveSender;
import org.ietf.jgss.GSSManager;
import resourcesManage.ObjectIOC;
import resourcesManage.ThreadManager;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Random;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		if(gameState.getMode()==1){
			System.out.println("otherclicked return");
			return;
		}
		//Cancel all highlighted things
		BasicCommands.unHighlighting(out, gameState);

		//human draws a card
		((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).drawCard(out,gameState,gameState.getMode());

		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

		((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).InitTurn(gameState, gameState.getMode());

		//switch mode to ai player
		gameState.switchMode();
		//if it is first turn, ai will not gain mana
		if(gameState.getTurns()>=2){
			((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).setMana(out,gameState, gameState.getMode());
		}
		//Ai's turn
		ThreadManager.getInstance().EventProcessorByThread(AIAction.getInstance());
	}
}




