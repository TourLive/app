package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderStageConnectionRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;
import io.realm.RealmList;


public class RiderStageConnectionPresenter implements IRiderStageConnectionPresenter {
    private static RiderStageConnectionPresenter instance = null;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private RiderStageConnectionRepository riderStageConnectionRepository = new RiderStageConnectionRepository();

    private IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback onSaveRiderStageConnectionCallbackCallback;
    private IRiderStageConnectionRepository.OnGetAllRiderStageConnectionsCallback onGetAllRiderStageConnectionsCallback;
    private IRiderStageConnectionRepository.OnUpdateRiderStageConnectionCallBack onUpdateRiderStageConnectionCallBack;
    private IRiderStageConnectionRepository.OnUpdateRiderStateCallBack onUpdateRiderStateCallBack;

    public static RiderStageConnectionPresenter getInstance() {
        if (instance == null) {
            instance = new RiderStageConnectionPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveRiderStageConnectionCallbackCallback = new IRiderStageConnectionRepository.OnSaveRiderStageConnectionCallback() {
            @Override
            public void onSuccess() {
                for (Fragment frag : fragments) {
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
                for (Fragment frag : fragments) {
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
                for (Fragment frag : fragments) {
                    if (frag instanceof VirtualClassFragment) {
                        ((VirtualClassFragment) frag).updateRiderStageConnection();
                    }
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
                for (Fragment frag : fragments) {
                    if (frag instanceof RaceFragment) {
                        ((RaceFragment) frag).updateRiderStateOnGUI(connection);
                    }
                    if (frag instanceof RiderRaceGroupFragment) {
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
    public RiderStageConnection addRiderStageConnection(RiderStageConnection riderStageConnection) {
        return riderStageConnectionRepository.addRiderStageConnection(riderStageConnection, onSaveRiderStageConnectionCallbackCallback);
    }

    @Override
    public void updateRiderStageConnectionReward(RiderStageConnection riderStageConnection) {
        riderStageConnectionRepository.updateRiderStageConnectionReward(riderStageConnection, onUpdateRiderStageConnectionCallBack);
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
    public RealmList<RiderStageConnection> getAllRiderStateConnections() {
        return riderStageConnectionRepository.getAllRiderStateConnections();
    }

    @Override
    public void updateRiderStageConnectionRanking(final RiderRanking riderRanking, final RiderStageConnection connection) {
        riderStageConnectionRepository.updateRiderStageConnectionRanking(riderRanking, connection);
    }

    @Override
    public void updateRiderStageConnectionTime(long timeBefore, long timeStamp, RaceGroup res) {
        riderStageConnectionRepository.updateRiderStageConnectionTime(timeBefore, timeStamp, res, onUpdateRiderStageConnectionCallBack);
    }

    @Override
    public void appendTimeInLeadGroup(final RiderStageConnection riderStageConnection, int value){
        riderStageConnectionRepository.appendTimeInLeadGroup(riderStageConnection, value);
    }

    @Override
    public void appendDistanceInLeadGroup(final RiderStageConnection riderStageConnection, double value){
        riderStageConnectionRepository.appendDistanceInLeadGroup(riderStageConnection, value);
    }

}
