package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RiderStageConnection extends RealmObject {
    @PrimaryKey
    private String id;
    private int rank;
    private int bonusPoint;
    private int bonusTime;
    @Required
    private Date officialTime;
    @Required
    private Date officialGap;
    @Required
    private Date virtualGap;
    @Required
    private String typeState;

    @LinkingObjects("riderStages")
    private final RealmResults<Rider> riders = null;

    @LinkingObjects("stageConnections")
    private final RealmResults<Stage> stages = null;


    public Rider getRiders() {
        return riders.first();
    }

    public Stage getStages() { return stages.first(); }

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(int bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public int getBonusTime() {
        return bonusTime;
    }

    public void setBonusTime(int bonusTime) {
        this.bonusTime = bonusTime;
    }

    public Date getOfficialTime() {
        return officialTime;
    }

    public void setOfficialTime(Date officialTime) {
        this.officialTime = officialTime;
    }

    public Date getOfficialGap() {
        return officialGap;
    }

    public void setOfficialGap(Date officialGap) {
        this.officialGap = officialGap;
    }

    public Date getVirtualGap() {
        return virtualGap;
    }

    public void setVirtualGap(Date virtualGap) {
        this.virtualGap = virtualGap;
    }

    public RiderStateType getType() {
        return RiderStateType.valueOf(typeState);
    }

    public void setType(RiderStateType type) {
        this.typeState = type.toString();
    }
}
