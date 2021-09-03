package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.Attack;
import gamelogic.AttackContainer;
import gamelogic.UnitMove;
import gamelogic.UnitMoveSender;
import resourcesManage.ObjectIOC;
import structures.GameState;

/**
 * Indicates that a unit instance has stopped moving. 
 * The event reports the unique id of the unit.
 * 
 * { 
 *   messageType = “unitStopped”
 *   id = <unit id>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class UnitStopped implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).findByID(message.get("id").asInt())&&
		gameState.FindUnitByID(message.get("id").asInt()).isCanAttack())
		{
			((Attack)ObjectIOC.getInstance().getBean("Attack")).startAttack(out,gameState,
					((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).getAttacker(message.get("id").asInt()),
					((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).getTargetTile(message.get("id").asInt()));
		}
		((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).MoveEnd();

	}
}
