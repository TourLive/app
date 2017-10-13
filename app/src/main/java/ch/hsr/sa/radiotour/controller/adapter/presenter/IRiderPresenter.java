package ch.hsr.sa.radiotour.controller.adapter.presenter;

import ch.hsr.sa.radiotour.dataaccess.Rider;

/**
 * Created by Dom on 12.10.2017.
 */

public interface IRiderPresenter extends IBasePresenter {
    void addRider(Rider student);
    void getAllRiders();
    void clearAllRiders();
}
