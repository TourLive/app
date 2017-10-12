package ch.hsr.sa.radiotour.controller.adapter.presenter;

import android.widget.Toast;

import ch.hsr.sa.radiotour.dataaccess.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.Rider;
import ch.hsr.sa.radiotour.dataaccess.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderChartFragment;
import io.realm.RealmList;
import io.realm.RealmResults;

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
    public void addRider(Rider rider) {
        riderRepository.addRider(rider, onSaveRiderCallback);
    }

    @Override
    public void getAllRiders() {
        riderRepository.getAllRiders(onGetAllRidersCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                //view.showMessage("Added");
            }

            @Override
            public void onError(String message) {
                //view.showMessage(message);
            }
        };

        onGetAllRidersCallback = new IRiderRepository.OnGetAllRidersCallback() {
            @Override
            public void onSuccess(RealmList<Rider> riders) {
                view.showRiders(riders);
            }

            @Override
            public void onError(String message) {
                //view.showMessage(message);
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRiderCallback = null;
        onGetAllRidersCallback = null;
    }

}
