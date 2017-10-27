package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;

public interface IRewardRankingConnectionRepository {

    void addRewardRiderConnection(RewardRiderConnection rewardRiderConnection);

    void clearAllRewardRiderConnections();
}
