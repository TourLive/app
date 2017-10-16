package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RaceGroup extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String type;
    private long actualGapTime;
    private long historyGapTime;
    private int position;

    private RealmList<Rider> riders;

    public void setRiders(RealmList<Rider> riders) {
        this.riders = riders;
    }

    public RealmList<Rider> getRiders() {
        return riders;
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

    public String getName() {
        switch(getType()) {
            case LEAD:
                return "LEAD";
            case LAST:
                return "LAST";
            case FELD:
                return "FELD";
            case NORMAL:
                return "GROUP " + getPosition();
            default:
                return "DEFAULT";
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
