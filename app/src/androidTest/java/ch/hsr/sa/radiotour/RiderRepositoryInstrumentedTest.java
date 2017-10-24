package ch.hsr.sa.radiotour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.RealmModul;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class RiderRepositoryInstrumentedTest {

    private Realm realm;
    private RiderRepository repository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;

    private RealmList<Rider> riders = new RealmList<>();

    @Before
    public void initTestData() {
        this.repository = new RiderRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        riders.clear();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Rider.class).findAll().deleteAllFromRealm();
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
    }

    @Test
    public void addRider(){
        Rider rider = new Rider();
        rider.setCountry("swiss");
        rider.setName("testrider");
        rider.setStartNr(15);
        synchronized (this){
            repository.addRider(rider, onSaveRiderCallback);
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
            synchronized (this){
                repository.addRider(rider, onSaveRiderCallback);
            }
        }
        synchronized (this){
            repository.getAllRiders(onGetAllRidersCallback);
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
            synchronized (this){
                repository.addRider(rider, onSaveRiderCallback);
            }
        }
        riders = repository.getAllRidersReturned();
        assertEquals(3, riders.size());
        assertEquals(2, riders.get(1).getStartNr());
    }

    private void riderAddedSuccessfully(){
        notifyAll();
    }

    private void getAllRidersSuccessfully(RealmList<Rider> riders){
        this.riders.addAll(riders);
        notifyAll();
    }
}
