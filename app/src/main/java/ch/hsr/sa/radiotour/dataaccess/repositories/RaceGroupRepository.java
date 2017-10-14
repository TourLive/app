package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Urs Forrer on 13.10.2017.
 */

public class RaceGroupRepository implements IRaceGroupRepository {
    @Override
    public void addRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RaceGroup transferRaceGroup = raceGroup;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RaceGroup realmRaceGroup = realm.createObject(RaceGroup.class, UUID.randomUUID().toString());
                realmRaceGroup.setType(transferRaceGroup.getType());
                realmRaceGroup.setActualGapTime(transferRaceGroup.getActualGapTime());
                realmRaceGroup.setHistoryGapTime(transferRaceGroup.getHistoryGapTime());
                RealmResults<Rider> results = realm.where(Rider.class).findAll();
                RealmList<Rider> res = new RealmList<>();
                res.addAll(results);

                realmRaceGroup.setPosition(transferRaceGroup.getPosition());
                realmRaceGroup.setRiders(res);
            }
        });

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void getAllRaceGroups(OnGetAllRaceGroupsCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RaceGroup> results = realm.where(RaceGroup.class).findAll();
        RealmList<RaceGroup> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    @Override
    public void clearAllRaceGroups() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        realm.where(RaceGroup.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void deleteRaceGroup() {

    }
}
