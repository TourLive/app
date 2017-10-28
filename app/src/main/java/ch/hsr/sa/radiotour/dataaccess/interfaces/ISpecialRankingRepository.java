package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;

public interface ISpecialRankingRepository {

    interface OnSaveSpecialRankingCallback {
        void onSuccess();
        void onError(String message);
    }

    void addSpecialRanking(SpecialRanking specialRanking, OnSaveSpecialRankingCallback callback);

    void clearAllSpecialRankings();
}
