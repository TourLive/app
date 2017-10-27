package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;

public interface ISpecialRankingPresenter extends IBasePresenter {
    void addSpecialRanking(SpecialRanking specialRanking);
    void clearAllSpecialRankings();
}
