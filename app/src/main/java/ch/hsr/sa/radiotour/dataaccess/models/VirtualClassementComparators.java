package ch.hsr.sa.radiotour.dataaccess.models;

import net.gotev.recycleradapter.AdapterItem;

import java.util.Comparator;

import ch.hsr.sa.radiotour.presentation.models.VirtualClassementRider;

/**
 * Created by Urs Forrer on 09.11.2017.
 */

public final class VirtualClassementComparators {
    private VirtualClassementComparators() {
        // no instance
    }

    public static Comparator<AdapterItem> getStartNrComparator() {
        return new VirtualClassementRiderStartNrComparator();
    }

    public static Comparator<AdapterItem> getCountryComparator() {
        return new VirtualClassementRiderCountryComparator();
    }

    public static Comparator<AdapterItem> getNameComparator() {
        return new VirtualClassementRiderNameComparator();
    }

    public static Comparator<AdapterItem> getTeamComparator() {
        return new VirtualClassementRiderTeamComparator();
    }

    public static Comparator<AdapterItem> getPointComparator() {
        return new VirtualClassementRiderPointComparator();
    }

    public static Comparator<AdapterItem> getSprintPointComparator() {
        return new VirtualClassementRiderSprintPointComparator();
    }

    public static Comparator<AdapterItem> getMountainPointComparator() {
        return new VirtualClassementRiderMountainPointComparator();
    }

    public static Comparator<AdapterItem> getMoneyComparator() {
        return new VirtualClassementRiderMoneyComparator();
    }

    public static Comparator<AdapterItem> getVirtualDeficitComparator() {
        return new VirtualClassementRiderVirtualDeficitComparator();
    }

    public static Comparator<AdapterItem> getOffizialDeficitComparator() {
        return new VirtualClassementRiderOffizialDeficitComparator();
    }

    public static Comparator<AdapterItem> getOfficialTimeComparator() {
        return new VirtualClassementRiderOfficialTimeComparator();
    }

    public static Comparator<AdapterItem> getTimeInLeadGroupComparator() {
        return new VirtualClassementRiderTimeInLeadGroupComparator();
    }

    public static Comparator<AdapterItem> getDistanceInLeadGroupComparator() {
        return new VirtualClassementRiderTimeInLeadGroupComparator();
    }

    private static class VirtualClassementRiderStartNrComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStartNr() - riderTwo.getRiderStartNr();
            }
            return 0;
        }
    }


    private static class VirtualClassementRiderCountryComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderCountry().compareTo(riderTwo.getRiderCountry());
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderNameComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderName().compareTo(riderTwo.getRiderName());
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderTeamComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderTeam().compareTo(riderTwo.getRiderTeam());
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderPointComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStageConnection().getRiderRanking(RankingType.POINTS).getRank() - riderTwo.getRiderStageConnection().getRiderRanking(RankingType.POINTS).getRank();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderSprintPointComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderTwo.getRiderSprintPoints() - riderOne.getRiderSprintPoints();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderMountainPointComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStageConnection().getRiderRanking(RankingType.MOUNTAIN).getRank() - riderTwo.getRiderStageConnection().getRiderRanking(RankingType.MOUNTAIN).getRank();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderMoneyComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStageConnection().getRiderRanking(RankingType.MONEY).getRank() - riderTwo.getRiderStageConnection().getRiderRanking(RankingType.MONEY).getRank();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderTimeInLeadGroupComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStageConnection().getRiderRanking(RankingType.TIME_IN_LEAD).getRank() - riderTwo.getRiderStageConnection().getRiderRanking(RankingType.TIME_IN_LEAD).getRank();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderDistanceInLeadGroupComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return riderOne.getRiderStageConnection().getRiderRanking(RankingType.DISTANCE_IN_LEAD).getRank() - riderTwo.getRiderStageConnection().getRiderRanking(RankingType.DISTANCE_IN_LEAD).getRank();
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderVirtualDeficitComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return Long.compare(riderOne.getRiderVirtualGap(), riderTwo.getRiderVirtualGap());
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderOffizialDeficitComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return Long.compare(riderOne.getRiderOfficalGap(), riderTwo.getRiderOfficalGap());
            }
            return 0;
        }
    }

    private static class VirtualClassementRiderOfficialTimeComparator implements Comparator<AdapterItem> {
        @Override
        public int compare(AdapterItem itemOne, AdapterItem itemTwo) {
            if (itemOne.getClass() == VirtualClassementRider.class && itemTwo.getClass() == VirtualClassementRider.class) {
                VirtualClassementRider riderOne = (VirtualClassementRider) itemOne;
                VirtualClassementRider riderTwo = (VirtualClassementRider) itemTwo;
                return Long.compare(riderOne.getRiderOfficalTime(), riderTwo.getRiderOfficalTime());
            }
            return 0;
        }
    }

}
