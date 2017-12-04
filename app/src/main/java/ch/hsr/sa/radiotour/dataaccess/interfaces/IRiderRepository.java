package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IRiderRepository {

    void addRider(Rider rider, OnSaveRiderCallback callback);

    void getAllRiders(OnGetAllRidersCallback callback);

    Rider getRiderByStartNr(int startNr);

    RealmList<Rider> getAllRidersReturned();

    RealmList<Rider> getAllActiveRidersReturned();

    RealmList<Rider> getAllUnknownRidersReturned();

    void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections, OnUpdateRiderStageCallback callback);

    void clearAllRiders();

    void removeRider(Rider rider, OnSaveRiderCallback callback);

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
}
