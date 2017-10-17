package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.RealmList;

public interface IRiderStageConnectionRepository {
    interface OnSaveRiderStageConnectionCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllRiderStageConnectionsCallback {
        void onSuccess(RealmList<RiderStageConnection> riderStageConnections);
        void onError(String message);
    }

    interface OnUpdateRiderStageConnectionCallBack {
        void onSuccess();
        void onError(String message);
    }

    interface OnUpdateRiderStateCallBack {
        void onSuccess();
        void onError(String message);
    }

    void addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback);

    void getRiderStageConnections(OnGetAllRiderStageConnectionsCallback callback);

    void clearAllRiderStageConnection();

    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void updateRiderState(final RiderState newRiderState, final Rider rider, OnUpdateRiderStateCallBack callback);

    void deleteRiderStageConnection();
}
