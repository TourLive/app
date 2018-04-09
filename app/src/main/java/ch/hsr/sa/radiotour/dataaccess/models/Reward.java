package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Reward extends RealmObject {
    @PrimaryKey
    private long id;
    @Required
    private String type;

    private RealmList<Integer> points;

    private RealmList<Integer> money;

    private RealmList<Judgement> rewardJudgements;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RewardType getType() {
        return RewardType.valueOf(type);
    }

    public void setType(RewardType type) {
        this.type = type.toString();
    }

    public RealmList<Integer> getPoints() {
        return points;
    }

    public void setPoints(RealmList<Integer> points) {
        this.points = points;
    }

    public RealmList<Integer> getMoney() {
        return money;
    }

    public void setMoney(RealmList<Integer> money) {
        this.money = money;
    }

    public RealmList<Judgement> getRewardJudgements() {
        return rewardJudgements;
    }

    public void setRewardJudgements(RealmList<Judgement> judgements) {
        this.rewardJudgements = judgements;
    }
}

