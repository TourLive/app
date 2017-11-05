package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public interface IJudgmentRepository {

    interface OnSaveJudgmentCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllJudgmentCallback {
        void onSuccess(RealmList<Judgement> riders);
        void onError(String message);
    }

    void addJudgment(Judgement judgement, OnSaveJudgmentCallback callback);

    void getAllJudgments(OnGetAllJudgmentCallback callback);

    RealmList<Judgement> getJudgmentsById(final int judgmentId);

    void clearAllJudgments();
}
