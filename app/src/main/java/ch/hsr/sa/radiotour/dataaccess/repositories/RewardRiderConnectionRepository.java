package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRewardRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import io.realm.Realm;

public class RewardRiderConnectionRepository implements IRewardRiderConnectionRepository {
    @Override
    public void addRewardRiderConnection(RewardRiderConnection rewardRiderConnection, OnSaveRewardRiderConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RewardRiderConnection transferConnection = rewardRiderConnection;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RewardRiderConnection realmConnection= realm.createObject(RewardRiderConnection.class, UUID.randomUUID().toString());
                realmConnection.setRank(transferConnection.getRank());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllRewardRiderConnections() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RewardRiderConnection.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
