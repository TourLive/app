package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Rider extends RealmObject {
    @PrimaryKey
    private String id;

    private int startNr;
    @Required
    private String name;
    @Required
    private String country;

    @LinkingObjects("ridersStageConnections")
    private final RealmResults<RiderStageConnection> stageConnections = null;

    @LinkingObjects("riders")
    private final RealmResults<RaceGroup> raceGroups = null;

    public int getStartNr() {
        return startNr;
    }

    public void setStartNr(int startNr) { this.startNr = startNr; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
