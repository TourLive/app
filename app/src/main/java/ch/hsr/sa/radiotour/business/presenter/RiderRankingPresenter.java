package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IMaillotPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderRankingPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderRaceGroupAdapter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IMaillotRepository;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRankingRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.repositories.MaillotRepository;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRankingRepository;
import ch.hsr.sa.radiotour.presentation.fragments.MaillotsFragment;
import io.realm.RealmList;

public class RiderRankingPresenter implements IRiderRankingPresenter {
    private static RiderRankingPresenter instance = null;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private RiderRankingRepository riderRankingRepository = new RiderRankingRepository();

    private IRiderRankingRepository.OnSaveRiderRankingCallback onSaveRiderRankingCallback;

    public static RiderRankingPresenter getInstance() {
        if (instance == null) {
            instance = new RiderRankingPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }


    @Override
    public void subscribeCallbacks() {
        onSaveRiderRankingCallback = new IRiderRankingRepository.OnSaveRiderRankingCallback() {
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
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveRiderRankingCallback = null;
    }


    @Override
    public void addRiderRanking(RiderRanking riderRanking) {
        riderRankingRepository.addRiderRanking(riderRanking, onSaveRiderRankingCallback);
    }

    @Override
    public RiderRanking getFirstInRanking(RankingType type) {
        return riderRankingRepository.getFirstInRanking(type);
    }

    @Override
    public RiderRanking getRiderRanking(RiderRanking riderRanking){
        return riderRankingRepository.getRiderRanking(riderRanking);
    }

    @Override
    public void clearAllRiderRankings() {
        riderRankingRepository.clearAllRiderRankings();
    }
}
