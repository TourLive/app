package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Stage extends RealmObject {
    @PrimaryKey
    private String id;
    private int stageId;
    private int raceId;
    private String raceName;
    @Required
    private String name;
    @Required
    private Date startTime;
    @Required
    private Date endTime;
    private int distance;
    @Required
    private String type;
    @Required
    private String from;
    @Required
    private String to;

    private RealmList<RiderStageConnection> stageConnections;

    private RealmList<Maillot> maillotConnections;

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getRaceId() { return raceId;}

    public void setRaceId(int raceId) {this.raceId = raceId;}

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public StageType getType() {
        return StageType.valueOf(type);
    }

    public void setType(StageType type) {
        this.type = type.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public RealmList<RiderStageConnection> getStageConnections() {
        return stageConnections;
    }

    public void setStageConnections(RealmList<RiderStageConnection> stageConnections) {
        this.stageConnections = stageConnections;
    }

    public RealmList<Maillot> getMaillotConnections() {
        return maillotConnections;
    }

    public void setMaillotConnections(RealmList<Maillot> maillotConnections) {
        this.maillotConnections = maillotConnections;
    }
}
