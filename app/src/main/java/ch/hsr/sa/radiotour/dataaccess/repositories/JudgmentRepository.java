package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class JudgmentRepository implements IJudgmentRepository {
    @Override
    public void addJudgment(Judgement judgement, OnSaveJudgmentCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final Judgement transferJudgment = judgement;
        realm.executeTransaction((Realm db) -> {
            Judgement realmJudgment = db.createObject(Judgement.class, judgement.getId());
            realmJudgment.setName(transferJudgment.getName());
            realmJudgment.setDistance(transferJudgment.getDistance());
            realmJudgment.setRewardId(transferJudgment.getRewardId());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllJudgments(OnGetAllJudgmentCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<Judgement> results = realm.where(Judgement.class).findAll();
        RealmList<Judgement> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    @Override
    public RealmList<Judgement> getJudgmentsById(final long judgmentId) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmList<Judgement> results = new RealmList<>();
        RealmResults<Judgement> judgments = realm.where(Judgement.class).findAll();

        for (Judgement j : judgments) {
            if (j.getRewardId() == judgmentId) {
                results.add(j);
            }
        }

        return results;
    }

    @Override
    public Judgement getJudgmentByObjectIdReturned(final long id) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(Judgement.class).equalTo("id", id).findFirst();
    }

    @Override
    public void clearAllJudgments() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Judgement.class).findAll().deleteAllFromRealm());
    }
}
