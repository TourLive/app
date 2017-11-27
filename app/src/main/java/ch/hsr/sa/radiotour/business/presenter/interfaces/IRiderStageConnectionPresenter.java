package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public interface IRiderStageConnectionPresenter extends IBasePresenter {
    void addRiderStageConnection(RiderStageConnection riderStageConnection);
    void getRiderStageConnections();
    void calculateRanks();
    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, RiderStageConnection oldRiderStageConnection);
    void updateRiderStageConnectionReward(final RiderStageConnection riderStageConnection);
    void deleteRiderStageConnection();
    void updateRiderState(final RiderStateType type, final Rider rider);
    void clearAllRiderStageConnection();
    RiderStageConnection getRiderByRank(final int rank);
    RealmList<RiderStageConnection> getAllRiderStateConnections();
    void updateRiderStageConnectionRank(final int rank, final RiderStageConnection connection);
}
