package aiaction;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.*;
import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements the AI's play cards, move, and attack
 * */
public class AIAction implements Runnable{
    private static AIAction aiAction=new AIAction();
    private GameState gameState;
    private ActorRef out;
    private Lock lock=new ReentrantLock();
    private Condition condition= lock.newCondition();

    private AIAction(){}

    public static AIAction getInstance(){
        return aiAction;
    }

    /**
     * AI uses Card, summonUnit or Spell
     */
    public synchronized boolean playCard(Card aiCard){
            //if true-> has available card to summon
            int position =gameState.getAiCardsInHands().indexOf(aiCard);
            System.out.println("Ai want to play card " + aiCard.getCardname());
            //highLight and store available tiles
            ((Summon)ObjectIOC.getInstance().getBean("Summon")).playCard(out,gameState,position, gameState.getMode());
            //summon
            Tile target = null;
            if(aiCard.getUnit() != null){
                target = Ai.getInstance().selectMoveOrSummon(gameState.getCurrentTiles(),gameState.getAiAvatar(),gameState,1);
                if(target == null){
                    gameState.setCurrentCard(null);
                    return false;
                }
            }
            if(aiCard.getSpell() != null){
                if(aiCard.getSpell().getKind() == 2){
                    target = gameState.getAiAvatar().getTile();
                    //AI doesn't want to use this magic card
                }else if(aiCard.getSpell().getKind() == 3){
                    return false;
                }else {
                    target = Ai.getInstance().selectSpell(gameState.getAiAvatar(),gameState);
                    if(target == null){
                        gameState.setCurrentCard(null);
                        return false;
                    }
                }
            }

            if(target != null){
                ((Summon)ObjectIOC.getInstance().getBean("Summon")).summonOrSpell(out,gameState, gameState.getCurrentCard(),target, gameState.getMode());
                gameState.setCurrentCard(null);
                return true;
            }

            return false;
    }

    public synchronized void moveAndAttack(){
        ArrayList<Unit> aiCurrentUnits = gameState.getAiCurrentUnits();
        Unit unit ;
        for(int i = aiCurrentUnits.size() - 1; i >=0;i--){
            unit = aiCurrentUnits.get(i);
            Tile clickedTile = unit.getTile();
            gameState.setCurrentUnit(unit);
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 1);
            ArrayList<Tile> enemies = ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).ShowAttackScope(out,unit.getTile(),1);
            BasicCommands.unHighlighting(out,gameState);
            Tile attackTile = Ai.getInstance().selectAttack(enemies,unit,gameState);
            if(attackTile != null){
               ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 1);
                ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).UnitMoveAndAttackCom(out,clickedTile,attackTile);
                gameState.setCurrentUnit(null);
                try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
                continue;
            }

            ArrayList<Tile> moveScope = ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).GetMoveScope(out,unit.getTile(),1);
            //If the AI has no grid to move, end the action
            if(moveScope.size() == 0){
               continue;
            }
            // select move tile
            Tile moveTile = Ai.getInstance().selectMoveOrSummon(moveScope,unit,gameState,0);

            if(moveTile != null){

                ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 1);
                ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).UnitMoveAndAttackCom(out,clickedTile,moveTile);

                gameState.setCurrentUnit(null);
                try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
                continue;
            }else{
                Tile endTile = findAvailableTiles();

                ((Abilities)ObjectIOC.getInstance().getBean("Abilities")).existProvoke(out,gameState, gameState.getCurrentUnit(), 0);
                ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).UnitMoveAndAttackCom(out,clickedTile,endTile);

                gameState.setCurrentUnit(null);
                try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
                continue;
            }
        }
    }
    /**
     * find an available tile from highlighting tiles
     * @param
     * @return
     */
    public Tile findAvailableTiles(){
        Random r=new Random();
        ArrayList<Tile> totalTiles = ((UnitMove) ObjectIOC.getInstance().getBean("UnitMove")).getTotalTiles(); //UnitMove.getInstance().getTotalTiles();
        int size = totalTiles.size();
        int index = r.nextInt(size);
        return totalTiles.get(index);
    }

    public  void aiStartsPlay(){
        BasicCommands.addPlayer1Notification(out,"Now, it's your turn",3);
        //AI uses all available cards
        ArrayList<Card> aiCards = ((Summon)ObjectIOC.getInstance().getBean("Summon")).checkAvailableCard(gameState);
        for(int i = aiCards.size()-1; i>=0;i--){
            if(!playCard(aiCards.get(i))){
                continue;
            }
        }
        moveAndAttack();
        end();
    }

    public void end(){
        //draw card before switching mode
        ((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).drawCard(out,gameState,gameState.getMode());
       // EndTurn.getInstance().drawCard(out,gameState,gameState.getMode());
        //switch mode to human player
        while(((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).getMessageLength()>0||
                ((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender")).getSizeOfMessageQueue()>0)
        {
            System.out.println("There are some attacks or movements"+((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).getMessageLength()+" "+((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender")).getSizeOfMessageQueue());
            for(int i=0;i<((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender")).getSizeOfMessageQueue();i++)
            {
                System.out.println(((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender")).GetFirst().getEndTile().getTilex());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).InitTurn(gameState,gameState.getMode());
        gameState.switchMode();
        //turn+1
        gameState.addTurn();
        //human will gain manna,because the turn ends
        ((EndTurn)ObjectIOC.getInstance().getBean("EndTurn")).setMana(out,gameState,gameState.getMode());
        //EndTurn.getInstance().setMana(out,gameState,gameState.getMode());
        BasicCommands.addPlayer1Notification(out,"It's my turn",3);
    }


    public void run(){
        while (((AttackContainer)ObjectIOC.getInstance().getBean("AttackContainer")).getMessageLength()>0&&
                ((UnitMoveSender)ObjectIOC.getInstance().getBean("UnitMoveSender")).getSizeOfMessageQueue()>0){
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        aiAction();
    }

    public void aiAction(){
        lock.lock();
        while (gameState.getMode()==0) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        aiStartsPlay();
        lock.unlock();

    }

    public void actionSignal(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setOut(ActorRef out) {
        this.out = out;
    }
}
