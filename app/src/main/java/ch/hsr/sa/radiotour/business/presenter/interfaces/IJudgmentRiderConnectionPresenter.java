package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import io.realm.RealmList;

public interface IJudgmentRiderConnectionPresenter extends IBasePresenter {
    void addJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection);

    void addJudgmentRiderConnectionImport(JudgmentRiderConnection judgmentRiderConnection);

    void clearAllJudgmentRiderConnections();

    RealmList<JudgmentRiderConnection> getJudgmentRiderConnectionsReturnedByJudgment(Judgement judgement);
}
