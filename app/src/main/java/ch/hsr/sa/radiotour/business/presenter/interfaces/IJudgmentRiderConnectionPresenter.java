package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;

public interface IJudgmentRiderConnectionPresenter extends IBasePresenter {
    void addJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection);
    void clearAllJudgmentRiderConnections();
}
