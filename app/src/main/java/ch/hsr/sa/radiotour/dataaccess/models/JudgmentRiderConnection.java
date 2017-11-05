package ch.hsr.sa.radiotour.dataaccess.models;


import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class JudgmentRiderConnection extends RealmObject {
    @PrimaryKey
    private String id;
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @LinkingObjects("judgmentRiderConnections_riders")
    private final RealmResults<Rider> riders = null;

    @LinkingObjects("judgmentRiderConnections")
    private final RealmResults<Judgement> judgements = null;

    public Rider getRiders() {
        return riders.first();
    }

    public Judgement getRewards() {
        return judgements.first();
    }

}
