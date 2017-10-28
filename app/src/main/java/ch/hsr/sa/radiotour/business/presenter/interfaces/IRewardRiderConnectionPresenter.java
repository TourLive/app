package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;

public interface IRewardRiderConnectionPresenter extends IBasePresenter {
    void addRewardRiderConnection(RewardRiderConnection rewardRiderConnection);
    void clearAllRewardRiderConnections();
}
