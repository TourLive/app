package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RaceGroup extends RealmObject {
    @PrimaryKey
    private String id;
    private Long dbRaceGroupid;

    @Required
    private String type;
    private long actualGapTime;
    private long historyGapTime;
    private int position;

    private RealmList<Rider> riders;

    public void setId(String id) { this.id = id;}

    public String getId() {
        return id;
    }

    public RealmList<Rider> getRiders() {
        return riders;
    }

    public void setRiders(RealmList<Rider> riders) {
        this.riders = riders;
    }

    public void appendRiders(RealmList<Rider> newRiders) {
        this.riders.addAll(newRiders);
    }

    public void removeRider(Rider rider) {
        this.riders.remove(rider);
    }

    public int getRidersCount() {
        return riders.size();
    }

    public RaceGroupType getType() {
        return RaceGroupType.valueOf(type);
    }

    public void setType(RaceGroupType type) {
        this.type = type.toString();
    }

    public long getActualGapTime() {
        return actualGapTime;
    }

    public void setActualGapTime(long actualGapTime) {
        this.actualGapTime = actualGapTime;
    }

    public long getHistoryGapTime() {
        return historyGapTime;
    }

    public void setHistoryGapTime(long historyGapTime) {
        this.historyGapTime = historyGapTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        switch (getType()) {
            case LEAD:
                return "SPITZENGRUPPE";
            case FELD:
                return "FELD";
            case NORMAL:
                return "GRUPPE " + getPosition();
            default:
                return "DEFAULT";
        }
    }

    public Long getDbRaceGroupid() {
        return dbRaceGroupid;
    }

    public void setDbRaceGroupid(Long dbRaceGroupid) {
        this.dbRaceGroupid = dbRaceGroupid;
    }
}
