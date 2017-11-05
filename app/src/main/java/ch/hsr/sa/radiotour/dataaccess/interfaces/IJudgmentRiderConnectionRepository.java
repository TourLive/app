package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;

public interface IJudgmentRiderConnectionRepository {

    interface OnSaveJudgmentRiderConnectionCallback {
        void onSuccess();
        void onError(String message);
    }

    void addJudgmentRiderConnection(JudgmentRiderConnection rewardRiderConnection, OnSaveJudgmentRiderConnectionCallback callback);

    void clearAllJudgmentRiderConnections();
}
