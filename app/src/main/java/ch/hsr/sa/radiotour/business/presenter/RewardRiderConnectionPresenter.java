package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRewardRiderConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRiderConnectionRepository;

public class RewardRiderConnectionPresenter implements IRewardRiderConnectionPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static RewardRiderConnectionPresenter instance = null;
    private RewardRiderConnectionRepository rewardRiderConnectionRepository = new RewardRiderConnectionRepository();

    private IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback onSaveRewardRiderConnectionCallback;

    public static RewardRiderConnectionPresenter getInstance() {
        if(instance == null){
            instance = new RewardRiderConnectionPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRewardRiderConnectionCallback = new IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {

            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRewardRiderConnectionCallback = null;
    }

    @Override
    public void addRewardRiderConnection(RewardRiderConnection rewardRiderConnection) {
        rewardRiderConnectionRepository.addRewardRiderConnection(rewardRiderConnection, onSaveRewardRiderConnectionCallback);
    }

    @Override
    public void clearAllRewardRiderConnections() {
        rewardRiderConnectionRepository.clearAllRewardRiderConnections();
    }
}
