package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;

public class RiderStageConnectionComparatorMoney  implements Comparator<RiderStageConnection> {
    @Override
    public int compare(RiderStageConnection r1, RiderStageConnection r2) {
        return Long.compare(r2.getMoney(), r1.getMoney());
    }
}