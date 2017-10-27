package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRewardRiderConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRiderConnectionRepository;

public class RewardRiderConnectionPresenter implements IRewardRiderConnectionPresenter {
    private IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback onSaveRewardRiderConnectionCallback;
    private IRewardRiderConnectionRepository rewardRiderConnectionRepository;

    public RewardRiderConnectionPresenter(){
        this.rewardRiderConnectionRepository = new RewardRiderConnectionRepository();
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRewardRiderConnectionCallback = new IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback() {
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
