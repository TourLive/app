package ch.hsr.sa.radiotour;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;

/**
 * Created by Urs Forrer on 30.10.2017.
 */

public class RaceGroupRepositoryInstrumentedTest {

    private Realm realm;
    private RiderRepository riderRepository;
    private RaceGroupRepository raceGroupRepository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRaceGroupRepository.OnSaveRaceGroupCallback onSaveRaceGroupCallback;
    private IRaceGroupRepository.OnGetAllRaceGroupsCallback onGetAllRaceGroupsCallback;
    private IRaceGroupRepository.OnUpdateRaceGroupCallBack onUpdateRaceGroupCallBack;

    private RealmList<RaceGroup> raceGroups = new RealmList<>();
    private RealmList<Rider> riders = new RealmList<>();

    @Before
    public void initTestData() {
        this.riderRepository = new RiderRepository();
        this.raceGroupRepository = new RaceGroupRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        raceGroups.clear();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Rider.class).findAll().deleteAllFromRealm();
                realm.where(RaceGroup.class).findAll().deleteAllFromRealm();
            }
        });
        createRiders();
    }

    private void createRiders() {
        this.riders.clear();
        for (int i = 0; i < 3; i++) {
            Rider rider = new Rider();
            rider.setTeamShortName("T");
            rider.setTeamName("TEST");
            rider.setCountry("T");
            rider.setName("TEST " + i);
            rider.setStartNr(i);
            this.riders.add(rider);
            synchronized (this){
                riderRepository.addRider(rider, onSaveRiderCallback);
            }
        }
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

        onSaveRaceGroupCallback = new IRaceGroupRepository.OnSaveRaceGroupCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
        onGetAllRaceGroupsCallback = new IRaceGroupRepository.OnGetAllRaceGroupsCallback() {
            @Override
            public void onSuccess(RealmList<RaceGroup> res) {
                getAllRaceGroupsSuccessfully(res);
            }

            @Override
            public void onError(String message) {
            }
        };
        onUpdateRaceGroupCallBack = new IRaceGroupRepository.OnUpdateRaceGroupCallBack() {
            @Override
            public void onSuccess(RaceGroup raceGroup) {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
    }

    private void getAllRaceGroupsSuccessfully(RealmList<RaceGroup> res) {
        this.raceGroups.addAll(res);
        notifyAll();
    }

    private void successFullyAdded() {
        notifyAll();
    }


    @Test
    public void addRaceGroup(){
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setType(RaceGroupType.NORMAL);
        raceGroup.setPosition(0);
        raceGroup.setRiders(riders);

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroup, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();

        assertEquals(1, res.size());
        assertEquals(0, res.get(0).getHistoryGapTime());
        assertEquals(0, res.get(0).getHistoryGapTime());
        assertEquals(0, res.get(0).getPosition());
        assertEquals(RaceGroupType.NORMAL, res.get(0).getType());

    }

    @Test
    public void updateRaceGroupPosition(){
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setType(RaceGroupType.NORMAL);
        raceGroup.setPosition(1);
        raceGroup.setRiders(riders);

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroup, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();
        raceGroupRepository.updateRaceGroupPosition(res.get(0), 0);
        RealmResults<RaceGroup> endRes = realm.where(RaceGroup.class).findAll();

        assertEquals(1, res.size());
        assertEquals(0, endRes.get(0).getPosition());
        assertEquals(RaceGroupType.LEAD, res.get(0).getType());

    }

    @Test
    public void addInitialRaceGroup(){
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setType(RaceGroupType.NORMAL);
        raceGroup.setHistoryGapTime(100);
        raceGroup.setActualGapTime(10);
        raceGroup.setPosition(0);
        raceGroup.setRiders(riders);

        synchronized (this) {
            raceGroupRepository.addInitialRaceGroup(raceGroup, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();

        assertEquals(1, res.size());
        assertEquals(100, res.get(0).getHistoryGapTime());
        assertEquals(10, res.get(0).getActualGapTime());
        assertEquals(0, res.get(0).getPosition());
        assertEquals(RaceGroupType.NORMAL, res.get(0).getType());

    }

    @Test
    public void clearAllRaceGroups(){
        raceGroupRepository.clearAllRaceGroups();
        synchronized (this) {
            raceGroupRepository.getAllRaceGroups(onGetAllRaceGroupsCallback);
        }
        assertEquals(0, raceGroups.size());
    }

    @Test
    public void deleteRiderInRaceGroup() {
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setType(RaceGroupType.NORMAL);
        raceGroup.setPosition(1);
        raceGroup.setRiders(riders);

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroup, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();
        RealmResults<Rider> resRiders = realm.where(Rider.class).findAll();
        Rider riderToDelete = resRiders.get(0);
        assertEquals(3, res.get(0).getRiders().size());
        synchronized (this) {
            raceGroupRepository.deleteRiderInRaceGroup(res.first(), riderToDelete, onUpdateRaceGroupCallBack);
        }
        RealmResults<RaceGroup> endRes = realm.where(RaceGroup.class).findAll();
        assertEquals(2, endRes.get(0).getRiders().size());
    }

    @Test
    public void updateRaceGroupGapTime() {
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setType(RaceGroupType.NORMAL);
        raceGroup.setPosition(1);
        raceGroup.setRiders(riders);

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroup, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();
        assertEquals(0, res.get(0).getHistoryGapTime());
        assertEquals(0, res.get(0).getActualGapTime());
        synchronized (this) {
            raceGroupRepository.updateRaceGroupGapTime(res.get(0), 100, onUpdateRaceGroupCallBack);
        }
        RealmResults<RaceGroup> endRes = realm.where(RaceGroup.class).findAll();
        assertEquals(0, res.get(0).getHistoryGapTime());
        assertEquals(100, res.get(0).getActualGapTime());
    }

    @Test
    public void updateRaceGroupRiders() {
        RaceGroup raceGroupOne = new RaceGroup();
        raceGroupOne.setType(RaceGroupType.LEAD);
        raceGroupOne.setPosition(0);
        raceGroupOne.setRiders(riders);

        RealmList<Rider> newRiders = new RealmList<>();
        for (int i = 3; i < 6; i++) {
            Rider rider = new Rider();
            rider.setTeamShortName("T");
            rider.setTeamName("TEST");
            rider.setCountry("T");
            rider.setName("TEST " + i);
            rider.setStartNr(i);
            newRiders.add(rider);
            synchronized (this){
                riderRepository.addRider(rider, onSaveRiderCallback);
            }
        }

        RaceGroup raceGroupTwo = new RaceGroup();
        raceGroupTwo.setType(RaceGroupType.NORMAL);
        raceGroupTwo.setPosition(1);
        raceGroupTwo.setRiders(newRiders);

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroupOne, onSaveRaceGroupCallback);
        }

        synchronized (this) {
            raceGroupRepository.addRaceGroup(raceGroupTwo, onSaveRaceGroupCallback);
        }

        RealmResults<RaceGroup> res = realm.where(RaceGroup.class).findAll();
        RealmList<Rider> riderToTransfer = new RealmList<>();
        riderToTransfer.add(res.get(0).getRiders().first());

        assertEquals(3, res.get(0).getRiders().size());
        assertEquals(3, res.get(1).getRiders().size());

        synchronized (this) {
            raceGroupRepository.updateRaceGroupRiders(res.get(1), riderToTransfer, onUpdateRaceGroupCallBack);
        }

        RealmResults<RaceGroup> endRes = realm.where(RaceGroup.class).findAll();
        assertEquals(2, res.get(0).getRiders().size());
        assertEquals(4, res.get(1).getRiders().size());
    }
}
