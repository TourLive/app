package ch.hsr.sa.radiotour.business;

import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
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
        RaceGroupPresenter pres = new RaceGroupPresenter(null);
        pres.addRaceGroup(raceGroup);
    }

    public static void deleteRaceGroups(){
        RaceGroupPresenter pres = new RaceGroupPresenter(null);
        pres.clearAllRaceGroups();
    }

    public static void addJudgment(Judgement judgment){
        JudgmentPresenter judgmentPresenter = new JudgmentPresenter();
        judgmentPresenter.addJudgment(judgment);
    }

    public static void deleteJudgments(){
        JudgmentPresenter judgmentPresenter = new JudgmentPresenter();
        judgmentPresenter.clearAllJudgments();
    }

    public static void addReward(Reward reward){
        RewardPresenter rewardPresenter = new RewardPresenter();
        rewardPresenter.addReward(reward);
    }

    public static void deleteRewards(){
        RewardPresenter rewardPresenter = new RewardPresenter();
        rewardPresenter.clearAllRewards();
    }
}
