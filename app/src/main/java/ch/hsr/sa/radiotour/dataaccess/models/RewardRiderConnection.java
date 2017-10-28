package ch.hsr.sa.radiotour.dataaccess.models;


import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RewardRiderConnection extends RealmObject {
    @PrimaryKey
    private String id;
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @LinkingObjects("riderRewardConnections")
    private final RealmResults<Rider> riders = null;

    @LinkingObjects("rewardRiderConnections")
    private final RealmResults<Reward> rewards = null;

    public Rider getRiders() {
        return riders.first();
    }

    public Reward getRewards() {
        return rewards.first();
    }

}
