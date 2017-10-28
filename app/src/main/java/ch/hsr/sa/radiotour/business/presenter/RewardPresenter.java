package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRewardPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRepository;

public class RewardPresenter implements IRewardPresenter {
    private IRewardRepository.OnSaveRewardCallback onSaveRewardCallback;
    private IRewardRepository rewardRepository;

    public RewardPresenter(){
        this.rewardRepository = new RewardRepository();
    }


    @Override
    public void subscribeCallbacks() {
        onSaveRewardCallback = new IRewardRepository.OnSaveRewardCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {

            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRewardCallback = null;
    }

    @Override
    public void addReward(Reward reward) {
        rewardRepository.addReward(reward, onSaveRewardCallback);
    }

    @Override
    public void clearAllRewards() {
        rewardRepository.clearAllRewards();
    }
}
