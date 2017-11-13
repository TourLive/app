package ch.hsr.sa.radiotour.dataaccess.models;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    private RealmList<Rider> rider = null;

    private RealmList<Judgement> judgements;

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
