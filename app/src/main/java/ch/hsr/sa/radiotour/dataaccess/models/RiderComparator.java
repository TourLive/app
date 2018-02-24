package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;


public class RiderComparator implements Comparator<Rider> {
    @Override
    public int compare(Rider r1, Rider r2) {
        return Integer.compare(r1.getStartNr(), r2.getStartNr());
    }
}
