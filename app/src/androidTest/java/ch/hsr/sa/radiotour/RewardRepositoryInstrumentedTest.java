package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RewardRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
public class RewardRepositoryInstrumentedTest {
    private Realm realm;
    private JudgmentRepository judgmentRepository;
    private RewardRepository rewardRepository;
    private IJudgmentRepository.OnSaveJudgmentCallback onSaveJudgmentCallback;
    private IRewardRepository.OnSaveRewardCallback onSaveRewardCallback;

    @Before
    public void initTestData() {
        this.judgmentRepository = new JudgmentRepository();
        this.rewardRepository = new RewardRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(realm -> {
            realm.where(Judgement.class).findAll().deleteAllFromRealm();
            realm.where(Reward.class).findAll().deleteAllFromRealm();
        });

        Judgement judgement = new Judgement();
        judgement.setId(new Random().nextLong());
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        Judgement judgementTwo = new Judgement();
        judgementTwo.setId(new Random().nextLong());
        judgementTwo.setDistance(100);
        judgementTwo.setName("judgmentTwo");
        judgementTwo.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
            judgmentRepository.addJudgment(judgementTwo, onSaveJudgmentCallback);
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
    }

    private void successFullyAdded() {
        notifyAll();
    }

    @Test
    public void addReward(){
        RealmList<Judgement> judgements = new RealmList<>();
        RealmResults<Judgement> realmJudgments = realm.where(Judgement.class).findAll();
        judgements.addAll(realmJudgments);

        long id = new Random().nextLong();
        Reward reward = new Reward();
        reward.setId(id);
        reward.setPoints(new RealmList<Integer>(1,3,5));
        reward.setMoney(new RealmList<Integer>(100, 300, 500));
        reward.setType(RewardType.POINTS);
        reward.setRewardJudgements(judgements);

        synchronized (this){
            rewardRepository.addReward(reward, onSaveRewardCallback);
        }

        Assert.assertEquals(id, realm.where(Reward.class).findAll().first().getId());
        Assert.assertEquals(300, realm.where(Reward.class).findAll().first().getMoney().get(1).intValue());
        Assert.assertEquals(2, realm.where(Reward.class).findAll().first().getRewardJudgements().size());
    }

    @Test
    public void clearAllRewards(){
        Reward reward = new Reward();
        long id = new Random().nextLong();
        reward.setId(id);
        reward.setPoints(new RealmList<Integer>(1,3,5));
        reward.setMoney(new RealmList<Integer>(100, 300, 500));
        reward.setType(RewardType.POINTS);

        synchronized (this){
            rewardRepository.addReward(reward, onSaveRewardCallback);
        }

        synchronized (this){
            rewardRepository.clearAllRewards();
        }

        Assert.assertEquals(0, realm.where(Reward.class).findAll().size());
    }

}
