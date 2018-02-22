package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;


public class RiderStageConnectionComparatorMountainPoints implements Comparator<RiderStageConnection> {
    @Override
    public int compare(RiderStageConnection r1, RiderStageConnection r2) {
        return Long.compare(r1.getMountainBonusPoints(), r2.getMountainBonusPoints());
    }
}
