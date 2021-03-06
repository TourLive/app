package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRewardPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRepository;

public class RewardPresenter implements IRewardPresenter {
    private static RewardPresenter instance = null;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private RewardRepository rewardRepository = new RewardRepository();

    private IRewardRepository.OnSaveRewardCallback onSaveRewardCallback;

    public static RewardPresenter getInstance() {
        if (instance == null) {
            instance = new RewardPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }


    @Override
    public void subscribeCallbacks() {
        onSaveRewardCallback = new IRewardRepository.OnSaveRewardCallback() {
            @Override
            public void onSuccess() {
                for (Fragment frag : fragments) {
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
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
    public Reward getRewardReturnedByJudgment(Judgement judgement) {
        return rewardRepository.getRewardReturnedByJudgment(judgement);
    }

    @Override
    public void clearAllRewards() {
        rewardRepository.clearAllRewards();
    }

}
