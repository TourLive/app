package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
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

    void addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback);

    void getRiderStageConnections(OnGetAllRiderStageConnectionsCallback callback);

    void clearRiderStageConnection();

    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void deleteRiderStageConnection();
}
