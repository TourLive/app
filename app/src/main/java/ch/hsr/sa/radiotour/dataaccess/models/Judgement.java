package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Judgement extends RealmObject {

    @LinkingObjects("rewardJudgements")
    private final RealmResults<Reward> rewards = null;
    @LinkingObjects("judgements")
    private final RealmResults<JudgmentRiderConnection> judgmentRiderConnections = null;
    @Expose
    int rewardId;
    @PrimaryKey
    private long id;
    @Required
    private String name;
    private double distance;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Reward getRewards() {
        return this.rewards.first();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public RealmList<JudgmentRiderConnection> getJudgmentRiderConnection() {
        RealmList<JudgmentRiderConnection> list = new RealmList<>();
        list.addAll(judgmentRiderConnections);
        return list;
    }

}
