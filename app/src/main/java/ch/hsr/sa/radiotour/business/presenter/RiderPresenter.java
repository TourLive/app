package ch.hsr.sa.radiotour.business.presenter;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;
import io.realm.RealmList;

public class RiderPresenter implements IRiderPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private static RiderPresenter instance = null;

    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;
    private IRiderRepository.OnGetAllRidersCallback onGetAllRidersCallback;
    private IRiderRepository.OnUpdateRiderStageCallback onUpdateRiderStateCallback;

    private static Handler handler;

    private RiderRepository riderRepository = new RiderRepository();

    public static RiderPresenter getInstance() {
        if(instance == null){
            instance = new RiderPresenter();
            Looper.prepare();
            handler = new Handler();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }

    public void removeView(Fragment fragment) {
        this.fragments.remove(fragment);
    }

    @Override
    public void addRider(Rider rider) { riderRepository.addRider(rider, onSaveRiderCallback); }

    @Override
    public void addRiderNone(Rider rider) {
        riderRepository.addRider(rider, null);
    }

    @Override
    public void getAllRiders() {
        riderRepository.getAllRiders(onGetAllRidersCallback);
    }

    @Override
    public RealmList<Rider> getAllRidersReturned() { return riderRepository.getAllRidersReturned(); }

    @Override
    public RealmList<Rider> getAllActiveRidersReturned() { return riderRepository.getAllActiveRidersReturned(); }

    @Override
    public Rider getRiderByStartNr(int startNr){ return riderRepository.getRiderByStartNr(startNr); }

    @Override
    public RealmList<Rider> getAllUnknownRidersReturned() { return riderRepository.getAllUnknownRidersReturned(); }

    @Override
    public void removeRider(Rider rider) {
        riderRepository.removeRider(rider, onSaveRiderCallback);
    }

    @Override
    public void removeRiderWithoutCallback(Rider rider){
        riderRepository.removeRider(rider, null);
    }

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
                for(Fragment frag : fragments){
                    if(frag instanceof RaceFragment){
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
                for(Fragment frag : fragments){
                    if(frag instanceof RaceFragment) {
                        RaceFragment rF = (RaceFragment) frag;
                        rF.showRiders(riders);
                    } else if (frag instanceof RiderRaceGroupFragment) {
                        RiderRaceGroupFragment riderRaceGroupFragment = (RiderRaceGroupFragment) frag;
                        riderRaceGroupFragment.showRiders(riders);
                    } else if (frag instanceof VirtualClassFragment) {
                        VirtualClassFragment virtualClassFragment = (VirtualClassFragment) frag;
                        virtualClassFragment.showRiders(riderToExtendedRider(riders));
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

    private List<RiderExtended> riderToExtendedRider (RealmList<Rider> riders) {
        List<RiderExtended> newRiders = new ArrayList<>();
        for (Rider r : riders) {
            RiderStageConnection riderStageConnection = r.getRiderStages().first();
            RiderExtended riderExtended = new RiderExtended();
            riderExtended.setVirtualGap(riderStageConnection.getVirtualGap());
            riderExtended.setTeamShortName(r.getTeamShortName());
            riderExtended.setStartNr(r.getStartNr());
            riderExtended.setRank(riderStageConnection.getRank());
            riderExtended.setOfficialTime(riderStageConnection.getOfficialTime());
            riderExtended.setOfficialGap(riderStageConnection.getOfficialGap());
            riderExtended.setName(r.getName());
            riderExtended.setMoney(riderStageConnection.getMoney());
            riderExtended.setBonusPoint(riderStageConnection.getBonusPoint());
            riderExtended.setCountry(r.getCountry());
            riderExtended.setTeamName(r.getTeamName());
            newRiders.add(riderExtended);
        }
        return newRiders;
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRiderCallback = null;
        onGetAllRidersCallback = null;
        onUpdateRiderStateCallback = null;
    }

}
