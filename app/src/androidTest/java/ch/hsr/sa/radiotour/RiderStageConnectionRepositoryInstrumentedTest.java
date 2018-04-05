package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStageConnectionRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RiderStageConnectionRepositoryInstrumentedTest {
    private Realm realm;
    private RiderStageConnectionRepository riderStageConnectionRepository;
    private RiderRepository riderRepository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallback;
    private IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback onGetAllRiderStageConnectionsCallback;
    private IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack onUpdateRiderStageConnectionCallBack;
    private IRiderStageConnectionRepository.OnUpdateRiderStateCallBack onUpdateRiderStateCallBack;
    private IRiderRepository.OnUpdateRiderStageCallback onUpdateRiderStageCallback;

    private RealmList<RiderStageConnection> riderStageConnections;

    @Before
    public void initTestData() {
        this.riderRepository = new RiderRepository();
        this.riderStageConnectionRepository = new RiderStageConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Rider.class).findAll().deleteAllFromRealm();
                realm.where(RiderStageConnection.class).findAll().deleteAllFromRealm();
            }
        });
    }


    private void initCallbacks() {
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
        onGetAllRiderStageConnectionsCallback = new IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback() {
            @Override
            public void onSuccess(RealmList<RiderStageConnection> riderStageConnections) {
                setAllNewConnections(riderStageConnections);
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
        onUpdateRiderStageConnectionCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {

            }
        };
        onUpdateRiderStateCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStateCallBack() {
            @Override
            public void onSuccess(RiderStageConnection riderStageConnection) {

            }

            @Override
            public void onError(String message) {

            }
        };
        onUpdateRiderStageCallback = new IRiderRepository.OnUpdateRiderStageCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {

            }
        };

    }

    private void setAllNewConnections(RealmList<RiderStageConnection> riderStageConnections) {
        this.riderStageConnections = riderStageConnections;
        notifyAll();
    }

    private void successFullyAdded() {
        notifyAll();
    }

    @Test
    public void addRiderStageConnection(){
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setSprintBonusPoints(100);
        riderStageConnection.setMountainBonusPoints(200);
        riderStageConnection.setOfficialGap(100);
        riderStageConnection.setOfficialTime(100);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(100);

        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
        }

        RealmResults<RiderStageConnection> res = realm.where(RiderStageConnection.class).findAll();

        assertEquals(1, res.size());
        assertEquals(100, res.get(0).getOfficialGap());
        assertEquals(100, res.get(0).getVirtualGap());
        assertEquals(100, res.get(0).getOfficialTime());
        assertEquals(10, res.get(0).getBonusPoint());
        assertEquals(20, res.get(0).getBonusTime());
        assertEquals(100, res.get(0).getSprintBonusPoints());
        assertEquals(200, res.get(0).getMountainBonusPoints());
        assertEquals(RiderStateType.DNC, res.get(0).getType());

    }
    private void initalData() {
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setSprintBonusPoints(100);
        riderStageConnection.setMountainBonusPoints(200);
        riderStageConnection.setOfficialGap(100);
        riderStageConnection.setOfficialTime(100);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(100);

        RiderStageConnection riderStageConnectionTwo = new RiderStageConnection();
        riderStageConnectionTwo.setBonusPoint(20);
        riderStageConnectionTwo.setSprintBonusPoints(300);
        riderStageConnectionTwo.setMountainBonusPoints(400);
        riderStageConnectionTwo.setBonusTime(40);
        riderStageConnectionTwo.setOfficialGap(100);
        riderStageConnectionTwo.setOfficialTime(100);
        riderStageConnectionTwo.setType(RiderStateType.DOCTOR);
        riderStageConnectionTwo.setVirtualGap(100);


        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnectionTwo, onSaveRiderStageConnectionCallback);
        }
    }

    @Test
    public void getAllRiderStageConnections(){
        initalData();

        synchronized (this) {
            riderStageConnectionRepository.getRiderStageConnections(onGetAllRiderStageConnectionsCallback);
        }

        RealmResults<RiderStageConnection> res = realm.where(RiderStageConnection.class).findAll();

        assertEquals(2, riderStageConnections.size());
    }

    @Test
    public void updateRiderStageConnection() {
        initalData();
        String id;

        RiderStageConnection res = realm.where(RiderStageConnection.class).findAll().first();
        id = res.getId();
        RiderStageConnection newStageConnection = new RiderStageConnection();
        newStageConnection.setType(RiderStateType.ACTIVE);
        newStageConnection.setBonusPoint(100);
        newStageConnection.setBonusTime(2020);
        newStageConnection.setMountainBonusPoints(4000);
        newStageConnection.setSprintBonusPoints(5000);
        newStageConnection.setVirtualGap(res.getVirtualGap());
        newStageConnection.setOfficialTime(res.getOfficialTime());
        newStageConnection.setOfficialGap(res.getOfficialGap());

        synchronized (this) {
            riderStageConnectionRepository.updateRiderStageConnection(newStageConnection, res, onUpdateRiderStageConnectionCallBack);
        }

        RiderStageConnection endRes = realm.where(RiderStageConnection.class).equalTo("id", id).findAll().first();

        assertEquals(100, endRes.getOfficialGap());
        assertEquals(100, endRes.getVirtualGap());
        assertEquals(100, endRes.getOfficialTime());
        assertEquals(100, endRes.getBonusPoint());
        assertEquals(2020, endRes.getBonusTime());
        assertEquals(4000, endRes.getMountainBonusPoints());
        assertEquals(5000, endRes.getSprintBonusPoints());
        assertEquals(RiderStateType.ACTIVE, endRes.getType());
    }

    @Test
    public void updateRiderState() {
        RealmList<RiderStageConnection> riderStageConnections = new RealmList<>();
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setOfficialGap(100);
        riderStageConnection.setOfficialTime(100);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(100);

        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
        }

        initalRider();

        Rider rider = realm.where(Rider.class).findAll().first();
        RiderStageConnection stageConnection = realm.where(RiderStageConnection.class).findAll().first();
        riderStageConnections.add(stageConnection);

        synchronized (this) {
            riderRepository.updateRiderStageConnection(rider, riderStageConnections, onUpdateRiderStageCallback);
        }

        synchronized (this) {
            riderStageConnectionRepository.updateRiderState(RiderStateType.DROP, rider, onUpdateRiderStateCallBack);
        }

        RiderStageConnection res = riderStageConnectionRepository.getAllRiderStateConnections().first();

        assertEquals(RiderStateType.DROP, res.getType());
    }

    private void initalRider() {
        Rider rider = new Rider();
        rider.setTeamShortName("T");
        rider.setTeamName("TEST");
        rider.setCountry("T");
        rider.setName("TEST");
        rider.setStartNr(1);
        synchronized (this){
            riderRepository.addRider(rider, onSaveRiderCallback);
        }
    }

    @Test
    public void getRiderByRank() {
        initalData();
        RiderStageConnection rider = null;
        for (RiderStageConnection riderStageConnection : riderStageConnectionRepository.getAllRiderStateConnections()) {
           if (riderStageConnection.getBonusPoint() == 20) {
               rider = riderStageConnection;
           }
        }
        assertEquals(100, rider.getOfficialGap());
        assertEquals(100, rider.getVirtualGap());
        assertEquals(100, rider.getOfficialTime());
        assertEquals(20, rider.getBonusPoint());
        assertEquals(400, rider.getMountainBonusPoints());
        assertEquals(300, rider.getSprintBonusPoints());
        assertEquals(40, rider.getBonusTime());
        assertEquals(RiderStateType.DOCTOR, rider.getType());
    }

    @Test
    public void updateRiderStageConnectionRank(){
        Date date = new Date();
        RiderRanking virtRank = new RiderRanking();
        virtRank.setRank(1);
        virtRank.setType(RankingType.VIRTUAL);
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setOfficialGap(100);
        riderStageConnection.setOfficialTime(100);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(100);

        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
        }
        RiderStageConnection rSC = realm.where(RiderStageConnection.class).equalTo("virtualGap", riderStageConnection.getVirtualGap()).findFirst();

        synchronized (this) {
            riderStageConnectionRepository.updateRiderStageConnectionRanking(virtRank, rSC);
        }

        rSC = realm.where(RiderStageConnection.class).equalTo("id", rSC.getId()).findFirst();

        assertEquals(1, rSC.getRiderRanking(RankingType.VIRTUAL).getRank());
    }

    @Test
    public void clearAllRaceGroups(){
        riderStageConnectionRepository.clearAllRiderStageConnection();
        synchronized (this) {
            riderStageConnectionRepository.getRiderStageConnections(onGetAllRiderStageConnectionsCallback);
        }
        assertEquals(0, riderStageConnections.size());
    }

}
