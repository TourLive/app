package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
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
public class RiderRepositoryInstrumentedTest {

    private Realm realm;
    private RiderRepository riderRepository;
    private RiderStageConnectionRepository riderStageConnectionRepository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;
    private IRiderRepository.OnUpdateRiderStageCallback OnUpdateRiderStageCallback;
    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallbackCallback;

    private RealmList<Rider> riders = new RealmList<>();

    @Before
    public void initTestData() {
        this.riderRepository = new RiderRepository();
        this.riderStageConnectionRepository = new RiderStageConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        riders.clear();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Rider.class).findAll().deleteAllFromRealm();
                realm.where(RiderStageConnection.class).findAll().deleteAllFromRealm();
            }
        });
    }


    private void initCallbacks(){
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                riderAddedSuccessfully();
            }

            @Override
            public void onError(String message) {
            }
        };

        onGetAllRidersCallback = new IRiderRepository.OnGetAllRidersCallback() {
            @Override
            public void onSuccess(RealmList<Rider> res) {
                getAllRidersSuccessfully(res);
            }

            @Override
            public void onError(String message) {
            }
        };

        OnUpdateRiderStageCallback = new IRiderRepository.OnUpdateRiderStageCallback() {
            @Override
            public void onSuccess() {
                updateRiderStateSuccessfully();
            }

            @Override
            public void onError(String message) {
            }
        };

        onSaveRiderStageConnectionCallbackCallback = new IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback() {
            @Override
            public void onSuccess() {
                saveRiderStageConnectionSuccessfully();
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
    public void addRider(){
        Rider rider = new Rider();
        rider.setCountry("swiss");
        rider.setName("testrider");
        rider.setStartNr(15);
        rider.setTeamName("Swiss");
        rider.setTeamShortName("CH");
        synchronized (this){
            riderRepository.addRider(rider, onSaveRiderCallback);
        }
        RealmResults<Rider> riders = realm.where(Rider.class).findAll();
        RealmList<Rider> res = new RealmList<>();
        for(Rider r : riders){
            if(r.getStartNr() == 15){
                res.add(r);
            }
        }
        assertEquals(15, res.first().getStartNr());

    }

    @Test
    public void getRidersTest(){
        Rider rider = new Rider();
        for(int i = 1; i < 4; i++){
            rider.setCountry("swiss");
            rider.setName("testrider" + 1);
            rider.setStartNr(i);
            rider.setTeamName("Swiss");
            rider.setTeamShortName("CH");
            synchronized (this){
                riderRepository.addRider(rider, onSaveRiderCallback);
            }
        }
        synchronized (this){
            riderRepository.getAllRiders(onGetAllRidersCallback);
        }
        assertEquals(3, riders.size());
        assertEquals(2, riders.get(1).getStartNr());
    }

    @Test
    public void getAllRidersWithoutCallback(){
        Rider rider = new Rider();
        for(int i = 1; i < 4; i++){
            rider.setCountry("swiss");
            rider.setName("testrider" + 1);
            rider.setStartNr(i);
            rider.setTeamName("Swiss");
            rider.setTeamShortName("CH");
            synchronized (this){
                riderRepository.addRider(rider, onSaveRiderCallback);
            }
        }
        riders = riderRepository.getAllRidersReturned();
        assertEquals(3, riders.size());
        assertEquals(2, riders.get(1).getStartNr());
    }

    @Test
    public void clearAllRiders(){
        riderRepository.clearAllRiders();
        assertEquals(0, riderRepository.getAllRidersReturned().size());
    }

    @Test
    public void updateRiderStateConnection(){

        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setRank(1);
        riderStageConnection.setOfficialTime(new Date(100));
        riderStageConnection.setOfficialGap(new Date(100));
        riderStageConnection.setVirtualGap(new Date(100));
        riderStageConnection.setBonusPoint(100);
        riderStageConnection.setBonusTime(200);
        riderStageConnection.setType(RiderStateType.AKTIVE);
        synchronized (this){
            riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallbackCallback);
        }

        Rider rider = new Rider();
        rider.setCountry("swiss");
        rider.setName("testrider" + 1);
        rider.setStartNr(15);
        rider.setTeamName("Swiss");
        rider.setTeamShortName("CH");

        synchronized (this){
            riderRepository.addRider(rider, onSaveRiderCallback);
        }


        RealmList<RiderStageConnection> connections = new RealmList<>();
        synchronized (this){
            connections.add(riderStageConnectionRepository.getRiderByRank(1));
            riderRepository.updateRiderStageConnection(rider, connections, OnUpdateRiderStageCallback);
        }

        Rider realmRider = realm.where(Rider.class).equalTo("startNr", rider.getStartNr()).findFirst();
        RealmList<RiderStageConnection> riderStageConnections = realmRider.getRiderStages();

        assertEquals(1, riderStageConnections.get(0).getRank());
        assertEquals(100, riderStageConnections.get(0).getBonusPoint());
        assertEquals(200, riderStageConnections.get(0).getBonusTime());
    }

    private void riderAddedSuccessfully(){
        notifyAll();
    }

    private void getAllRidersSuccessfully(RealmList<Rider> riders){
        this.riders.addAll(riders);
        notifyAll();
    }

    private void updateRiderStateSuccessfully(){
        notifyAll();
    }

    private void saveRiderStageConnectionSuccessfully() {
        notifyAll();
    }
}
