package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.Reward;

/**
 * Created by Urs Forrer on 20.11.2017.
 */

public final class RiderStageConnectionUtilities {

    private RiderStageConnectionUtilities() {
        // To Hide public Constructor
    }

    public static int getPointsAtPosition(int pos, Reward reward) {
        if (reward.getPoints().size() >= pos) {
            return reward.getPoints().get(pos - 1);
        } else {
            return 0;
        }
    }

    public static int getMoneyAtPosition(int pos, Reward reward) {
        if (reward.getMoney().size() >= pos) {
            return reward.getMoney().get(pos -1);
        } else {
            return 0;
        }
    }
}