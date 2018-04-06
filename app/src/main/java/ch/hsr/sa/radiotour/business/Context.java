package ch.hsr.sa.radiotour.business;

import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderRankingPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.StagePresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import io.realm.RealmList;

public final class Context {
    private Context() {
        // Empty, needed to hide public Constructor
    }

    public static void addRider(Rider rider) {
        RiderPresenter.getInstance().addRiderNone(rider);
    }

    public static void deleteRiders() {
        RiderPresenter.getInstance().clearAllRiders();
    }

    public static RealmList<Rider> getAllRiders() {
        return RiderPresenter.getInstance().getAllRidersReturned();
    }

    public static void addRaceGroup(RaceGroup raceGroup) {
        RaceGroupPresenter.getInstance().addRaceGroupWithoutCallback(raceGroup);
    }

    public static void deleteRaceGroups() {
        RaceGroupPresenter.getInstance().clearAllRaceGroups();
    }

    public static void addJudgment(Judgement judgment) {
        JudgmentPresenter.getInstance().addJudgment(judgment);
    }

    public static void deleteJudgments() {
        JudgmentPresenter.getInstance().clearAllJudgments();
    }

    public static void addReward(Reward reward) {
        RewardPresenter.getInstance().addReward(reward);
    }

    public static void deleteRewards() {
        RewardPresenter.getInstance().clearAllRewards();
    }

    public static RealmList<Judgement> getJudgmentsById(int judgmentId) {
        return JudgmentPresenter.getInstance().getJudgmentsById(judgmentId);
    }

    public static void addStage(Stage stage) {
        StagePresenter.getInstance().addStage(stage);
    }

    public static void deleteStages() {
        StagePresenter.getInstance().clearAllStages();
    }

    public static RiderStageConnection addRiderStageConnection(RiderStageConnection riderStageConnection) {
        return RiderStageConnectionPresenter.getInstance().addRiderStageConnection(riderStageConnection);
    }

    public static void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> riderStageConnection) {
        RiderPresenter.getInstance().updateRiderStageConnection(rider, riderStageConnection);
    }

    public static void updateRiderStageConnectionRanking(RiderRanking riderRanking, RiderStageConnection connection) {
        RiderStageConnectionPresenter.getInstance().updateRiderStageConnectionRanking(riderRanking, connection);
    }

    public static void deleteAllRiderStageConnections() {
        RiderStageConnectionPresenter.getInstance().clearAllRiderStageConnection();
    }

    public static RealmList<RiderStageConnection> getAllRiderStageConnections() {
        return RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
    }

    public static void addMaillot(Maillot maillot) {
        MaillotPresenter.getInstance().addMaillot(maillot);
    }

    public static void deleteMaillots() {
        MaillotPresenter.getInstance().clearAllMaillots();
    }

    public static RealmList<Maillot> getAllMaillots() {
        return MaillotPresenter.getInstance().getAllMaillotsReturned();
    }

    public static void addRiderToMaillot(Maillot maillot, int riderDbId) {
        MaillotPresenter.getInstance().addRiderToMaillot(maillot, riderDbId);
    }

    public static Maillot getMaillotById(int id) {
        return MaillotPresenter.getInstance().getMaillotById(id);
    }

    public  static void addRiderRanking(RiderRanking riderRanking){
        RiderRankingPresenter.getInstance().addRiderRanking(riderRanking);
    }

    public static RiderRanking getRiderRanking(RiderRanking riderRanking){
        return  RiderRankingPresenter.getInstance().getRiderRanking(riderRanking);
    }

    public static void deleteRiderRankings() {
        RiderRankingPresenter.getInstance().clearAllRiderRankings();
    }

    public static Stage getStage() {
        return StagePresenter.getInstance().getStage();
    }

    public static void updateStage(String raceName, int raceID) {
        StagePresenter.getInstance().updateStageWithRace(raceName, raceID);
    }
}
