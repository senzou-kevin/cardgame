package structures.basic;

import gamelogic.Attack;

public class AttackMessage {
    private  Unit currentUnit;
    private Unit tarGet;
    public AttackMessage(Unit currentUnit,Unit tarGet)
    {

        this.currentUnit=currentUnit;
        this.tarGet=tarGet;
    }
    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public Unit getTarGet() {
        return tarGet;
    }

    public void setTarGet(Unit tarGet) {
        this.tarGet = tarGet;
    }
}
