package ch.hsr.sa.radiotour.business;

import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public final class Context {

    public static void addRider(Rider rider){
        RiderPresenter.getInstance().addRider(rider);
    }
}
