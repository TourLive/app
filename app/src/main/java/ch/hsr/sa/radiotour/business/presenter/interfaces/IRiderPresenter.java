package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IRiderPresenter extends IBasePresenter {
    void addRiderNone(Rider rider);

    void getAllRiders();

    RealmList<Rider> getAllRidersReturned();

    RealmList<Rider> getAllActiveRidersReturned();

    Rider getRiderByStartNr(int startNr);

    RealmList<Rider> getAllUnknownRidersReturned();

    void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections);

    void clearAllRiders();

    void removeRiderWithoutCallback(Rider rider);
}
