package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Reward;

public interface IRewardRepository {

    void addReward(Reward reward);

    void clearAllRewards();
}
