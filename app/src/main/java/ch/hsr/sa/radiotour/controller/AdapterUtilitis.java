package ch.hsr.sa.radiotour.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public final class AdapterUtilitis {
    private AdapterUtilitis() {
        // To Hide public Constructor
    }

    public static RealmList<Rider> removeUnknownRiders(RealmList<Rider> riders) {
        RealmList<Rider> sortedRiders = riders;
        Iterator<Rider> iterator = sortedRiders.iterator();
        while (iterator.hasNext()) {
            Rider r = iterator.next();
            if (r.isUnknown()) {
                iterator.remove();
            }
        }
        return sortedRiders;
    }

    public static List<String> listWithAllRidersForSpinner() {
        List<String> list = new ArrayList<>();
        for (Rider r : RiderPresenter.getInstance().getAllActiveRidersReturned()) {
            if (!r.isUnknown()) {
                list.add("" + Integer.toString(r.getStartNr()) + "-" + r.getCountry() + "-" + r.getName());
            }
        }
        return list;
    }

    public static String longTimeToString(long time) {
        long days = TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time - days * 86400);
        long minutes = TimeUnit.SECONDS.toMinutes(time - hours * 3600 - days * 86400);
        long seconds = TimeUnit.SECONDS.toSeconds(time - minutes * 60 - hours * 3600 - days * 86400);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }
}
