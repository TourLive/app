package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

@RunWith(AndroidJUnit4.class)
public class JudgmentRepositoryInstrumentedTest {
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
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Judgement.class).findAll().deleteAllFromRealm();
                realm.where(Reward.class).findAll().deleteAllFromRealm();
            }
        });
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
    public void addJudgment(){
        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }

        Judgement realmJudgment = realm.where(Judgement.class).findAll().first();

        Assert.assertEquals(judgement.getDistance(), realmJudgment.getDistance());
        Assert.assertEquals(judgement.getRewardId(), realmJudgment.getRewardId());
        Assert.assertEquals(judgement.getName(), realmJudgment.getName());
    }

    @Test
    public void getJudgmentById(){
        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }

        Judgement realmJudgment;
        synchronized (this){
            realmJudgment = judgmentRepository.getJudgmentsById(93).first();
        }

        Assert.assertEquals(judgement.getDistance(), realmJudgment.getDistance());
        Assert.assertEquals(judgement.getRewardId(), realmJudgment.getRewardId());
        Assert.assertEquals(judgement.getName(), realmJudgment.getName());
    }

    @Test
    public void clearAllJudgments(){
        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
            judgement.setName("judgment2");
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }

        synchronized (this){
            judgmentRepository.clearAllJudgments();
        }

        Assert.assertEquals(0, realm.where(Judgement.class).findAll().size());
    }

    @Test
    public void checkJudgmentRewardConnection(){
        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this) {
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }

        Reward reward = new Reward();
        reward.setPoints(new RealmList<Integer>(1, 3, 5));
        reward.setMoney(new RealmList<Integer>(100, 300, 500));
        reward.setType(RewardType.POINTS);
        reward.setRewardJudgements(judgmentRepository.getJudgmentsById(93));

        synchronized (this){
            rewardRepository.addReward(reward, onSaveRewardCallback);
        }

        Assert.assertEquals(300, judgmentRepository.getJudgmentsById(93).first().getRewards().getMoney().get(1).intValue());
    }

}
