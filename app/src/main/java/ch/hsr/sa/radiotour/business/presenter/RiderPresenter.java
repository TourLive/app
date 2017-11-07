package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import io.realm.RealmList;

public class RiderPresenter implements IRiderPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private static RiderPresenter instance = null;

    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;
    private IRiderRepository.OnUpdateRiderStageCallback onUpdateRiderStateCallback;


    private RiderRepository riderRepository = new RiderRepository();

    public static RiderPresenter getInstance() {
        if(instance == null){
            instance = new RiderPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }


    @Override
    public void addRider(Rider rider) { riderRepository.addRider(rider, onSaveRiderCallback); }

    @Override
    public void getAllRiders() {
        riderRepository.getAllRiders(onGetAllRidersCallback);
    }

    @Override
    public RealmList<Rider> getAllRidersReturned() { return riderRepository.getAllRidersReturned(); }

    @Override
    public Rider getRiderByStartNr(int startNr){ return riderRepository.getRiderByStartNr(startNr); }

    @Override
    public RealmList<Rider> getAllUnknownRidersReturned() { return riderRepository.getAllUnknownRidersReturned(); }

    @Override
    public void removeRider(Rider rider) {
        riderRepository.removeRider(rider, onSaveRiderCallback);
    }

    @Override
    public void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections) {
        riderRepository.updateRiderStageConnection(rider, connections, onUpdateRiderStateCallback);
    }

    @Override
    public void clearAllRiders() { riderRepository.clearAllRiders(); }


    @Override
    public void subscribeCallbacks() {
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
                        RaceFragment rF = (RaceFragment) frag;
                        rF.addRiderToList();
                    } else if (frag instanceof RiderRaceGroupFragment) {
                        RiderRaceGroupFragment riderRaceGroupFragment = (RiderRaceGroupFragment) frag;
                        riderRaceGroupFragment.addRiderToList();
                    } else {
                        // Do nothing because a not supported fragment
                    }
                }
            }

            @Override
            public void onError(String message) {
            }
        };

        onGetAllRidersCallback = new IRiderRepository.OnGetAllRidersCallback() {
            @Override
            public void onSuccess(RealmList<Rider> riders) {
                for(Fragment frag : fragments){
                    if(frag instanceof RaceFragment) {
                        RaceFragment rF = (RaceFragment) frag;
                        rF.showRiders(riders);
                    } else if (frag instanceof RiderRaceGroupFragment) {
                        RiderRaceGroupFragment riderRaceGroupFragment = (RiderRaceGroupFragment) frag;
                        riderRaceGroupFragment.showRiders(riders);
                    } else {
                        // Do nothing because a not supported fragment
                    }
                }
            }

            @Override
            public void onError(String message) {
            }
        };

        onUpdateRiderStateCallback = new IRiderRepository.OnUpdateRiderStageCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRiderCallback = null;
        onGetAllRidersCallback = null;
        onUpdateRiderStateCallback = null;
    }

}
