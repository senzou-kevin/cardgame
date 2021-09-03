package gamelogic;

import akka.actor.ActorRef;
import annotation.IOCService;
import commands.BasicCommands;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import java.util.ArrayList;
/**
 * This class handles the Abilities of Unit
 * */
@IOCService(name = "Abilities")
public class Abilities {

    public void spellAbilities(ActorRef out, GameState gameState,int mode){
        // add ATK and Health
        spellAddAttribute(out,gameState,14,mode);
        spellAddAttribute(out,gameState,15,mode);
    }

    public void summonAbilities(ActorRef out, GameState gameState,Unit unit){
        //Add health
        summonAddHealth(out, gameState, unit, 4);
        summonAddHealth(out, gameState, unit, 5);
        //draw card
        summonDrawCard(out,gameState,unit,18);
        summonDrawCard(out,gameState,unit,19);
    }

    public void deathAbilities(ActorRef out, GameState gameState,Unit unit){
        // draw a card
        deathDrawCard(out,gameState,unit,30);
        deathDrawCard(out,gameState,unit,31);
    }
    public void dealtAbilities(ActorRef out, GameState gameState,Unit unit){
        // add attack
        dealtAddAttack(out,gameState,unit,16);
        dealtAddAttack(out,gameState,unit,17);
    }

    /**
     * Type: Exist
     * card:Silverguard Knight(unit id:16,17),Ironcliff Guardian(unit id:12,13),Rock Pulveriser(AI,unit id:26,27)
     *  negative
     *
     *Abilities:  If an enemy
     * unit can attack and is
     * adjacent to any unit
     * with provoke, then it
     * can only choose to
     * attack the provoke
     * units. Enemy units
     * cannot move when
     * provoked.
     * */

    public  boolean existProvoke(ActorRef out, GameState gameState,Unit unit,int mode) {

        Tile tile = new Tile();
        //store all the enemy units near the selected unit in the ArrayList
        ArrayList<Unit> unitsAdjacent = new ArrayList<>();
        ArrayList<Tile> totalTiles = new ArrayList<>();
        int tilex = unit.getTile().getTilex();
        int tiley = unit.getTile().getTiley();
        int t = 0;


        for (int i = tilex - 1; i <= tilex + 1; i++) {
            if (i < 0 || i > 8) {
                continue;
            }

            for (int j = tiley - 1; j <= tiley + 1; j++) {
                if (j < 0 || j > 4) {
                    continue;
                }
                tile = gameState.getBoard().getTileByBoard(i, j);

                if (mode == 0) {
                    if ((tile.getUnit() != null) && ((tile.getUnit().getId() ==26) || (tile.getUnit().getId() == 27))) {

                        unit.setMoveScopeMode(2);
                        t = 1;

                    }

                } else {
                    if ((tile.getUnit() != null) && ((tile.getUnit().getId() == 17) || (tile.getUnit().getId() == 16)|| (tile.getUnit().getId() == 12)|| (tile.getUnit().getId() == 13))) {

                        unit.setMoveScopeMode(2);
                        t = 1;
                    }

                }
            }
        }


        if (t == 0 && unit.getMoveScopeMode() == 2) {
            unit.setMoveScopeMode(unit.getPerMoveScopeMode());
        }


        if (unit.getMoveScopeMode() == 2) {
            return true;
        } else {
            return false;
        }

    }

    
    /**
     * Type: Spell
     * card:Pureblade Enforcer
     *
     *Abilities: If the enemy player casts a spell,
     * this minion gains +1 attack and +1 health
     * */
    public void spellAddAttribute(ActorRef out, GameState gameState,int id,int mode){
        if(mode == 0){
            return;
        }

        // if units already exist
        for(Unit unit : gameState.getHumanCurrentUnits()){
            if(unit.getId() == id){
                // play effect
                EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
                BasicCommands.playEffectAnimation(out, ef, unit.getTile());
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

                // Get new Attack and Health
                int newAttack = unit.getAttack() + 1;
                int newHealth = unit.getHealth() + 1;
                // Set new Attack and Health
                unit.setAttack(newAttack);
                unit.setHealth(newHealth);
                unit.setMaxHealth(unit.getMaxHealth() + 1);
                BasicCommands.setUnitAttack(out,unit,newAttack);
                try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
                BasicCommands.setUnitHealth(out,unit,newHealth);
                try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }

    /**
     * Type: Summon
     * card:Azure Herald
     * Abilities: When this unit is summoned give your avatar +3 health (maximum 20)
     **/
    public void summonAddHealth(ActorRef out, GameState gameState,Unit unit,int id){
        if(unit.getId() == id){
            Unit human = gameState.getHumanAvatar();
            // play Effect
            EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
            BasicCommands.playEffectAnimation(out, ef, gameState.getHumanAvatar().getTile());
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            // add 3 health
            int newHealth = human.getHealth() + 3;
            // if newHealth over MaxHealth
            if(newHealth >= 20){
                newHealth = 20;
            }
            //set new health
            human.setHealth(newHealth);
            BasicCommands.setUnitHealth(out,human,newHealth);
            BasicCommands.setPlayer1Health(out, gameState.getHumanPlayer());
            try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    /**
     * Type:Summon
     * card:Blaze Hound
     *
     * Abilities:When this unit is summoned, both players draw a card
     * */
    public void summonDrawCard(ActorRef out,GameState gameState,Unit unit,int id){
        if(unit.getId() == id){
            //Human draws card
            ((EndTurn) ObjectIOC.getInstance().getBean("EndTurn")).drawCard(out,gameState,0);
            //Ai draws card
            ((EndTurn) ObjectIOC.getInstance().getBean("EndTurn")).drawCard(out,gameState,1);
        }
    }

    /**
     * Type: Dealt
     * card:Silverguard Knight
     *
     * Abilities:If human avatar is dealt damage this unit gains +2 attack
     **/
    public void dealtAddAttack(ActorRef out, GameState gameState,Unit defender,int id){
        if(defender == gameState.getHumanAvatar()){
            for(Unit u: gameState.getHumanCurrentUnits()){
                if(u.getId() == id){
                    int newAttack = u.getAttack() + 2;
                    u.setAttack(newAttack);
                    BasicCommands.setUnitAttack(out,u,newAttack);
                }
            }
        }
    }

    /**
     * Type: Death
     * card:Windshrike
     *
     * Abilities: When this unit dies, ai draws a card
     * */
    public void deathDrawCard(ActorRef out,GameState gameState,Unit unit,int id){
        if(unit.getId() == id){
            //Ai draws card
            ((EndTurn) ObjectIOC.getInstance().getBean("EndTurn")).drawCard(out,gameState,1);
        }
    }
}
