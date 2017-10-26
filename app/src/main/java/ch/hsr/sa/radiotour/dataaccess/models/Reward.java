package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.ArrayList;

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
    @Required
    private ArrayList<Integer> points;
    @Required
    private ArrayList<Integer> money;

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

    public ArrayList<Integer> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Integer> points) {
        this.points = points;
    }

    public ArrayList<Integer> getMoney() {
        return money;
    }

    public void setMoney(ArrayList<Integer> money) {
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

