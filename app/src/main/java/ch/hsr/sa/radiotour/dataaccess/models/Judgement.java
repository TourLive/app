package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Judgement extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;
    int rewardId;

    private int distance;

    @LinkingObjects("rewardJudgements")
    private final RealmResults<Reward> rewards = null;

    @LinkingObjects("specialRankingJudgements")
    private final RealmResults<SpecialRanking> specialRankings = null;

    public SpecialRanking getSpecialRankings(){
        return this.specialRankings.first();
    }

    public Reward getRewards() { return this.rewards.first(); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }
}
