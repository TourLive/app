package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.RealmList;

public interface IRiderStateRepository {
    interface OnSaveRiderStateCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllRiderStateCallback {
        void onSuccess(RealmList<RiderState> riderState);
        void onError(String message);
    }

    interface OnUpdateRiderStateCallBack {
        void onSuccess();
        void onError(String message);
    }

    void addRiderState(RiderState riderState, OnSaveRiderStateCallback callback);

    void getRiderState(OnGetAllRiderStateCallback callback);

    void clearAllRiderState();

    void updateRiderStateConnection(OnUpdateRiderStateCallBack riderStageConnection, final RealmList<RiderState> newRiderState, OnUpdateRiderStateCallBack callback);

    void deleteRiderState();
}
