package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RiderStageConnection extends RealmObject {
    @LinkingObjects("riderStages")
    private final RealmResults<Rider> riders = null;
    @LinkingObjects("stageConnections")
    private final RealmResults<Stage> stages = null;
    @LinkingObjects("mailRiderConnection")
    private final RealmResults<Maillot> maillots = null;
    private RealmList<RiderRanking> riderRankings;

    @Expose
    @PrimaryKey
    private long id;
    @Expose
    private int bonusPoint;
    @Expose
    private int mountainBonusPoints;
    @Expose
    private int sprintBonusPoints;
    @Expose
    private int bonusTime;
    @Expose
    private int money;
    @Expose
    private long officialTime;
    @Expose
    private long officialGap;
    @Expose
    private long virtualGap;
    @Expose
    private int timeInLeadGroup = 0;
    @Expose
    @Required
    private String typeState;

    public Rider getRiders() {
        if (riders.isEmpty()) {
            return null;
        }
        return riders.first();
    }

    public Stage getStages() {
        return stages.first();
    }

    public Maillot getMaillot() {
        if(maillots.isEmpty()) {
            return null;
        }
        return maillots.first();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(int bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public void appendBonusPoint(int add) {
        this.bonusPoint += add;
    }

    public void removeBonusPoint(int sub) {
        this.bonusPoint -= sub;
    }

    public int getMountainBonusPoints() {
        return mountainBonusPoints;
    }

    public void setMountainBonusPoints(int mountainBonusPoints) {
        this.mountainBonusPoints = mountainBonusPoints;
    }

    public void appendMountainBonusPoints(int add) {
        this.mountainBonusPoints += add;
    }

    public void removeMountainBonusPoints(int sub) {
        this.mountainBonusPoints -= sub;
    }

    public int getSprintBonusPoints() {
        return sprintBonusPoints;
    }

    public void setSprintBonusPoints(int sprintBonusPoints) {
        this.sprintBonusPoints = sprintBonusPoints;
    }

    public void appendSprintBonusPoints(int add) {
        this.sprintBonusPoints += add;
    }

    public void removeSprintBonusPoints(int sub) {
        this.sprintBonusPoints -= sub;
    }

    public int getBonusTime() {
        return bonusTime;
    }

    public void setBonusTime(int bonusTime) {
        this.bonusTime = bonusTime;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void appendMoney(int add) {
        this.money += add;
    }

    public void removeMoney(int sub) {
        this.money -= sub;
    }

    public void appendBonusTime(int add) {
        this.bonusTime += add;
    }

    public void removeBonusTime(int sub) {
        this.bonusTime -= sub;
    }

    public long getOfficialTime() {
        return officialTime;
    }

    public void setOfficialTime(long officialTime) {
        this.officialTime = officialTime;
    }

    public long getOfficialGap() {
        return officialGap;
    }

    public void setOfficialGap(long officialGap) {
        this.officialGap = officialGap;
    }

    public long getVirtualGap() {
        return virtualGap;
    }

    public void setVirtualGap(long virtualGap) {
        this.virtualGap = virtualGap;
    }

    public RiderStateType getType() {
        return RiderStateType.valueOf(typeState);
    }

    public void setType(RiderStateType type) {
        this.typeState = type.toString();
    }

    public void addRiderRanking(RiderRanking riderRanking) {
        this.riderRankings.add(riderRanking);
    }

    public RiderRanking getRiderRanking(RankingType type) {
        if (this.riderRankings.isEmpty()) {
            return null;
        }
        RiderRanking temp = new RiderRanking();
        for(RiderRanking r : this.riderRankings){
            if(r.getType() == type){
                temp = r;
                break;
            }
        }
        return temp;
    }

    public int getTimeInLeadGroup() {
        return timeInLeadGroup;
    }

    public void appendTimeInLeadGroup(int add) {
        this.timeInLeadGroup += add;
    }
}
