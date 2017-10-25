package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IRiderRepository {

    interface OnSaveRiderCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllRidersCallback {
        void onSuccess(RealmList<Rider> riders);
        void onError(String message);
    }

    interface OnUpdateRiderStageCallback {
        void onSuccess();
        void onError(String message);
    }

    void addRider(Rider rider, OnSaveRiderCallback callback);

    void getAllRiders(OnGetAllRidersCallback callback);

    RealmList<Rider> getAllRidersReturned();

    void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections, OnUpdateRiderStageCallback callback);

    void clearAllRiders();
}
