package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.HighlightScope;
import gamelogic.Abilities;
import gamelogic.Attack;
import resourcesManage.ObjectIOC;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

/**
 * This class is related to spell
 */
public class Spell {
    //Types of spell
    int kind;

    public Spell() {
    }

    public Spell(int kind) {
        this.kind = kind;
    }

    // play Spell
    public void playSpell(ActorRef out, GameState gameState,Tile tile){
        if(kind == 0){
            damageSpell(out,gameState,tile);
        }
        if(kind == 1){
            addHealth(out,gameState,tile);
        }
        if(kind == 2){
            addAttack(out,gameState,tile);
        }
        if(kind == 3){
            killUnit(out,gameState,tile);
        }
        BasicCommands.unHighlighting(out, gameState);
    }

    /**
     * kind 0
     * Spell: Truestrike
     * Scope: Ai's Units
     * Abilities: Deal 2 damage to an enemy unit
     */
    public void damageSpell(ActorRef out, GameState gameState,Tile tile){
        Unit unit = new Unit();
        HighlightScope highlightScope = new HighlightScope();
        // get unit which be clicked
        for (Unit u:highlightScope.getSpellScope(out,gameState,kind)){
            if(u.getTile() == tile){
                unit = u;
                try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }
        // play Effect
        EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation);
        BasicCommands.playEffectAnimation(out, ef, tile);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        // cost 2 health
        int newHealth = unit.getHealth() - 2;
        // if the unit health is 0, the unit dies
        if(newHealth <= 0){
            ((Attack) ObjectIOC.getInstance().getBean("Attack")).death(out,gameState,unit);
           // Attack.getInstance().death(out,gameState,unit);
            return;
        }
        // set unit health
        unit.setHealth(newHealth);
        BasicCommands.setUnitHealth(out,unit,newHealth);
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
        //Abilities
        ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).dealtAbilities(out,gameState,unit);
        //Abilities.getAbilities().dealtAbilities(out,gameState,unit);
    }

    /**
     * kind 1
     * Spell: Sundrop Elixir
     * Scope: All Units
     * Abilities: Add +5 health to a Unit.
     * This cannot take a unit over its starting health value.
     */
    public void addHealth(ActorRef out, GameState gameState,Tile tile){
        Unit unit = new Unit();
        HighlightScope highlightScope = new HighlightScope();
        for (Unit u:highlightScope.getSpellScope(out,gameState,kind)){
            if(u.getTile() == tile){
                unit = u;
            }
        }
        // play Effect
        EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
        BasicCommands.playEffectAnimation(out, ef, tile);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        // add 5 health
        int newHealth = unit.getHealth() + 5;
        // if newHealth over MaxHealth
        if(newHealth >= unit.getMaxHealth()){
            newHealth = unit.getMaxHealth();
        }
        //set new health
        unit.setHealth(newHealth);
        BasicCommands.setUnitHealth(out,unit,newHealth);
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    /**
     * kind 2
     * Spell: Staff of Y’Kir
     * Scope: Ai Avatar
     * Abilities:Add +2 attack to your avatar
     */
    public void addAttack(ActorRef out, GameState gameState,Tile tile){
        //get ai's avatar
        Unit unit =gameState.getAiAvatar();
        // play Effect
        EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
        BasicCommands.playEffectAnimation(out, ef, tile);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        // add 2 attack
        int newAttack = unit.getAttack() + 2;

        //set new attack
        unit.setAttack(newAttack);
        BasicCommands.setUnitAttack(out,unit,newAttack);
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    /**
     * kind 3
     * Spell: Entropic Decay
     * Scope: All Units
     * Abilities: Reduce a non-avatar unit to 0 health
     */
    public void killUnit(ActorRef out,GameState gameState,Tile tile){
        Unit unit = new Unit();

        for (Unit u:gameState.getHumanCurrentUnits()){
            if(u.getTile() == tile){
                unit = u;
                // can not kill human avatar
                if(u == gameState.getHumanAvatar()){
                    return;
                }
            }
        }
        // play Effect
        EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation);
        BasicCommands.playEffectAnimation(out, ef, tile);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        // kill unit
        ((Attack)ObjectIOC.getInstance().getBean("Attack")).death(out,gameState,unit);
        // Attack.getInstance().death(out,gameState,unit);
    }





    public int getKind() {
        return kind;
    }

}
