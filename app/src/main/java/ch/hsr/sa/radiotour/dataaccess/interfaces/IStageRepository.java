package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Stage;

public interface IStageRepository {

    void addStage(Stage stage, OnSaveStageCallback callback);

    void clearAllStages();

    Stage getStage();

    void updateStageWithRace(String raceName, int raceID);

    void getStage(OnGetStageCallback callback);

    interface OnSaveStageCallback {
        void onSuccess(Stage stage);

        void onError(String message);
    }

    interface OnGetStageCallback {
        void onSuccess(Stage stage);

        void onError(String message);
    }
}
