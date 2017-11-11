package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStageConnectionRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import io.realm.RealmList;


public class RiderStageConnectionPresenter implements IRiderStageConnectionPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static RiderStageConnectionPresenter instance = null;
    private RiderStageConnectionRepository riderStageConnectionRepository = new RiderStageConnectionRepository();

    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallbackCallback;
    private IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback onGetAllRiderStageConnectionsCallback;
    private IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack onUpdateRiderStageConnectionCallBack;
    private IRiderStageConnectionRepository.OnUpdateRiderStateCallBack onUpdateRiderStateCallBack;

    public static RiderStageConnectionPresenter getInstance() {
        if(instance == null){
            instance = new RiderStageConnectionPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRiderStageConnectionCallbackCallback = new IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented

            }
        };

        onGetAllRiderStageConnectionsCallback = new IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback() {
            @Override
            public void onSuccess(RealmList<RiderStageConnection> stageConnections) {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onUpdateRiderStageConnectionCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onUpdateRiderStateCallBack = new IRiderStageConnectionRepository.OnUpdateRiderStateCallBack() {
            @Override
            public void onSuccess(RiderStageConnection connection) {
                for(Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
                        ((RaceFragment) frag).updateRiderStateOnGUI(connection);
                    }
                    if(frag instanceof RiderRaceGroupFragment){
                        ((RiderRaceGroupFragment) frag).updateRiderStateOnGUI(connection);
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
    public void updateRiderStageConnection(RiderStageConnection newRiderStageConnection, RiderStageConnection oldRiderStageConnection) {
        riderStageConnectionRepository.updateRiderStageConnection(newRiderStageConnection, oldRiderStageConnection, onUpdateRiderStageConnectionCallBack);
    }

    @Override
    public void deleteRiderStageConnection() {
        // Not implemented yet
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

    @Override
    public RealmList<RiderStageConnection> getAllRiderStateConnections(){
        return riderStageConnectionRepository.getAllRiderStateConnections();
    }

    @Override
    public void updateRiderStageConnectionRank(final int rank, final RiderStageConnection connection){
        riderStageConnectionRepository.updateRiderStageConnectionRank(rank, connection);
    }

}
