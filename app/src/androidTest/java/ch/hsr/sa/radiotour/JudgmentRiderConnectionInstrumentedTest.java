package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.PostHandler;
import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import io.realm.Realm;
import io.realm.RealmList;

@RunWith(AndroidJUnit4.class)
public class JudgmentRiderConnectionInstrumentedTest {
    private Realm realm;
    private JudgmentRiderConnectionRepository judgmentRiderConnectionRepository;
    private IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback onSaveJudgmentRiderConnectionCallback;
    private RiderRepository riderRepository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IJudgmentRepository.OnSaveJudgmentCallback onSaveJudgmentCallback;
    private JudgmentRepository judgmentRepository;

    @Before
    public void initTestData() {
        this.judgmentRiderConnectionRepository = new JudgmentRiderConnectionRepository();
        this.riderRepository = new RiderRepository();
        this.judgmentRepository = new JudgmentRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction((Realm db) ->  {
            db.where(JudgmentRiderConnection.class).findAll().deleteAllFromRealm();
            db.where(Rider.class).findAll().deleteAllFromRealm();
            db.where(Judgement.class).findAll().deleteAllFromRealm();
        });
        PostHandler postHandler = new PostHandler();
        postHandler.start();
        APIClient.setDemoMode(true);
    }

    private void initCallbacks() {
        onSaveJudgmentRiderConnectionCallback = new IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {

            }
        };
        onSaveJudgmentCallback = new IJudgmentRepository.OnSaveJudgmentCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
    }

    private void addRider() {
        Rider rider = new Rider();
        rider.setId(new Random().nextLong());
        rider.setCountry("swiss");
        rider.setName("testrider");
        rider.setStartNr(15);
        rider.setTeamName("Swiss");
        rider.setTeamShortName("CH");
        synchronized (this){
            riderRepository.addRider(rider, onSaveRiderCallback);
        }
    }

    private void addJudgment() {
        Judgement judgement = new Judgement();
        judgement.setDistance(100);
        judgement.setName("judgment");
        judgement.setRewardId(93);

        synchronized (this){
            judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
        }
    }

    private void successFullyAdded() {
        notifyAll();
    }

    @Test
    public void addRewardRiderConnection(){
        addRider();
        addJudgment();
        RealmList<Rider> riderList = new RealmList<>();
        RealmList<Judgement> judgementList = new RealmList<>();
        riderList.add(RiderPresenter.getInstance().getRiderByStartNr(15));
        judgementList.add(JudgmentPresenter.getInstance().getJudgmentsByRewardId(93).first());

        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
        judgmentRiderConnection.setRank(1);
        judgmentRiderConnection.setJudgements(judgementList);
        judgmentRiderConnection.setRider(riderList);

        synchronized (this){
            judgmentRiderConnectionRepository.addJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
        }

        Assert.assertEquals(1, realm.where(JudgmentRiderConnection.class).findAll().first().getRank());
    }

    @Test
    public void clearAllRewardRiderConnections(){
        addRider();
        addJudgment();
        RealmList<Rider> riderList = new RealmList<>();
        RealmList<Judgement> judgementList = new RealmList<>();
        riderList.add(RiderPresenter.getInstance().getRiderByStartNr(15));
        judgementList.add(JudgmentPresenter.getInstance().getJudgmentsByRewardId(93).first());

        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
        judgmentRiderConnection.setRank(1);
        judgmentRiderConnection.setJudgements(judgementList);
        judgmentRiderConnection.setRider(riderList);

        synchronized (this){
            judgmentRiderConnectionRepository.addJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
        }

        synchronized (this){
            judgmentRiderConnectionRepository.clearAllJudgmentRiderConnections();
        }

        Assert.assertEquals(0, realm.where(JudgmentRiderConnection.class).findAll().size());
    }
}
