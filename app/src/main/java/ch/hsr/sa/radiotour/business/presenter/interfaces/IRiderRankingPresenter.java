package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;

public interface IRiderRankingPresenter extends IBasePresenter {
    void addRiderRanking(RiderRanking riderRanking);
    RiderRanking getFirstInRanking(RankingType type);
    RiderRanking getRiderRanking(RiderRanking riderRanking);
    void clearAllRiderRankings();
}
