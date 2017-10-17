package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public interface IRiderPresenter extends IBasePresenter {
    void addRider(Rider student);
    void getAllRiders();
    RealmList<Rider> getAllRidersReturned();
    void clearAllRiders();
}
