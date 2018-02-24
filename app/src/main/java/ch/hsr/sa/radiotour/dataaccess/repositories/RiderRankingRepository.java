package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRankingRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import io.realm.Realm;

public class RiderRankingRepository implements IRiderRankingRepository {
    @Override
    public void addRiderRanking(RiderRanking riderRanking, OnSaveRiderRankingCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.executeTransaction((Realm db) -> {
            RiderRanking realmRiderRanking = db.createObject(RiderRanking.class, UUID.randomUUID().toString());
            realmRiderRanking.setType(riderRanking.getType());
            realmRiderRanking.setRank(riderRanking.getRank());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public RiderRanking getFirstInRanking(RankingType type) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(RiderRanking.class).equalTo("type", type.toString()).equalTo("rank", 1).findFirst();
    }

    @Override
    public RiderRanking getRiderRanking(RiderRanking riderRanking){
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(RiderRanking.class).equalTo("type", riderRanking.getType().toString()).equalTo("rank", riderRanking.getRank()).findFirst();
    }

    @Override
    public void clearAllRiderRankings() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(RiderRanking.class).findAll().deleteAllFromRealm());
    }
}
