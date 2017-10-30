package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public interface IJudgmentPresenter extends IBasePresenter {
    void addJudgment(Judgement judgement);
    void clearAllJudgments();
    RealmList<Judgement> getJudgmentsById(int judgmentId);
}
