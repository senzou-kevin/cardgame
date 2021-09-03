package aiaction;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import java.util.ArrayList;
/**
 *The function of this class is to help the AI calculate the gains and losses after the action.
 *The AI will select the most profitable target in the current situation based on the calculation results,
 *such as attack target, moving location, spell target
 * */
public class Ai {

    private static Ai ai = new Ai();

    public Ai() {
    }
    public static Ai getInstance(){
        return ai;
    }
    /**
     * Threat:
     * Rule 1: AI attacks units closer to it first
     * Rule 2: AI attacks units closer to the AiAvatar first
     *
     * */
    public int distanceMenace(Unit enemy,Unit curUnit,GameState gameState){
        int distance = Math.abs(enemy.getTile().getTilex() - curUnit.getTile().getTilex()) +
                Math.abs(enemy.getTile().getTiley() - curUnit.getTile().getTiley());

        if(curUnit == gameState.getAiAvatar()){
            return distance*5;
        }else{
            return distance*20;
        }

    }
    /**
     * Selecting threat
     * */
    public Tile threat(ArrayList<Tile> enemies,Unit curUnit, GameState gameState){
        int threat = 0;
        int maxScore = Integer.MIN_VALUE;
        Tile target = null;
        for(Tile tile: enemies){
            threat = tile.getUnit().getAttack()*60 + tile.getUnit().getAttack()*30 - distanceMenace(tile.getUnit(),curUnit,gameState);
            if(threat > maxScore){
                maxScore = threat;
                target = tile;
            }
        }
        return target;
    }

    /**
     * Damage score:
     * Rule 1: If AI kill with a single hit, AI gain additional kill scores
     * Rule 2: If AI attack a Unit and then AI is countered by this Unit, the benefit of the attack is reduced
     * Rule 3: AI will attack units with low health and high damage
     * */
    public int dealtScore(Unit enemy, Unit curUnit, GameState gameState){
        int dealtScore = enemy.getAttack() * 30 + (curUnit.getAttack()/enemy.getHealth()) * 100;
        int injured = curUnit.getAttack() * 30 + (enemy.getAttack()/curUnit.getHealth()) * 100;

        if(enemy == gameState.getHumanAvatar()){
            //HumanAvatar's life is more precious
            dealtScore = (int) (dealtScore*2);
        }

        if(curUnit == gameState.getAiAvatar()){
            //AiAvatar's life is more precious
            injured = (int) (injured*1.5);
        }

        // Kill scores
        if(curUnit.getAttack() - enemy.getHealth() == 0){
            return dealtScore + death(enemy,gameState);
        }
        if (curUnit.getAttack() - enemy.getHealth() > 0){
            //ai get injured after an attack
            if(enemy.getAttack() - curUnit.getHealth() == 0){
                return dealtScore - injured - death(curUnit,gameState);
            }
        }
        return dealtScore - injured;
    }
    /**
     * Kill scores: Death Unit's Attack + Death Unit's MaxHealth
     * Rule 1: Kill Human will get the highest score
     * Rule 2: Kill units with high damage will get more kill points
     * */
    public int death(Unit curUnit, GameState gameState){
        if(curUnit == gameState.getHumanAvatar() || curUnit == gameState.getAiAvatar()){
            return 999999999;
        }else{
            return curUnit.getAttack() * 60 + curUnit.getMaxHealth()*20;
        }
    }
    /**
     * Calculate the attack score
     * */
    public int attackScore(Unit enemy, Unit curUnit,GameState gameState){
        int distanceMenace= distanceMenace(enemy,curUnit,gameState);
        int dealtScore = dealtScore(enemy,curUnit,gameState);
        int attackScore = dealtScore - distanceMenace;

        return attackScore;
    }

    /**
     * Selecting Attacks
     * */
    public Tile selectAttack(ArrayList<Tile> enemies,Unit curUnit, GameState gameState){
        if(enemies.size() == 0){
            return null;
        }
        int attackScore = 0;
        int maxScore = Integer.MIN_VALUE;
        Tile target = null;
        for(Tile tile: enemies){
            attackScore = attackScore(tile.getUnit(),curUnit,gameState);
            if(attackScore > maxScore){
                maxScore = attackScore;
                target = tile;
            }
        }
        return target;
    }

    /**
     * Select Move or Summon Destination
     * mode 0 : Move
     * mode 1 : Summon
     * Rule 1: Melee Unit will move closer to the units with the most attack value
     * Rule 2: Ranged units will try to avoid being close by enemies
     * */
    public Tile selectMoveOrSummon(ArrayList<Tile> scope, Unit curUnit,GameState gameState,int mode){
        ArrayList<Tile> em = new ArrayList<>();
        for(Unit unit:gameState.getHumanCurrentUnits()){
            em.add(unit.getTile());
        }
        Tile target = null;

        if(mode == 0){
            // AI health is too low, Ai will escape
            if(curUnit == gameState.getAiAvatar() && gameState.getAiAvatar().getHealth() < 5){
                target =escapeMode(scope,em,curUnit,gameState);
                return target;
            }
        }

        //Select the Tile closest to the target
        if(curUnit.getAttackScopeMode() == 0){
            target = attackMode(scope,em,curUnit,gameState);
            return target;
        }
        //Select the grid farthest from the target
        if(curUnit.getAttackScopeMode() == 1){
            target =escapeMode(scope,em,curUnit,gameState);
            return target;
        }
        return target;
    }

    /**
     * Select the units with the most attack value
     * */
    public Tile attackMode(ArrayList<Tile> Scope,ArrayList<Tile> enemies,Unit curUnit,GameState gameState){
        Tile target = null;
        Tile attackTarget = selectAttack(enemies,curUnit,gameState);

        int minDistance = Integer.MAX_VALUE;
        int distance ;

        for(Tile tile:Scope){
            distance = Math.abs(attackTarget.getTilex() - tile.getTilex()) +
                    Math.abs(attackTarget.getTiley() - tile.getTiley());
            if(distance < minDistance){
                minDistance = distance;
                target = tile;
            }
        }

        return target;
    }

    /**
     * Select the Unit that poses the greatest threat
     * */
    public Tile escapeMode(ArrayList<Tile> Scope,ArrayList<Tile> enemies,Unit curUnit,GameState gameState){
        Tile target = null;
        Tile threatTarget = threat(enemies,curUnit,gameState);
        int maxDistance = Integer.MIN_VALUE;
        int distance ;
        for(Tile tile:Scope){
            distance = Math.abs(threatTarget.getTilex() - tile.getTilex()) +
                    Math.abs(threatTarget.getTiley() - tile.getTiley());
            if(distance > maxDistance){
                maxDistance = distance;
                target = tile;
            }
        }
        return target;
    }

    /**
     *Select the target of Ai spell attack
     * */
    public Tile selectSpell(Unit curUnit,GameState gameState){

        ArrayList<Tile> enemies = new ArrayList<>();
        for(Unit unit:gameState.getHumanCurrentUnits()){
            if(unit != gameState.getHumanAvatar()){
                enemies.add(unit.getTile());
            }
        }
        return selectAttack(enemies,curUnit,gameState);
    }

}