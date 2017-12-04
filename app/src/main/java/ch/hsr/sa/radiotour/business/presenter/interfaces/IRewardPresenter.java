package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;

public interface IRewardPresenter extends IBasePresenter {
    void addReward(Reward reward);

    Reward getRewardReturnedByJudgment(Judgement judgement);

    void clearAllRewards();
}
