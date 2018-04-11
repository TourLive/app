package ch.hsr.sa.radiotour.dataaccess.repositories;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import io.realm.Realm;

public class RewardRepository implements IRewardRepository {
    @Override
    public void addReward(Reward reward, OnSaveRewardCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Reward transferReward = reward;

        realm.executeTransaction((Realm db) -> {
            Reward realmReward = db.createObject(Reward.class, reward.getId());
            realmReward.setMoney(transferReward.getMoney());
            realmReward.setPoints(transferReward.getPoints());
            realmReward.setType(transferReward.getType());
            realmReward.setRewardJudgements(transferReward.getRewardJudgements());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public Reward getRewardReturnedByJudgment(Judgement judgement) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(Reward.class).equalTo("rewardJudgements.id", judgement.getId()).findFirst();
    }

    @Override
    public void clearAllRewards() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Reward.class).findAll().deleteAllFromRealm());
    }
}
