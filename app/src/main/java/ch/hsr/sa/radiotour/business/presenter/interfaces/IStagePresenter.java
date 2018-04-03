package ch.hsr.sa.radiotour.business.presenter.interfaces;


import ch.hsr.sa.radiotour.dataaccess.models.Stage;

public interface IStagePresenter extends IBasePresenter {
    void addStage(Stage stage);

    void clearAllStages();

    Stage getStage();

    void getStageWithCallback();

    void updateStageWithRace(String raceName, int raceID);
}
