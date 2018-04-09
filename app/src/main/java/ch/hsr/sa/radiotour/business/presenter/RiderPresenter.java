package ch.hsr.sa.radiotour.business.presenter;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;
import io.realm.RealmList;

public class RiderPresenter implements IRiderPresenter {
    private static RiderPresenter instance = null;
    private static Handler handler;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;
    private IRiderRepository.OnUpdateRiderStageCallback onUpdateRiderStateCallback;
    private RiderRepository riderRepository = new RiderRepository();

    public static RiderPresenter getInstance() {
        if (instance == null) {
            instance = new RiderPresenter();
            if ((Looper.getMainLooper().getThread() != Thread.currentThread()))
                Looper.prepare();
            handler = new Handler();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }

    public void removeView(Fragment fragment) {
        this.fragments.remove(fragment);
    }

    @Override
    public void addRiderNone(Rider rider) {
        riderRepository.addRider(rider, null);
    }

    @Override
    public void getAllRiders() {
        riderRepository.getAllRiders(onGetAllRidersCallback);
    }

    @Override
    public RealmList<Rider> getAllRidersReturned() {
        return riderRepository.getAllRidersReturned();
    }

    @Override
    public RealmList<Rider> getAllActiveRidersReturned() {
        return riderRepository.getAllActiveRidersReturned();
    }

    @Override
    public Rider getRiderByStartNr(int startNr) {
        return riderRepository.getRiderByStartNr(startNr);
    }

    @Override
    public RealmList<Rider> getAllUnknownRidersReturned() {
        return riderRepository.getAllUnknownRidersReturned();
    }

    @Override
    public void removeRiderWithoutCallback(Rider rider) {
        riderRepository.removeRider(rider, null);
    }

    @Override
    public void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> connections) {
        riderRepository.updateRiderStageConnection(rider, connections, onUpdateRiderStateCallback);
    }

    @Override
    public void clearAllRiders() {
        riderRepository.clearAllRiders();
    }


    @Override
    public void subscribeCallbacks() {
        onSaveRiderCallback = new IRiderRepository.OnSaveRiderCallback() {
            @Override
            public void onSuccess() {
                for (Fragment frag : fragments) {
                    if (frag instanceof RaceFragment) {
                        handler.post(() -> {
                            RaceFragment rF = (RaceFragment) frag;
                            rF.addRiderToList();
                        });
                    } else if (frag instanceof RiderRaceGroupFragment) {
                        handler.post(() -> {
                            RiderRaceGroupFragment riderRaceGroupFragment = (RiderRaceGroupFragment) frag;
                            riderRaceGroupFragment.addRiderToList();
                        });
                    } else {
                        // Do nothing because a not supported fragment
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onGetAllRidersCallback = new IRiderRepository.OnGetAllRidersCallback() {
            @Override
            public void onSuccess(RealmList<Rider> riders) {
                for (Fragment frag : fragments) {
                    if (frag instanceof RaceFragment) {
                        RaceFragment rF = (RaceFragment) frag;
                        rF.showRiders(riders);
                    } else if (frag instanceof RiderRaceGroupFragment) {
                        RiderRaceGroupFragment riderRaceGroupFragment = (RiderRaceGroupFragment) frag;
                        riderRaceGroupFragment.showRiders(riders);
                    } else if (frag instanceof VirtualClassFragment) {
                        VirtualClassFragment virtualClassFragment = (VirtualClassFragment) frag;
                        virtualClassFragment.updateRiders(riders);
                    } else {
                        // Do nothing because a not supported fragment
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onUpdateRiderStateCallback = new IRiderRepository.OnUpdateRiderStageCallback() {
            @Override
            public void onSuccess() {
                // Not needed and therefore not implemented
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
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
