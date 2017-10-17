package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStateRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RiderStateRepository implements IRiderStateRepository {
    @Override
    public void addRiderState(RiderState riderState, OnSaveRiderStateCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RiderState transferRiderState = riderState;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RiderState realmRiderState = realm.createObject(RiderState.class, UUID.randomUUID().toString());
                realmRiderState.setType(transferRiderState.getType());
            }
        });

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void getRiderState(Rider rider, OnGetRiderStateCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RiderStageConnection res = realm.where(RiderStageConnection.class).
                equalTo("ridersStageConnections.startNr", rider.getStartNr()).findFirst();

        if (callback != null) {
            callback.onSuccess(res.getRiderState().first());
        }
    }

    @Override
    public void clearAllRiderState() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RiderState.class).findAll().deleteAllFromRealm();
            }
        });
    }

    @Override
    public void updateRiderState(final RiderState newRiderState, OnUpdateRiderStateCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RiderState res = realm.where(RiderState.class).equalTo("id", newRiderState.getId()).findFirst();
        res.setType(newRiderState.getType());

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void getAllRiderStates(OnGetAllRidersStatesCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderState> results = realm.where(RiderState.class).findAll();
        RealmList<RiderState> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }


    @Override
    public void deleteRiderState() {

    }
}
