package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public interface IRiderStageConnectionPresenter extends IBasePresenter {
    RiderStageConnection addRiderStageConnection(RiderStageConnection riderStageConnection);

    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, RiderStageConnection oldRiderStageConnection);

    void updateRiderStageConnectionReward(final RiderStageConnection riderStageConnection);

    void updateRiderState(final RiderStateType type, final Rider rider);

    void clearAllRiderStageConnection();

    RealmList<RiderStageConnection> getAllRiderStateConnections();

    void updateRiderStageConnectionRanking(final RiderRanking riderRanking, final RiderStageConnection connection);

    void updateRiderStageConnectionTime(long timeBefore, long timeStamp, RaceGroup res);

    void appendTimeInLeadGroup(RiderStageConnection riderStageConnection, int value);
}
