package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public interface IRiderPresenter extends IBasePresenter {
    void addRider(Rider student);
    void getAllRiders();
    void clearAllRiders();
}
