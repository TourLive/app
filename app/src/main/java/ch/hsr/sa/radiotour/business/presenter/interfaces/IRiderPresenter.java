package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IRiderPresenter extends IBasePresenter {
    void addRider(Rider rider);
    void getAllRiders();
    RealmList<Rider> getAllRidersReturned();
    void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections);
    void clearAllRiders();
}
