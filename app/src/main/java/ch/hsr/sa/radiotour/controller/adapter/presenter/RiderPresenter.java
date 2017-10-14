package ch.hsr.sa.radiotour.controller.adapter.presenter;

import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import io.realm.RealmList;

/**
 * Created by Dom on 12.10.2017.
 */

public class RiderPresenter implements IRiderPresenter {
    private RaceFragment view;

    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;


    private IRiderRepository riderRepository;

    public RiderPresenter(RaceFragment view) {
        this.view = view;
        riderRepository = new RiderRepository();
    }

    @Override
    public void addRider(Rider rider) { riderRepository.addRider(rider, onSaveRiderCallback); }

    @Override
    public void getAllRiders() {
        riderRepository.getAllRiders(onGetAllRidersCallback);
    }

    @Override
    public void clearAllRiders() { riderRepository.clearAllRiders(); }


    @Override
    public void subscribeCallbacks() {
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                view.addRiderToList();
            }

            @Override
            public void onError(String message) {
            }
        };

        onGetAllRidersCallback = new IRiderRepository.OnGetAllRidersCallback() {
            @Override
            public void onSuccess(RealmList<Rider> riders) {
                view.showRiders(riders);
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
    }

}
