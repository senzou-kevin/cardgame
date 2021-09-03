package gamelogic;


import akka.actor.ActorRef;
import annotation.IOCResource;
import annotation.IOCService;
import commands.BasicCommands;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * This class handles attacks on Unit
 * */

@IOCService(name = "Attack")
public  class Attack {

    @IOCResource
    AttackContainer attackContainer;
    private Unit attacker;
    private Unit defender;

    public synchronized void startAttack(ActorRef out, GameState gameState,Unit attacker,Tile endtile){
        System.out.println(attacker.getAttack());
        //get attacker
        this.attacker = attacker;
        //get defender
        defender = endtile.getUnit();
        int attackerAttack = attacker.getAttack();
        int attackerHealth = attacker.getHealth();
        int defenderAttack = defender.getAttack();
        int defenderHealth = defender.getHealth();
        int remainingHealth = defenderHealth - attackerAttack;
        int remainingHealth2 = attackerHealth - defenderAttack;

        //play Animation
        if(attacker.getAttackScopeMode() == 0){
            BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.attack);

        }
        // playProjectileAnimation
        if(attacker.getAttackScopeMode() == 1){
            EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
            BasicCommands.playUnitAnimation(out,attacker, UnitAnimationType.attack);

            BasicCommands.playProjectileAnimation(out, projectile, 0, attacker.getTile(), defender.getTile());
        }
        System.out.println(attacker.getAttack()+"2");

        if(remainingHealth > 0 ) {
                renewHealth(out,gameState,defender,remainingHealth);
                BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.attack);
                //Abilities
                ((Abilities) ObjectIOC.getInstance().getBean("Abilities")).dealtAbilities(out,gameState,defender);

                // If the defender has more than 0 health, the defending unit then counter-attacks
            if(Math.abs(attacker.getTile().getTilex() - defender.getTile().getTilex()) <=1 &&
                    Math.abs(attacker.getTile().getTiley() - defender.getTile().getTiley()) <=1){
                if(remainingHealth2 > 0){

                        renewHealth(out,gameState,attacker,remainingHealth2);
                        //Abilities
                        ((Abilities) ObjectIOC.getInstance().getBean("Abilities")).dealtAbilities(out,gameState,attacker);
                    }else{
                        // attacker dies
                        renewHealth(out,gameState,attacker,remainingHealth2);
                        death(out,gameState,attacker);
                    }
                }
        }else{

            // defender dies
            BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.attack);
            death(out,gameState,defender);
        }

        // reduce attack times
        attacker.reduceAttackTimes();
        attacker.setIsattack(true);
        if(attackContainer.findByID(attacker.getId()))
        {
            attackContainer.removeElementByAttacker(attacker);
        }

    }


    public void renewHealth(ActorRef out, GameState gameState,Unit unit,int newHealth){
        // set human health
        if(unit == gameState.getHumanAvatar()){
            gameState.getHumanAvatar().setHealth(newHealth);
            BasicCommands.setUnitHealth(out,unit,newHealth);
            gameState.getHumanPlayer().setHealth(newHealth);
            BasicCommands.setPlayer1Health(out, gameState.getHumanPlayer());
            try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace();}
            // set ai health
        }else if(unit == gameState.getAiAvatar()){
                gameState.getAiAvatar().setHealth(newHealth);
                BasicCommands.setUnitHealth(out,unit,newHealth);
                gameState.getAiPlayer().setHealth(newHealth);
                BasicCommands.setPlayer2Health(out, gameState.getAiPlayer());
                try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace();}
            // set unit health
        }else {
            unit.setHealth(newHealth);
            BasicCommands.setUnitHealth(out,unit,newHealth);
        }
    }

    public void death(ActorRef out, GameState gameState,Unit unit){
        //Abilities
        ((Abilities) ObjectIOC.getInstance().getBean("Abilities")).deathAbilities(out,gameState,unit);
        // delete unit
        Tile tile = unit.getTile();
        tile.setUnit(null);
        //If the AI dies, the humans win the game
        if(unit == gameState.getHumanAvatar()){
            BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}
            BasicCommands.addPlayer1Notification(out, "AI won this game", 3);
            System.exit(0);
            //If the Human dies, the Ai win the game
        } else if(unit == gameState.getAiAvatar()){
            BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}
            BasicCommands.addPlayer1Notification(out, "Human won this game", 3);
            System.exit(0);
        }else{
            //play Animation
            BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
            BasicCommands.deleteUnit(out,unit);
            // remove unit from AiCurrentUnits
            if(gameState.getAiCurrentUnits().contains(unit)){
                gameState.getAiCurrentUnits().remove(unit);
                // remove unit from HumanCurrentUnits
            }else{
                gameState.getHumanCurrentUnits().remove(unit);
            }
        }
    }

    public void reset(){
        //reset attack times
        if(attacker.getId()==6||attacker.getId()==7||
                attacker.getId()==28||attacker.getId()==29){
            attacker.resetAttackTimes(2);
        }else {
            attacker.resetAttackTimes(1);
        }
    }

}
