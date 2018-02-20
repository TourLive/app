package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;


public class RaceGroupComparator implements Comparator<RaceGroup> {
    @Override
    public int compare(RaceGroup raceGroup, RaceGroup t1) {
        int left = raceGroup.getPosition();
        int right = t1.getPosition();

        return Integer.compare(left, right);
    }
}
