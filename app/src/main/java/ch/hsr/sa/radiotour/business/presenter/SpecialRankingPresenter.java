package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.ISpecialRankingPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.ISpecialRankingRepository;
import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;
import ch.hsr.sa.radiotour.dataaccess.repositories.SpecialRankingRepository;

public class SpecialRankingPresenter implements ISpecialRankingPresenter {
    private ISpecialRankingRepository.OnSaveSpecialRankingCallback onSaveSpecialRankingCallback;
    private ISpecialRankingRepository specialRankingRepository;

    public SpecialRankingPresenter(){
        specialRankingRepository = new SpecialRankingRepository();
    }

    @Override
    public void addSpecialRanking(SpecialRanking specialRanking) {
        specialRankingRepository.addSpecialRanking(specialRanking, onSaveSpecialRankingCallback);
    }

    @Override
    public void clearAllSpecialRankings() {
        specialRankingRepository.clearAllSpecialRankings();
    }

    @Override
    public void subscribeCallbacks() {
         onSaveSpecialRankingCallback = new ISpecialRankingRepository.OnSaveSpecialRankingCallback() {
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
        onSaveSpecialRankingCallback = null;
    }
}
