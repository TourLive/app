package ch.hsr.sa.radiotour.business.presenter.interfaces;


import ch.hsr.sa.radiotour.dataaccess.models.Stage;

public interface IStagePresenter extends IBasePresenter {
    void addStage(Stage stage);
    void clearAllStages();
}