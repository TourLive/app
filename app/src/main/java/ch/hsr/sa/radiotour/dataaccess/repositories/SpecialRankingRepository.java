package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.ISpecialRankingRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;
import io.realm.Realm;

public class SpecialRankingRepository implements ISpecialRankingRepository {
    @Override
    public void addSpecialRanking(SpecialRanking specialRanking, OnSaveSpecialRankingCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final SpecialRanking transferSpecialRanking = specialRanking;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SpecialRanking realmSpecialRanking = realm.createObject(SpecialRanking.class, UUID.randomUUID().toString());
                realmSpecialRanking.setName(transferSpecialRanking.getName());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllSpecialRankings() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               realm.where(SpecialRanking.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
