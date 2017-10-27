package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStageConnectionRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import io.realm.RealmList;


public class RiderStageConnectionPresenter implements IRiderStageConnectionPresenter {
    private RaceFragment raceFragment;
    private IRiderStageConnectionRepository riderStageConnectionRepository;
    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallbackCallback;
    private IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback onGetAllRiderStageConnectionsCallback;
    private IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack onUpdateRiderStageConnectionCallBack;
    private IRiderStageConnectionRepository.OnUpdateRiderStateCallBack onUpdateRiderStateCallBack;

    public RiderStageConnectionPresenter(RaceFragment raceFragment){
        this.raceFragment = raceFragment;
        this.riderStageConnectionRepository = new RiderStageConnectionRepository();
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRiderStageConnectionCallbackCallback = new IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {

            }
        };

        onGetAllRiderStageConnectionsCallback = new IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback() {
            @Override
            public void onSuccess(RealmList<RiderStageConnection> stageConnections) {

            }

            @Override
            public void onError(String message) {

            }
        };

        onUpdateRiderStageConnectionCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {

            }
        };

        onUpdateRiderStateCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStateCallBack() {
            @Override
            public void onSuccess(RiderStageConnection connection) {
                //raceFragment.updateRiderState(connection);
            }

            @Override
            public void onError(String message) {

            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRiderStageConnectionCallbackCallback = null;
        onGetAllRiderStageConnectionsCallback = null;
        onUpdateRiderStageConnectionCallBack = null;
        onUpdateRiderStateCallBack = null;
    }

    @Override
    public void addRiderStageConnection(RiderStageConnection riderStageConnection) {
        riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallbackCallback);
    }

    @Override
    public void getRiderStageConnections() {
        riderStageConnectionRepository.getRiderStageConnections(onGetAllRiderStageConnectionsCallback);
    }

    @Override
    public void updateRiderStageConnection(RiderStageConnection newRiderStageConnection) {
        riderStageConnectionRepository.updateRiderStageConnection(newRiderStageConnection, onUpdateRiderStageConnectionCallBack);
    }

    @Override
    public void deleteRiderStageConnection() {

    }

    @Override
    public void updateRiderState(final RiderStateType type, Rider rider) {
        riderStageConnectionRepository.updateRiderState(type, rider, onUpdateRiderStateCallBack);
    }

    @Override
    public void clearAllRiderStageConnection() {
        riderStageConnectionRepository.clearAllRiderStageConnection();
    }

    @Override
    public RiderStageConnection getRiderByRank(final int rank) {
        return riderStageConnectionRepository.getRiderByRank(rank);
    }
}
