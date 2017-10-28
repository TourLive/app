package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;

public interface IJudgmentPresenter extends IBasePresenter {
    void addJudgment(Judgement judgement);
    void clearAllJudgments();
}
