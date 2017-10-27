package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;

public interface ISpecialRankingRepository {

    void addSpecialRanking(SpecialRanking specialRanking);

    void clearAllSpecialRankings();
}
