package ch.hsr.sa.radiotour.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public final class AdapterUtilitis {
    private AdapterUtilitis() {

    }

    public static RealmList<Rider> removeUnknownRiders(RealmList<Rider> riders) {
        RealmList<Rider> sortedRiders = riders;
        Iterator<Rider> iterator = sortedRiders.iterator();
        while(iterator.hasNext()) {
            Rider r = iterator.next();
            if (r.isUnknown()) {
                iterator.remove();
            }
        }
        return sortedRiders;
    }

    public static List<String> listWithAllRidersForSpinner() {
        List<String> list = new ArrayList<>();
        for (Rider r : RiderPresenter.getInstance().getAllRidersReturned()) {
            if (!r.isUnknown()) {
                list.add("" + Integer.toString(r.getStartNr()) + "-" + r.getCountry() + "-" + r.getName());
            }
        }
        return list;
    }
}
