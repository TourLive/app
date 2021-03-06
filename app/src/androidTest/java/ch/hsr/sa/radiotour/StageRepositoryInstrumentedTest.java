package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Random;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IStageRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.models.StageType;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.StageRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
public class StageRepositoryInstrumentedTest {
    private Realm realm;
    private StageRepository stageRepository;
    private RiderStageConnectionRepository riderStageConnectionRepository;
    private IStageRepository.OnSaveStageCallback onSaveStageCallback;
    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallback;

    @Before
    public void initTestData() {
        this.stageRepository = new StageRepository();
        this.riderStageConnectionRepository = new RiderStageConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(realm -> {
            realm.where(Stage.class).findAll().deleteAllFromRealm();
            realm.where(RiderStageConnection.class).findAll().deleteAllFromRealm();
        });
    }

    private void initCallbacks() {
        onSaveStageCallback = new IStageRepository.OnSaveStageCallback() {
            @Override
            public void onSuccess(Stage stage) {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };

        onSaveRiderStageConnectionCallback = new IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback() {
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
    public void addStage(){
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setId(new Random().nextLong());
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setOfficialGap(100);
        riderStageConnection.setOfficialTime(100);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(100);

        RiderStageConnection riderStageConnectionTwo = new RiderStageConnection();
        riderStageConnectionTwo.setId(new Random().nextLong());
        riderStageConnectionTwo.setBonusPoint(10);
        riderStageConnectionTwo.setBonusTime(20);
        riderStageConnectionTwo.setOfficialGap(100);
        riderStageConnectionTwo.setOfficialTime(100);
        riderStageConnectionTwo.setType(RiderStateType.DNC);
        riderStageConnectionTwo.setVirtualGap(100);

        synchronized (this){
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
            riderStageConnection.setBonusPoint(100);
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnectionTwo, onSaveRiderStageConnectionCallback);
        }

        RealmList<RiderStageConnection> connections = new RealmList<>();
        RealmResults<RiderStageConnection> realmConnections = realm.where(RiderStageConnection.class).findAll();
        connections.addAll(realmConnections);

        Stage stage = new Stage();
        stage.setId(1);
        stage.setDistance(100);
        stage.setStartTime(new Date());
        stage.setEndTime(new Date());
        stage.setFrom("bern");
        stage.setTo("zuerich");
        stage.setName("Etappe 1");
        stage.setType(StageType.FLATSTAGE);
        stage.setStageConnections(connections);

        synchronized (this){
            stageRepository.addStage(stage, onSaveStageCallback);
        }

        Assert.assertEquals(100, realm.where(Stage.class).findAll().first().getDistance());
        Assert.assertEquals("bern", realm.where(Stage.class).findAll().first().getFrom());
        Assert.assertEquals("Etappe 1", realm.where(Stage.class).findAll().first().getName());
        Assert.assertEquals(2, realm.where(Stage.class).findAll().first().getStageConnections().size());
        Assert.assertEquals(20, realm.where(Stage.class).findAll().first().getStageConnections().first().getBonusTime());
    }

    @Test
    public void clearAllStages(){
        Stage stage = new Stage();
        stage.setId(new Random().nextLong());
        stage.setDistance(100);
        stage.setStartTime(new Date());
        stage.setEndTime(new Date());
        stage.setFrom("bern");
        stage.setTo("zuerich");
        stage.setName("Ettape 1");
        stage.setType(StageType.FLATSTAGE);

        Stage stageTwo = new Stage();
        stageTwo.setId(new Random().nextLong());
        stageTwo.setDistance(100);
        stageTwo.setStartTime(new Date());
        stageTwo.setEndTime(new Date());
        stageTwo.setFrom("bern");
        stageTwo.setTo("zuerich");
        stageTwo.setName("Ettape 1");
        stageTwo.setType(StageType.FLATSTAGE);

        synchronized (this){
            stageRepository.addStage(stage, onSaveStageCallback);
            stage.setDistance(200);
            stageRepository.addStage(stageTwo, onSaveStageCallback);
        }

        synchronized (this){
            stageRepository.clearAllStages();
        }

        Assert.assertEquals(0, realm.where(Stage.class).findAll().size());
    }

}
