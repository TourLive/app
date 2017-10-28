package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;

public interface IRewardRiderConnectionRepository {

    interface OnSaveRewardRiderConnectionCallback {
        void onSuccess();
        void onError(String message);
    }

    void addRewardRiderConnection(RewardRiderConnection rewardRiderConnection, OnSaveRewardRiderConnectionCallback callback);

    void clearAllRewardRiderConnections();
}
