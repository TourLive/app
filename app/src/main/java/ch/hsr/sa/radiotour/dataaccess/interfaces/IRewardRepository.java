package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Reward;

public interface IRewardRepository {

    interface OnSaveRewardCallback {
        void onSuccess();
        void onError(String message);
    }


    void addReward(Reward reward, OnSaveRewardCallback callback);

    void clearAllRewards();
}