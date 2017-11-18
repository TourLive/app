package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Stage;

public interface IStageRepository {

    interface OnSaveStageCallback {
        void onSuccess(Stage stage);
        void onError(String message);
    }

    void addStage(Stage stage, OnSaveStageCallback callback);
    void clearAllStages();
    Stage getStage();
}
