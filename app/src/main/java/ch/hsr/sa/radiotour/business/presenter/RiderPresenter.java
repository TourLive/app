package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import io.realm.RealmList;

public class RiderPresenter implements IRiderPresenter {
    private RaceFragment view;

    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;
    private IRiderRepository.OnUpdateRiderStageCallback onUpdateRiderStateCallback;


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
    public RealmList<Rider> getAllRidersReturned() { return riderRepository.getAllRidersReturned(); }

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
