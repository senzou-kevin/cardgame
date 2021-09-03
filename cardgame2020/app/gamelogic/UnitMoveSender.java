package gamelogic;

import akka.actor.ActorRef;
import annotation.IOCResource;
import annotation.IOCService;
import commands.BasicCommands;
import structures.basic.MoveMessage;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@IOCService(name = "UnitMoveSender")
public class UnitMoveSender implements Runnable{
    private Unit lastMove;
    private Lock lock=new ReentrantLock();
    @IOCResource
    UnitMove  unitMove;
    private Condition condition= lock.newCondition();
    private volatile LinkedList<MoveMessage> moveQueue=new LinkedList<>();
    public void MoveUnitToTile()
    {
        lock.lock();
        while(unitMove.isMoving()==true)
        {
            try{condition.await();}catch(Exception e){e.printStackTrace();}
        }
        if(moveQueue.size()>0)
        {
            MoveMessage message=moveQueue.remove();
            if(lastMove==null)
            {
                lastMove=message.getCurrentUnit();
                unitMove.getCurrentState().setCanAttack(true);
            }
            else
                {
                    unitMove.getCurrentState().setCanAttack(true);
                }
            BasicCommands.moveUnitToTile(message.getOut(),message.getCurrentUnit(),message.getEndTile());
            message.getCurrentUnit().setCanAttack(message.isCanAttack());
            System.out.println(message.getCurrentUnit().getId()+"正在移动");
            unitMove.setMoving(true);
        }
        lock.unlock();
    }
    public void MoveSignal()
    {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
    public void AddMoveMessage(Unit currentUnit,Tile endTile,ActorRef out,boolean canAttack)
    {
        moveQueue.add(new MoveMessage(currentUnit,endTile,out,canAttack));
    }
    public void AddMoveMessage(MoveMessage message)
    {
        moveQueue.add(message);
    }
    public void RemoveMoveMessage(int id,Tile endtile)
    {
        for(MoveMessage move:moveQueue)
        {
            if (move.equals(id,endtile))
            {
                System.out.println("删除移动信息");
                moveQueue.remove(move);
                return;
            }
        }
    }
    public Condition getCondition() {
        return condition;
    }
    public int getSizeOfMessageQueue()
    {
        return moveQueue.size();
    }
    @Override
    public void run() {
        while(true){
            MoveUnitToTile();
        }
    }
    public MoveMessage GetFirst()
    {
        return moveQueue.getFirst();
    }
}
