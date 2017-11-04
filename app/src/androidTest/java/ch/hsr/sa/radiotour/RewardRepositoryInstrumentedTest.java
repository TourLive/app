package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRiderConnectionRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
public class RewardRepositoryInstrumentedTest {
    private Realm realm;
    private JudgmentRepository judgmentRepository;
    private RewardRepository rewardRepository;
    private RewardRiderConnectionRepository rewardRiderConnectionRepository;
    private IJudgmentRepository.OnSaveJudgmentCallback onSaveJudgmentCallback;
    private IRewardRepository.OnSaveRewardCallback onSaveRewardCallback;
    private IRewardRiderConnectionRepository.OnSaveRewardRiderConnectionCallback onSaveRewardRiderConnectionCallback;

    @Before
    public void initTestData() {
        this.judgmentRepository = new JudgmentRepository();
        this.rewardRepository = new RewardRepository();
        this.rewardRiderConnectionRepository = new RewardRiderConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Judgement.class).findAll().deleteAllFromRealm();
                realm.where(Reward.class).findAll().deleteAllFromRealm();
                realm.where(RewardRiderConnection.class).findAll().deleteAllFromRealm();
            }
        });

        RewardRiderConnection rewardRiderConnection = new RewardRiderConnection();
        rewardRiderConnection.setRank(1);

        synchronized (this){
            rewardRiderConnectionRepository.addRewardRiderConnection(rewardRiderConnection, onSaveRewardRiderConnectionCallback);
        }

        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
            judgement.setName("judgment2");
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }
    }

    private void initCallbacks() {
        onSaveJudgmentCallback = new IJudgmentRepository.OnSaveJudgmentCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };

        onSaveRewardCallback = new IRewardRepository.OnSaveRewardCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };

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
    public void addReward(){
        RealmList<Judgement> judgements = new RealmList<>();
        RealmResults<Judgement> realmJudgments = realm.where(Judgement.class).findAll();
        judgements.addAll(realmJudgments);

        RealmList<RewardRiderConnection> connections = new RealmList<>();
        RealmResults<RewardRiderConnection> realmConnections = realm.where(RewardRiderConnection.class).findAll();
        connections.addAll(realmConnections);

        Reward reward = new Reward();
        reward.setPoints(new RealmList<Integer>(1,3,5));
        reward.setMoney(new RealmList<Integer>(100, 300, 500));
        reward.setType(RewardType.POINTS);
        reward.setRewardId(93);
        reward.setRewardJudgements(judgements);
        reward.setRewardRiderConnections(connections);

        synchronized (this){
            rewardRepository.addReward(reward, onSaveRewardCallback);
        }

        Assert.assertEquals(93, realm.where(Reward.class).findAll().first().getRewardId().intValue());
        Assert.assertEquals(300, realm.where(Reward.class).findAll().first().getMoney().get(1).intValue());
        Assert.assertEquals(2, realm.where(Reward.class).findAll().first().getRewardJudgements().size());
        Assert.assertEquals(93, realm.where(Reward.class).findAll().first().getRewardJudgements().first().getRewardId());
        Assert.assertEquals(1, realm.where(Reward.class).findAll().first().getRewardRiderConnections().first().getRank());
    }

    @Test
    public void clearAllRewards(){
        Reward reward = new Reward();
        reward.setPoints(new RealmList<Integer>(1,3,5));
        reward.setMoney(new RealmList<Integer>(100, 300, 500));
        reward.setType(RewardType.POINTS);
        reward.setRewardId(93);

        synchronized (this){
            rewardRepository.addReward(reward, onSaveRewardCallback);
        }

        synchronized (this){
            rewardRepository.clearAllRewards();
        }

        Assert.assertEquals(0, realm.where(Reward.class).findAll().size());
    }

}
