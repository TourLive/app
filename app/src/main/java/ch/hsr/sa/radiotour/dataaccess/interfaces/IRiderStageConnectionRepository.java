package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public interface IRiderStageConnectionRepository {
    void addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback);

    void getRiderStageConnections(OnGetAllRiderStageConnectionsCallback callback);

    void clearAllRiderStageConnection();

    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, final RiderStageConnection oldRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void updateRiderStageConnectionReward(final RiderStageConnection riderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void updateRiderState(final RiderStateType type, final Rider rider, OnUpdateRiderStateCallBack callback);

    void deleteRiderStageConnection();

    void calculateRanks(OnGetAllRiderStageConnectionsCallback callback);

    RiderStageConnection getRiderByRank(final int rank);

    RealmList<RiderStageConnection> getAllRiderStateConnections();

    void updateRiderStageConnectionRank(final int rank, RiderStageConnection connection);

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
        void onSuccess(RiderStageConnection riderStageConnection);

        void onError(String message);
    }
}
