package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.UnitMove;
import structures.GameState;
import structures.basic.Card;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.getMode()==1){
			System.out.println("otherclicked return");
			return;
		}
		gameState.setCurrentUnit(null);
		//Cancel all highlighted things
		BasicCommands.unHighlighting(out, gameState);
		// delete the card that is currently selected
		gameState.setCurrentCard(null);
		
	}

}


