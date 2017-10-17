package ch.hsr.sa.radiotour.controller.adapter.presenter;

import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRiderStatePresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStateRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStateRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;

public class RiderStatePresenter implements IRiderStatePresenter {
    private RaceFragment raceFragment;
    private IRiderStateRepository riderStateRepository;
    private IRiderStateRepository.OnSaveRiderStateCallback onSaveRiderStateCallback;
    private IRiderStateRepository.OnGetRiderStateCallback onGetRiderStateCallback;
    private IRiderStateRepository.OnUpdateRiderStateCallBack onUpdateRiderStateCallBack;

    public RiderStatePresenter(RaceFragment raceFragment){
        this.raceFragment = raceFragment;
        this.riderStateRepository = new RiderStateRepository();
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRiderStateCallback = new IRiderStateRepository.OnSaveRiderStateCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {

            }
        };

        onGetRiderStateCallback = new IRiderStateRepository.OnGetRiderStateCallback(){
            @Override
            public void onSuccess(RiderState riderState) {

            }

            @Override
            public void onError(String message) {

            }
        };

        onUpdateRiderStateCallBack = new IRiderStateRepository.OnUpdateRiderStateCallBack(){
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
        onGetRiderStateCallback = null;
        onUpdateRiderStateCallBack = null;
        onSaveRiderStateCallback = null;
    }

    @Override
    public void addRiderState(RiderState riderState) {
        riderStateRepository.addRiderState(riderState, onSaveRiderStateCallback);
    }

    @Override
    public void getRiderState(Rider rider) {
        riderStateRepository.getRiderState(rider, onGetRiderStateCallback);
    }

    @Override
    public void clearAllRiderState() {

    }

    @Override
    public void updateRiderState(RiderState newRiderState) {
        riderStateRepository.updateRiderState(newRiderState, onUpdateRiderStateCallBack);
    }

    @Override
    public void deleteRiderState() {

    }
}
