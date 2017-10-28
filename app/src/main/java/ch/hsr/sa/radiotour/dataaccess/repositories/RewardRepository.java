package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import io.realm.Realm;


public class RewardRepository implements IRewardRepository {
    @Override
    public void addReward(Reward reward, OnSaveRewardCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Reward transferReward = reward;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Reward realmReward= realm.createObject(Reward.class, UUID.randomUUID().toString());
                realmReward.setMoney(transferReward.getMoney());
                realmReward.setPoints(transferReward.getPoints());
                realmReward.setRewardId(transferReward.getRewardId());
                realmReward.setType(transferReward.getType());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllRewards() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Reward.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
