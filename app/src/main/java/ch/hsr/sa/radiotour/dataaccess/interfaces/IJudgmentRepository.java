package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public interface IJudgmentRepository {

    void addJudgment(Judgement judgement, OnSaveJudgmentCallback callback);

    void getAllJudgments(OnGetAllJudgmentCallback callback);

    RealmList<Judgement> getJudgmentsByRewardId(final long rewardId);

    void clearAllJudgments();

    Judgement getJudgmentByObjectIdReturned(final long id);

    interface OnSaveJudgmentCallback {
        void onSuccess();

        void onError(String message);
    }

    interface OnGetAllJudgmentCallback {
        void onSuccess(RealmList<Judgement> riders);

        void onError(String message);
    }
}
