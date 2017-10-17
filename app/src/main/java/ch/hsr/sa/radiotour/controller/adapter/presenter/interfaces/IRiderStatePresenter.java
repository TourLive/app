package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;

public interface IRiderStatePresenter extends IBasePresenter {
    void addRiderState(RiderState riderState);
    void getRiderState(Rider rider);
    void clearAllRiderState();
    void getAllRiderStates();
    void updateRiderState(final RiderState newRiderState);
    void deleteRiderState();
}
