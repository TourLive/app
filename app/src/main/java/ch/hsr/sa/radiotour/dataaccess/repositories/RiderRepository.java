package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.Random;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RiderRepository implements IRiderRepository {
    @Override
    public void addRider(Rider rider, OnSaveRiderCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Rider transferRider = rider;
        if (rider.getId() == 0) {
            rider.setId(new Random().nextLong());
        }

        realm.executeTransaction((Realm db) -> {
            // TODO
            Rider realmRider = db.createObject(Rider.class, rider.getId());
            realmRider.setName(transferRider.getName());
            realmRider.setCountry(transferRider.getCountry());
            realmRider.setStartNr(transferRider.getStartNr());
            realmRider.setTeamName(transferRider.getTeamName());
            realmRider.setTeamShortName(transferRider.getTeamShortName());
            realmRider.setUnknown(transferRider.isUnknown());
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
    public void removeRider(Rider rider, OnSaveRiderCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        Rider foundRider = realm.where(Rider.class).equalTo("id", rider.getId()).findFirst();
        foundRider.deleteFromRealm();
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess();
        }
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
    public RealmList<Rider> getAllActiveRidersReturned() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Rider> results = realm.where(Rider.class).isNotEmpty("raceGroups").findAll();
        RealmList<Rider> res = new RealmList<>();
        res.addAll(results);
        return res;
    }

    @Override
    public Rider getRiderByStartNr(int startNr) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(Rider.class).equalTo("startNr", startNr).findFirst();
    }

    @Override
    public RealmList<Rider> getAllUnknownRidersReturned() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Rider> results = realm.where(Rider.class).equalTo("isUnknown", true).findAll().sort("startNr");
        RealmList<Rider> res = new RealmList<>();
        res.addAll(results);
        return res;
    }

    @Override
    public void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections, OnUpdateRiderStageCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        Rider res = realm.where(Rider.class).equalTo("startNr", rider.getStartNr()).findFirst();
        res.removeStages();
        realm.commitTransaction();

        realm.beginTransaction();
        Rider realmRider = realm.where(Rider.class).equalTo("startNr", rider.getStartNr()).findFirst();
        realmRider.appendStages(connections);
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void clearAllRiders() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Rider.class).findAll().deleteAllFromRealm());
    }
}
