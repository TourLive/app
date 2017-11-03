package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import io.realm.Realm;

@RunWith(AndroidJUnit4.class)
public class RewardRiderConnectionInstrumentedTest {
    private Realm realm;
    private RewardRiderConnectionRepository rewardRiderConnectionRepository;
    private IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback onSaveRewardRiderConnectionCallback;

    @Before
    public void initTestData() {
        this.rewardRiderConnectionRepository = new RewardRiderConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RewardRiderConnection.class).findAll().deleteAllFromRealm();
            }
        });

    }

    private void initCallbacks() {
        onSaveRewardRiderConnectionCallback = new IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
    }

    private void successFullyAdded() {
        notifyAll();
    }

    @Test
    public void addRewardRiderConnection(){

        RewardRiderConnection rewardRiderConnection = new RewardRiderConnection();
        rewardRiderConnection.setRank(1);

        synchronized (this){
            rewardRiderConnectionRepository.addRewardRiderConnection(rewardRiderConnection, onSaveRewardRiderConnectionCallback);
        }

        Assert.assertEquals(1, realm.where(RewardRiderConnection.class).findAll().first().getRank());
    }

    @Test
    public void clearAllRewardRiderConnections(){
        RewardRiderConnection rewardRiderConnection = new RewardRiderConnection();
        rewardRiderConnection.setRank(1);

        synchronized (this){
            rewardRiderConnectionRepository.addRewardRiderConnection(rewardRiderConnection, onSaveRewardRiderConnectionCallback);
        }

        synchronized (this){
            rewardRiderConnectionRepository.clearAllRewardRiderConnections();
        }

        Assert.assertEquals(0, realm.where(RewardRiderConnection.class).findAll().size());
    }
}
