package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IMaillotRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.Realm;

public class MaillotRepository implements IMaillotRepository {
    @Override
    public void addMaillot(Maillot maillot, OnSaveMaillotCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.executeTransaction((Realm db) -> {
            Maillot realmMaillot = db.createObject(Maillot.class, UUID.randomUUID().toString());
            realmMaillot.setColor(maillot.getColor());
            realmMaillot.setDbIDd(maillot.getDbIDd());
            realmMaillot.setName(maillot.getName());
            realmMaillot.setPartner(maillot.getPartner());
            realmMaillot.setType(maillot.getType());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllMaillots() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Maillot.class).findAll().deleteAllFromRealm());
    }
}
