package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;

public interface IRiderRankingRepository {
    void addRiderRanking(RiderRanking riderRanking, OnSaveRiderRankingCallback callback);

    RiderRanking getFirstInRanking(RankingType type);

    RiderRanking getRiderRanking(RiderRanking riderRanking);

    void clearAllRiderRankings();

    interface OnSaveRiderRankingCallback {
        void onSuccess();

        void onError(String message);
    }
}