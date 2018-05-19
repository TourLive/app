package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.controller.api.PostHandler;
import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.RiderStageConnectionUtilities;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class JudgmentRiderConnectionRepository implements IJudgmentRiderConnectionRepository {
    @Override
    public void addJudgmentRiderConnectionImport(JudgmentRiderConnection judgmentRiderConnection) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final JudgmentRiderConnection transferConnection = judgmentRiderConnection;

        realm.beginTransaction();
        if (transferConnection.getId() == "" || transferConnection.getId() == null) {
            transferConnection.setId(UUID.randomUUID().toString());
        }
        JudgmentRiderConnection realmConnection = realm.createObject(JudgmentRiderConnection.class, transferConnection.getId());
        realmConnection.setRank(transferConnection.getRank());
        realmConnection.setJudgements(transferConnection.getJudgements());
        realmConnection.setRider(transferConnection.getRider());
        realm.commitTransaction();
    }

    @Override
    public void addJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection, OnSaveJudgmentRiderConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final JudgmentRiderConnection transferConnection = judgmentRiderConnection;

        realm.beginTransaction();
        RealmResults<JudgmentRiderConnection> res = realm.where(JudgmentRiderConnection.class).equalTo("rank", judgmentRiderConnection.getRank()).equalTo("judgements.id", judgmentRiderConnection.getJudgements().first().getId()).findAll();

        if (!res.isEmpty()) {
            removeOldJudgmentRiderConnections(res);
        }
        realm.commitTransaction();

        realm.beginTransaction();
        if (judgmentRiderConnection.getId() == "" || judgmentRiderConnection.getId() == null) {
            judgmentRiderConnection.setId(UUID.randomUUID().toString());
        }
        JudgmentRiderConnection realmConnection = realm.createObject(JudgmentRiderConnection.class, judgmentRiderConnection.getId());
        realmConnection.setRank(transferConnection.getRank());
        realmConnection.setJudgements(transferConnection.getJudgements());
        realmConnection.setRider(transferConnection.getRider());
        realm.commitTransaction();

        PostHandler.makeMessage("CreateJudgmentRiderConnection", realm.copyFromRealm(realmConnection));

        if (callback != null)
            callback.onSuccess();
    }

    private void removeOldJudgmentRiderConnections(RealmResults<JudgmentRiderConnection> res) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        for (JudgmentRiderConnection jRc : res) {
            int rank = jRc.getRank();
            Judgement judgement = jRc.getJudgements().first();
            Reward reward = judgement.getRewards();
            RiderStageConnection riderStageConnection = jRc.getRider().first().getRiderStages().first();
            if(reward.getType() == RewardType.POINTS){
                if (judgement.getName().toLowerCase().contains("sprint")) {
                    riderStageConnection.removeSprintBonusPoints(RiderStageConnectionUtilities.getPointsAtPosition(rank, reward));
                    riderStageConnection.removeBonusPoint(RiderStageConnectionUtilities.getPointsAtPosition(rank, reward));
                } else if (judgement.getName().toLowerCase().contains("bergpreis")) {
                    riderStageConnection.removeMountainBonusPoints(RiderStageConnectionUtilities.getPointsAtPosition(rank, reward));
                } else {
                    riderStageConnection.removeBonusPoint(RiderStageConnectionUtilities.getPointsAtPosition(rank, reward));
                }
            }
            if(reward.getType() == RewardType.TIME){
                riderStageConnection.removeBonusTime(RiderStageConnectionUtilities.getPointsAtPosition(rank, reward));
            }
            riderStageConnection.removeMoney(RiderStageConnectionUtilities.getMoneyAtPosition(rank, reward));
            PostHandler.makeMessage("UpdateRiderStageConnection", realm.copyFromRealm(riderStageConnection));
            PostHandler.makeMessage("DeleteJudgmentRiderConnection", realm.copyFromRealm(jRc));
            jRc.deleteFromRealm();

        }
    }

    @Override
    public RealmList<JudgmentRiderConnection> getJudgmentRiderConnectionsReturnedByJudgment(Judgement judgement) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<JudgmentRiderConnection> res = realm.where(JudgmentRiderConnection.class).equalTo("judgements.id", judgement.getId()).findAll();
        RealmList<JudgmentRiderConnection> resList = new RealmList<>();
        resList.addAll(res);
        return resList;
    }

    @Override
    public void deleteJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection, OnSaveJudgmentRiderConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        JudgmentRiderConnection toDeleteJudgmentRiderConnection = realm.copyFromRealm(judgmentRiderConnection);
        realm.where(JudgmentRiderConnection.class).equalTo("id", judgmentRiderConnection.getId()).findFirst().deleteFromRealm();
        realm.commitTransaction();

        PostHandler.makeMessage("DeleteJudgmentRiderConnection", toDeleteJudgmentRiderConnection);

        if (callback != null)
            callback.onSuccess();

    }

    @Override
    public void clearAllJudgmentRiderConnections() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> realm.where(JudgmentRiderConnection.class).findAll().deleteAllFromRealm());
    }
}
