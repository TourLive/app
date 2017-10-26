package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class SpecialRanking extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;

    private RealmList<Judgement> specialRankingJudgements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Judgement> getSpecialRankingJudgements() {
        return specialRankingJudgements;
    }

    public void setSpecialRankingJudgements(RealmList<Judgement> judgements) {
        this.specialRankingJudgements = judgements;
    }
}
