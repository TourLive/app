package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Urs Forrer on 13.10.2017.
 */

public class RaceGroup extends RealmObject {
    @PrimaryKey
    private String id;

    @Required
    private String type;
    private int actualGapTime;
    private int historyGapTime;
    private int position;

    private RealmList<Rider> riders;

    public void setRiders(RealmList<Rider> riders) {
        this.riders = riders;
    }

    public RealmList<Rider> getRiders() {
        return riders;
    }

    public void appendRider(Rider rider) {
        riders.add(rider);
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

    public int getActualGapTime() {
        return actualGapTime;
    }

    public void setActualGapTime(int actualGapTime) {
        this.actualGapTime = actualGapTime;
    }

    public int getHistoryGapTime() {
        return historyGapTime;
    }

    public void setHistoryGapTime(int historyGapTime) {
        this.historyGapTime = historyGapTime;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return "HALLO";
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
