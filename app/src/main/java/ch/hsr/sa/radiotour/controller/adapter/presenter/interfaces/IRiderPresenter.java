package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IRiderPresenter extends IBasePresenter {
    void addRider(Rider student);
    void getAllRiders();
    RealmList<Rider> getAllRidersReturned();
    void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections);
    void clearAllRiders();
}
