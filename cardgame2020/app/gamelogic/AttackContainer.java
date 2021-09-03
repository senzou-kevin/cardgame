package gamelogic;

import annotation.IOCService;
import structures.basic.AttackMessage;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.LinkedList;
import java.util.List;

@IOCService(name = "AttackContainer")
public class AttackContainer {
    private volatile List<AttackMessage> attackMessageList=new LinkedList<>();
    public void addAttack(AttackMessage attackMessage)
    {    System.out.println("存入一条攻击"+attackMessage.getCurrentUnit().getId());
        attackMessageList.add(attackMessage);
    }
    public AttackMessage getAttackMessage(int index)
    {
        return attackMessageList.remove(index);
    }
    public boolean findByID(int index)
    {
        for(AttackMessage message:attackMessageList){
            if(message.getCurrentUnit().getId()==index)return true;
        }
        return false;
    }
    public Tile getTargetTile(int index){
        for(AttackMessage message:attackMessageList){
            if(message.getCurrentUnit().getId()==index)return message.getTarGet().getTile();
        }
        return null;
    }
    public Unit getAttacker(int index)
    {
        for(AttackMessage message:attackMessageList){
            if(message.getCurrentUnit().getId()==index)return message.getCurrentUnit();
        }
        return null;
    }
    public void removeElementById(int index)
    {
        System.out.println("移除攻击");
        for(AttackMessage message:attackMessageList){
            if(message.getCurrentUnit().getId()==index) attackMessageList.remove(message);
        }
    }
    public void removeElementByAttacker(Unit attacker)
    {

        System.out.println("移除攻击");
        for(AttackMessage message:attackMessageList){
            if(message.getCurrentUnit().equals(attacker)) {attackMessageList.remove(message);
                System.out.println(message.getCurrentUnit().getId()+"移除攻击完毕");};
        }
    }
    public int getMessageLength()
    {
        return attackMessageList.size();
    }
}

