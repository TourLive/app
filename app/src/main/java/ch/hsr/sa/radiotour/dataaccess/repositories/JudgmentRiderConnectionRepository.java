package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import io.realm.Realm;

public class JudgmentRiderConnectionRepository implements IJudgmentRiderConnectionRepository {
    @Override
    public void addJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection, OnSaveJudgmentRiderConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final JudgmentRiderConnection transferConnection = judgmentRiderConnection;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                JudgmentRiderConnection realmConnection= realm.createObject(JudgmentRiderConnection.class, UUID.randomUUID().toString());
                realmConnection.setRank(transferConnection.getRank());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllJudgmentRiderConnections() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(JudgmentRiderConnection.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
