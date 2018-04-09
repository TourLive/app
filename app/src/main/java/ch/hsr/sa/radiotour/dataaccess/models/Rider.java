package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Rider extends RealmObject {

    @LinkingObjects("rider")
    private final RealmResults<JudgmentRiderConnection> judgmentRiderConnectionsRiders = null;
    @LinkingObjects("riders")
    private final RealmResults<RaceGroup> raceGroups = null;
    @Expose
    @PrimaryKey
    private long id;
    private int startNr;
    @Required
    private String name;
    @Required
    private String country;
    @Required
    private String teamName;
    @Required
    private String teamShortName;
    private boolean isUnknown = false;
    private RealmList<RiderStageConnection> riderStages;

    public RaceGroup getRaceGroups() {
        try {
            return raceGroups.first();
        } catch (Exception ex) {
            return null;
        }
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartNr() {
        return startNr;
    }

    public void setStartNr(int startNr) {
        this.startNr = startNr;
    }

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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }

    public RealmList<RiderStageConnection> getRiderStages() {
        return riderStages;
    }

    public void setRiderStages(RealmList<RiderStageConnection> riderStages) {
        this.riderStages = riderStages;
    }

    public void appendStages(RealmList<RiderStageConnection> newStageConnections) {
        this.riderStages.addAll(newStageConnections);
    }

    public void removeStage(RiderStageConnection stageConnection) {
        this.riderStages.remove(stageConnection);
    }

    public JudgmentRiderConnection getJudgmentRewardConnection() {
        return judgmentRiderConnectionsRiders.first();
    }

    public void removeStages() {
        this.riderStages.clear();
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public void setUnknown(boolean unknown) {
        isUnknown = unknown;
    }
}
