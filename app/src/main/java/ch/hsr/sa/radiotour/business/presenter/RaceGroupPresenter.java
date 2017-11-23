package ch.hsr.sa.radiotour.business.presenter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RaceGroupRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import io.realm.RealmList;

public class RaceGroupPresenter implements IRaceGroupPresenter {
    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();
    private static RaceGroupPresenter instance = null;
    private RaceGroupRepository raceGroupRepository = new RaceGroupRepository();

    private IRaceGroupRepository.OnSaveRaceGroupCallback onSaveRaceGroupCallback;
    private IRaceGroupRepository.OnGetAllRaceGroupsCallback onGetAllRaceGroupsCallback;
    private IRaceGroupRepository.OnUpdateRaceGroupCallBack onUpdateRaceGroupCallBack;

    private static Handler handler;

    public static RaceGroupPresenter getInstance() {
        if(instance == null){
            instance = new RaceGroupPresenter();
            if((Looper.getMainLooper().getThread() != Thread.currentThread()))
                Looper.prepare();
            handler = new Handler();
        }
        return instance;
    }

    public void addView(android.support.v4.app.Fragment frag){
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRaceGroupCallback = new IRaceGroupRepository.OnSaveRaceGroupCallback() {
            @Override
            public void onSuccess() {
                for(android.support.v4.app.Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
                        ((RaceFragment) frag).addRaceGroupToList();
                    }
                    if(frag instanceof RiderRaceGroupFragment){
                        ((RiderRaceGroupFragment) frag).addRaceGroupToList();
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onGetAllRaceGroupsCallback = new IRaceGroupRepository.OnGetAllRaceGroupsCallback() {
            @Override
            public void onSuccess(RealmList<RaceGroup> raceGroups) {
                for(android.support.v4.app.Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
                        handler.post(() -> {
                            ((RaceFragment) frag).showRaceGroups(raceGroups);
                        });
                    }
                    if(frag instanceof RiderRaceGroupFragment){
                        handler.post(() -> {
                            ((RiderRaceGroupFragment) frag).showRaceGroups(raceGroups);
                        });
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };
        onUpdateRaceGroupCallBack = new IRaceGroupRepository.OnUpdateRaceGroupCallBack() {
            @Override
            public void onSuccess(RaceGroup raceGroup) {

                for(android.support.v4.app.Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
                        handler.post(() -> {
                            ((RaceFragment) frag).addRaceGroupToList();
                        });
                    }
                    if(frag instanceof RiderRaceGroupFragment){
                        handler.post(() -> {
                            ((RiderRaceGroupFragment) frag).addRaceGroupToList();
                        });
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onGetAllRaceGroupsCallback = null;
        onSaveRaceGroupCallback = null;
        onUpdateRaceGroupCallBack = null;
    }

    @Override
    public void addRaceGroup(RaceGroup raceGroup) {
        raceGroupRepository.addRaceGroup(raceGroup, onSaveRaceGroupCallback);
    }

    @Override
    public void addRaceGroupWithoutCallback(RaceGroup raceGroup) {
        raceGroupRepository.addRaceGroup(raceGroup, null);
    }

    @Override
    public void addInitialRaceGroup(RaceGroup raceGroup) {
        raceGroupRepository.addInitialRaceGroup(raceGroup, onSaveRaceGroupCallback);
    }

    @Override
    public void updateRaceGroupRiders(RaceGroup raceGroup, RealmList<Rider> newRiders) {
        raceGroupRepository.updateRaceGroupRiders(raceGroup, newRiders, onUpdateRaceGroupCallBack);
    }

    @Override
    public void updateRaceGroupGapTime(RaceGroup raceGroup, String minutes, String seconds) {
        long convertedMinutes = Long.parseLong(minutes);
        long convertedSeconds = Long.parseLong(seconds);
        long timeStamp = (60 * convertedMinutes) + convertedSeconds;
        raceGroupRepository.updateRaceGroupGapTime(raceGroup, timeStamp, onUpdateRaceGroupCallBack);
    }

    @Override
    public void getAllRaceGroups() {
        raceGroupRepository.getAllRaceGroups(onGetAllRaceGroupsCallback);
    }

    @Override
    public void clearAllRaceGroups() {
        raceGroupRepository.clearAllRaceGroups();
    }

    @Override
    public void deleteRaceGroup() {
        // Not implemented yet
    }

    @Override
    public void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider) {
        raceGroupRepository.deleteRiderInRaceGroup(raceGroup, rider, onUpdateRaceGroupCallBack);
    }

    @Override
    public void updateRaceGroupPosition(RaceGroup raceGroup, int position) {
        raceGroupRepository.updateRaceGroupPosition(raceGroup, position);
    }
}
