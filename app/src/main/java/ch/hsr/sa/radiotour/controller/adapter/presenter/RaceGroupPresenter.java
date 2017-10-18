package ch.hsr.sa.radiotour.controller.adapter.presenter;

import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RaceGroupRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import io.realm.RealmList;

public class RaceGroupPresenter implements IRaceGroupPresenter {
    private RaceFragment raceFragment;
    private IRaceGroupRepository.OnSaveRaceGroupCallback onSaveRaceGroupCallback;
    private IRaceGroupRepository.OnGetAllRaceGroupsCallback onGetAllRaceGroupsCallback;
    private IRaceGroupRepository.OnUpdateRaceGroupCallBack onUpdateRaceGroupCallBack;
    private IRaceGroupRepository raceGroupRepository;

    public RaceGroupPresenter(RaceFragment raceFragment) {
        this.raceFragment = raceFragment;
        this.raceGroupRepository = new RaceGroupRepository();

    }
    @Override
    public void subscribeCallbacks() {
        onSaveRaceGroupCallback = new IRaceGroupRepository.OnSaveRaceGroupCallback() {
            @Override
            public void onSuccess() {
                raceFragment.addRaceGroupToList();
            }

            @Override
            public void onError(String message) {

            }
        };

        onGetAllRaceGroupsCallback = new IRaceGroupRepository.OnGetAllRaceGroupsCallback() {
            @Override
            public void onSuccess(RealmList<RaceGroup> raceGroups) {
                raceFragment.showRaceGroups(raceGroups);
            }

            @Override
            public void onError(String message) {

            }
        };
        onUpdateRaceGroupCallBack = new IRaceGroupRepository.OnUpdateRaceGroupCallBack() {
            @Override
            public void onSuccess() {
                raceFragment.addRaceGroupToList();
            }

            @Override
            public void onError(String message) {

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
    }

    @Override
    public void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider) {
        raceGroupRepository.deleteRiderInRaceGroup(raceGroup, rider, onUpdateRaceGroupCallBack);
    }
}
