package structures.basic;

import akka.actor.ActorRef;

public class MoveMessage {
    private Unit currentUnit;
    private Tile endTile;
    private ActorRef out;
    private boolean canAttack=false;
    public MoveMessage(Unit unit,Tile tile,ActorRef out,boolean canAttack)
    {
        this.currentUnit=unit;
        this.endTile=tile;
        this.out=out;
        this.canAttack=canAttack;
    }
    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public Tile getEndTile() {
        return endTile;
    }

    public void setEndTile(Tile endTile) {
        this.endTile = endTile;
    }

    public ActorRef getOut() {
        return out;
    }

    public void setOut(ActorRef out) {
        this.out = out;
    }
    public boolean equals(int id,Tile endTile)
    {
        if(this.currentUnit.getId()==id&&this.endTile.equals(endTile))return true;

        return false;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }
}
