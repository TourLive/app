package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RiderRepository implements IRiderRepository {

    @Override
    public void addRider(Rider rider, OnSaveRiderCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Rider transferRider = rider;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Rider realmRider = realm.createObject(Rider.class, UUID.randomUUID().toString());
                realmRider.setName(transferRider.getName());
                realmRider.setCountry(transferRider.getCountry());
                realmRider.setStartNr(transferRider.getStartNr());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllRiders(OnGetAllRidersCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Rider> results = realm.where(Rider.class).findAll();
        RealmList<Rider> res = new RealmList<>();
        res.addAll(results);

        if (callback != null)
            callback.onSuccess(res);
    }

    @Override
    public RealmList<Rider> getAllRidersReturned() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Rider> results = realm.where(Rider.class).findAll();
        RealmList<Rider> res = new RealmList<>();
        res.addAll(results);
        return res;
    }

    @Override
    public void clearAllRiders(){
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        realm.where(Rider.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
