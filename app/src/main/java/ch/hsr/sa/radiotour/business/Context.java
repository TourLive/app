package ch.hsr.sa.radiotour.business;

import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.StagePresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import io.realm.RealmList;

public final class Context {


    public static void addRider(Rider rider){
        RiderPresenter.getInstance().addRider(rider);
    }

    public static void deleteRiders(){
        RiderPresenter.getInstance().clearAllRiders();
    }

    public static RealmList<Rider> getAllRiders(){
        return RiderPresenter.getInstance().getAllRidersReturned();
    }

    public static void addRaceGroup(RaceGroup raceGroup){
        RaceGroupPresenter.getInstance().addRaceGroup(raceGroup);
    }

    public static void deleteRaceGroups(){
        RaceGroupPresenter.getInstance().clearAllRaceGroups();
    }

    public static void addJudgment(Judgement judgment){
        JudgmentPresenter.getInstance().addJudgment(judgment);
    }

    public static void deleteJudgments(){
        JudgmentPresenter.getInstance().clearAllJudgments();
    }

    public static void addReward(Reward reward){
        RewardPresenter.getInstance().addReward(reward);
    }

    public static void deleteRewards(){
        RewardPresenter.getInstance().clearAllRewards();
    }

    public static RealmList<Judgement> getJudgmentsById(int judgmentId){
        return JudgmentPresenter.getInstance().getJudgmentsById(judgmentId);
    }

    public static void addStage(Stage stage){
        StagePresenter.getInstance().addStage(stage);
    }

    public static void deleteStages(){
        StagePresenter.getInstance().clearAllStages();
    }

    public static void addRiderStageConnection(RiderStageConnection riderStageConnection){
        RiderStageConnectionPresenter.getInstance().addRiderStageConnection(riderStageConnection);
    }

    public static void updateRiderStageConnection(Rider rider, RealmList<RiderStageConnection> riderStageConnection){
        RiderPresenter.getInstance().updateRiderStageConnection(rider, riderStageConnection);
    }

    public static void updateRiderStageConnectionRank(int rank, RiderStageConnection connection){
        RiderStageConnectionPresenter.getInstance().updateRiderStageConnectionRank(rank, connection);
    }

    public static void deleteAllRiderStageConnections(){
        RiderStageConnectionPresenter.getInstance().clearAllRiderStageConnection();
    }

    public static RiderStageConnection getRiderStageConnectionByRank(int rank){
        return RiderStageConnectionPresenter.getInstance().getRiderByRank(rank);
    }

    public static RealmList<RiderStageConnection> getAllRiderStageConnections(){
        return RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
    }
}
