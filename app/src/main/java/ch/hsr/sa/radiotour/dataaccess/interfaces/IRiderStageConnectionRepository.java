package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public interface IRiderStageConnectionRepository {
    RiderStageConnection addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback);

    void getRiderStageConnections(OnGetAllRiderStageConnectionsCallback callback);

    void clearAllRiderStageConnection();

    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, final RiderStageConnection oldRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void updateRiderStageConnectionReward(final RiderStageConnection riderStageConnection, OnUpdateRiderStageConnectionCallBack callback);

    void updateRiderState(final RiderStateType type, final Rider rider, OnUpdateRiderStateCallBack callback);

    RealmList<RiderStageConnection> getAllRiderStateConnections();

    void updateRiderStageConnectionRanking(final RiderRanking riderRanking, RiderStageConnection connection);

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

    void updateRiderStageConnectionTime(long timeBefore, long timeStamp, final RaceGroup res, OnUpdateRiderStageConnectionCallBack callback);


    void appendTimeInLeadGroup(final RiderStageConnection riderStageConnection, int value);
}
