package ch.hsr.sa.radiotour.controller;

import java.util.Iterator;

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
}
