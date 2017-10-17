package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStateRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.Realm;

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
    public void getRiderState(RiderState riderState, OnGetRiderStateCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RiderState res = realm.where(RiderState.class).equalTo("id", riderState.getId()).findFirst();

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    @Override
    public void clearAllRiderState() {

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
    public void deleteRiderState() {

    }
}
