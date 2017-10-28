package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;
import io.realm.Realm;


public class JudgmentRepository implements IJudgmentRepository {
    @Override
    public void addJudgment(Judgement judgement, OnSaveJudgmentCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Judgement transferJudgment = judgement;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Judgement realmJudgment = realm.createObject(Judgement.class, UUID.randomUUID().toString());
                realmJudgment.setName(transferJudgment.getName());
                realmJudgment.setDistance(transferJudgment.getDistance());
                realmJudgment.setRewardId(transferJudgment.getRewardId());
            }
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllJudgments() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Judgement.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
