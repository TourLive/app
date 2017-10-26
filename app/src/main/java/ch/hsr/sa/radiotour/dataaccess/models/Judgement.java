package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

public class Judgement extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;
    @Required
    private int distance;

    @LinkingObjects("judgements")
    private final RealmResults<SpecialRanking> specialRankings = null;

    public SpecialRanking getSpecialRankings(){
        return this.specialRankings.first();
    }

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
}
