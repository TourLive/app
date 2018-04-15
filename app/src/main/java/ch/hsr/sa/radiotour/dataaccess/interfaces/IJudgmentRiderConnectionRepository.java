package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import io.realm.RealmList;

public interface IJudgmentRiderConnectionRepository {

    void addJudgmentRiderConnection(JudgmentRiderConnection rewardRiderConnection, OnSaveJudgmentRiderConnectionCallback callback);

    void addJudgmentRiderConnectionImport(JudgmentRiderConnection rewardRiderConnection);

    RealmList<JudgmentRiderConnection> getJudgmentRiderConnectionsReturnedByJudgment(Judgement judgement);

    void clearAllJudgmentRiderConnections();

    interface OnSaveJudgmentRiderConnectionCallback {
        void onSuccess();

        void onError(String message);
    }
}
