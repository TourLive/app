package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;

/**
 * Created by Urs Forrer on 09.11.2017.
 */

public final class VirtualClassementComparators {
    private VirtualClassementComparators() {
        // no instance
    }

    public static Comparator<RiderExtended> getStartNrComparator() {
        return new RiderExtendedStartNrComparator();
    }
    public static Comparator<RiderExtended> getCountryComparator() {
        return new RiderExtendedCountryComparator();
    }
    public static Comparator<RiderExtended> getNameComparator() {
        return new RiderExtendedNameComparator();
    }
    public static Comparator<RiderExtended>  getTeamComparator() {
        return new RiderExtendedTeamComparator();
    }
    public static Comparator<RiderExtended> getPointComparator() {
        return new RiderExtendedPointComparator();
    }
    public static Comparator<RiderExtended> getMoneyComparator() {
        return new RiderExtendedMoneyComparator();
    }
    public static Comparator<RiderExtended> getVirtualDeficitComparator() {
        return new RiderExtendedVirtualDeficitComparator();
    }
    public static Comparator<RiderExtended> getOffizialDeficitComparator() {
        return new RiderExtendedOffizialDeficitComparator();
    }
    public static Comparator<RiderExtended> getOfficialTimeComparator() {
        return new RiderExtendedOfficialTimeComparator();
    }

    private static class RiderExtendedStartNrComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getStartNr() - riderTwo.getStartNr();
        }
    }

    private static class RiderExtendedCountryComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getCountry().compareTo(riderTwo.getCountry());
        }
    }

    private static class RiderExtendedNameComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getName().compareTo(riderTwo.getName());
        }
    }
    private static class RiderExtendedTeamComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getTeamName().compareTo(riderTwo.getTeamName());
        }
    }

    private static class RiderExtendedPointComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getBonusPoint() - riderTwo.getBonusPoint();
        }
    }

    private static class RiderExtendedMoneyComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getMoney() - riderTwo.getMoney();
        }
    }

    private static class RiderExtendedVirtualDeficitComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getVirtualGap().compareTo(riderTwo.getVirtualGap());
        }
    }

    private static class RiderExtendedOffizialDeficitComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getOfficialGap().compareTo(riderTwo.getOfficialGap());
        }
    }

    private static class RiderExtendedOfficialTimeComparator implements Comparator<RiderExtended> {
        @Override
        public int compare(final RiderExtended riderOne, final RiderExtended riderTwo) {
            return riderOne.getOfficialTime().compareTo(riderTwo.getOfficialTime());
        }
    }

}
