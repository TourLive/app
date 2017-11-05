package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import io.realm.RealmList;

public interface IJudgmentRiderConnectionRepository {

    interface OnSaveJudgmentRiderConnectionCallback {
        void onSuccess();
        void onError(String message);
    }

    void addJudgmentRiderConnection(JudgmentRiderConnection rewardRiderConnection, OnSaveJudgmentRiderConnectionCallback callback);
    RealmList<JudgmentRiderConnection> getJudgmentRiderConnectionsReturnedByJudgment(Judgement judgement);
    void clearAllJudgmentRiderConnections();
}
