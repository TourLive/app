package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;

public interface IRewardRepository {

    void addReward(Reward reward, OnSaveRewardCallback callback);

    Reward getRewardReturnedByJudgment(Judgement judgement);

    void clearAllRewards();

    interface OnSaveRewardCallback {
        void onSuccess();

        void onError(String message);
    }
}
