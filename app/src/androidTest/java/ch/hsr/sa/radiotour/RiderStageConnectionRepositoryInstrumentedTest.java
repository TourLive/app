package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
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
        Date date = new Date();
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setOfficialGap(date);
        riderStageConnection.setOfficialTime(date);
        riderStageConnection.setRank(1);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(date);

        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
        }

        RealmResults<RiderStageConnection> res = realm.where(RiderStageConnection.class).findAll();

        assertEquals(1, res.size());
        assertEquals(date, res.get(0).getOfficialGap());
        assertEquals(date, res.get(0).getVirtualGap());
        assertEquals(date, res.get(0).getOfficialTime());
        assertEquals(10, res.get(0).getBonusPoint());
        assertEquals(20, res.get(0).getBonusTime());
        assertEquals(1, res.get(0).getRank());
        assertEquals(RiderStateType.DNC, res.get(0).getType());

    }
    private Date initalData() {
        Date date = new Date();
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setBonusPoint(10);
        riderStageConnection.setBonusTime(20);
        riderStageConnection.setOfficialGap(date);
        riderStageConnection.setOfficialTime(date);
        riderStageConnection.setRank(1);
        riderStageConnection.setType(RiderStateType.DNC);
        riderStageConnection.setVirtualGap(date);

        RiderStageConnection riderStageConnectionTwo = new RiderStageConnection();
        riderStageConnectionTwo.setBonusPoint(20);
        riderStageConnectionTwo.setBonusTime(40);
        riderStageConnectionTwo.setOfficialGap(date);
        riderStageConnectionTwo.setOfficialTime(date);
        riderStageConnectionTwo.setRank(2);
        riderStageConnectionTwo.setType(RiderStateType.DOCTOR);
        riderStageConnectionTwo.setVirtualGap(date);


        synchronized (this) {
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallback);
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnectionTwo, onSaveRiderStageConnectionCallback);
        }
        return date;
    }

    @Test
    public void getAllRiderStageConnections(){
        Date date = initalData();

        synchronized (this) {
            riderStageConnectionRepository.getRiderStageConnections(onGetAllRiderStageConnectionsCallback);
        }

        RealmResults<RiderStageConnection> res = realm.where(RiderStageConnection.class).findAll();

        assertEquals(2, riderStageConnections.size());
    }

    @Test
    public void updateRiderStageConnection() {
        Date date = initalData();
        String id;

        RiderStageConnection res = realm.where(RiderStageConnection.class).findAll().first();
        id = res.getId();
        RiderStageConnection newStageConnection = new RiderStageConnection();
        newStageConnection.setType(RiderStateType.AKTIVE);
        newStageConnection.setRank(4);
        newStageConnection.setBonusPoint(100);
        newStageConnection.setBonusTime(2020);
        newStageConnection.setVirtualGap(res.getVirtualGap());
        newStageConnection.setOfficialTime(res.getOfficialTime());
        newStageConnection.setOfficialGap(res.getOfficialGap());

        synchronized (this) {
            riderStageConnectionRepository.updateRiderStageConnection(newStageConnection, res, onUpdateRiderStageConnectionCallBack);
        }

        RiderStageConnection endRes = realm.where(RiderStageConnection.class).equalTo("id", id).findAll().first();

        assertEquals(date, endRes.getOfficialGap());
        assertEquals(date, endRes.getVirtualGap());
        assertEquals(date, endRes.getOfficialTime());
        assertEquals(100, endRes.getBonusPoint());
        assertEquals(2020, endRes.getBonusTime());
        assertEquals(4, endRes.getRank());
        assertEquals(RiderStateType.AKTIVE, endRes.getType());
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
        Date date = initalData();

        RiderStageConnection rider = riderStageConnectionRepository.getRiderByRank(2);
        assertEquals(date, rider.getOfficialGap());
        assertEquals(date, rider.getVirtualGap());
        assertEquals(date, rider.getOfficialTime());
        assertEquals(20, rider.getBonusPoint());
        assertEquals(40, rider.getBonusTime());
        assertEquals(2, rider.getRank());
        assertEquals(RiderStateType.DOCTOR, rider.getType());
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
