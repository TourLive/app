package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Reward extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private Integer rewardId;
    @Required
    private String type;

    private RealmList<Integer> points;

    private RealmList<Integer> money;

    private RealmList<Judgement> rewardJudgements;

    private RealmList<RewardRiderConnection> rewardRiderConnections;

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public RealmList<RewardRiderConnection> getRewardRiderConnections() {
        return rewardRiderConnections;
    }

    public void setRewardRiderConnections(RealmList<RewardRiderConnection> rewardRiderConnections) {
        this.rewardRiderConnections = rewardRiderConnections;
    }
}

