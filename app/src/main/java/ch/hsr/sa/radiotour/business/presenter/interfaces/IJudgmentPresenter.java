package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public interface IJudgmentPresenter extends IBasePresenter {
    void addJudgment(Judgement judgement);

    void clearAllJudgments();

    void getAllJudgments();

    RealmList<Judgement> getJudgmentsById(int judgmentId);

    Judgement getJudgmentByObjectIdReturned(String id);
}
