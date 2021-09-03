package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.UnitMove;
import resourcesManage.ObjectIOC;
import structures.GameState;

/**
 * Indicates that a unit instance has started a move. 
 * The event reports the unique id of the unit.
 * 
 * { 
 *   messageType = “unitMoving”
 *   id = <unit id>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class UnitMoving implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).setMoving(true);
		int unitid = message.get("id").asInt();
	}
}
