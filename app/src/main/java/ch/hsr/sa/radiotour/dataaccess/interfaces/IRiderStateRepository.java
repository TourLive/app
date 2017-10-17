package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.RealmList;

public interface IRiderStateRepository {
    interface OnSaveRiderStateCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetRiderStateCallback {
        void onSuccess(RiderState riderState);
        void onError(String message);
    }

    interface OnUpdateRiderStateCallBack {
        void onSuccess();
        void onError(String message);
    }

    void addRiderState(RiderState riderState, OnSaveRiderStateCallback callback);

    void getRiderState(Rider rider, OnGetRiderStateCallback callback);

    void clearAllRiderState();

    void updateRiderState(final RiderState newRiderState, OnUpdateRiderStateCallBack callback);

    void deleteRiderState();
}
