package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.HighlightScope;
import gamelogic.Summon;
import gamelogic.UnitMove;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.getMode()==1){
			System.out.println("cardclicked return");
			return;
		}
		int handPosition = message.get("position").asInt();

		//Cancel all highlighted things
		BasicCommands.unHighlighting(out, gameState);
		//clear buffer
		((UnitMove)ObjectIOC.getInstance().getBean("UnitMove")).Initialize();
		try { Thread.sleep(100); }catch (Exception e) { }
		//Human play card
		((Summon)ObjectIOC.getInstance().getBean("Summon")).playCard(out,gameState,handPosition, gameState.getMode());
		try { Thread.sleep(100); }catch (Exception e) { }
	}

}
