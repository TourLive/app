package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class RiderRanking extends RealmObject {
    @PrimaryKey
    private String id;
    private String type;
    private int rank;

    @LinkingObjects("riderRankings")
    private final RealmResults<RiderStageConnection> riderRankings = null;

    public RankingType getType() {
        return RankingType.valueOf(type);
    }

    public void setType(RankingType type) {
        this.type = type.toString();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }
}
