package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IMaillotRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MaillotRepository implements IMaillotRepository {
    @Override
    public void addMaillot(Maillot maillot, OnSaveMaillotCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.executeTransaction((Realm db) -> {
            Maillot realmMaillot = db.createObject(Maillot.class, maillot.getId());
            realmMaillot.setColor(maillot.getColor());
            realmMaillot.setName(maillot.getName());
            realmMaillot.setPartner(maillot.getPartner());
            realmMaillot.setType(maillot.getType());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllMaillots(OnGetAllMaillotsCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Maillot> results = realm.where(Maillot.class).findAll();
        RealmList<Maillot> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    @Override
    public RealmList<Maillot> getAllMaillotsReturned() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Maillot> results = realm.where(Maillot.class).findAll();
        RealmList<Maillot> res = new RealmList<>();
        res.addAll(results);

        return res;
    }

    @Override
    public void addRiderToMaillot(Maillot maillot, int riderDbId) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        Maillot realmMaillot = realm.where(Maillot.class).equalTo("id", maillot.getId()).findFirst();
        Rider rider = realm.where(Rider.class).equalTo("id", riderDbId).findFirst();
        realm.beginTransaction();
        realmMaillot.setRider(rider);
        realm.commitTransaction();
    }

    @Override
    public Maillot getMaillotById(int id) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(Maillot.class).equalTo("id", id).findFirst();
    }

    @Override
    public void clearAllMaillots() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Maillot.class).findAll().deleteAllFromRealm());
    }
}
