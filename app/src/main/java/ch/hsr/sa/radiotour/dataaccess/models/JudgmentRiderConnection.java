package ch.hsr.sa.radiotour.dataaccess.models;


import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class JudgmentRiderConnection extends RealmObject {
    @Expose
    @PrimaryKey
    private String id;
    @Expose
    private int rank;
    @Expose
    private RealmList<Rider> rider = null;
    @Expose
    private RealmList<Judgement> judgements;

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public RealmList<Judgement> getJudgements() {
        return judgements;
    }

    public void setJudgements(RealmList<Judgement> judgements) {
        this.judgements = judgements;
    }

    public RealmList<Rider> getRider() {
        return rider;
    }

    public void setRider(RealmList<Rider> rider) {
        this.rider = rider;
    }

}
