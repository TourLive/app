package ch.hsr.sa.radiotour.dataaccess;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Dom on 12.10.2017.
 */

public class RiderRepository implements IRiderRepository {

    @Override
    public void addRider(Rider rider, OnSaveRiderCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        Rider realmRider = realm.createObject(Rider.class, UUID.randomUUID().toString());
        realmRider.setName(rider.getName());
        realmRider.setCountry(rider.getCountry());
        realmRider.setStartNr(rider.getStartNr());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllRiders(OnGetAllRidersCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Rider> results = realm.where(Rider.class).findAll();
        RealmList<Rider> res = new RealmList<Rider>();
        res.addAll(results);

        if (callback != null)
            callback.onSuccess(res);
    }
}
