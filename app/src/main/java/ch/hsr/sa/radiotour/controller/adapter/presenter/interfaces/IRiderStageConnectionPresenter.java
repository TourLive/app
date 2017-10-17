package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;

public interface IRiderStageConnectionPresenter extends IBasePresenter {
    void addRiderStageConnection(RiderStageConnection riderStageConnection);
    void getRiderStageConnections();
    void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection);
    void deleteRiderStageConnection();
}
