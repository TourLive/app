package ch.hsr.sa.radiotour.business.presenter;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IJudgmentPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import io.realm.RealmList;


public class JudgmentPresenter implements IJudgmentPresenter {
    private IJudgmentRepository.OnSaveJudgmentCallback onSaveJudgmentCallback;
    private IJudgmentRepository judgmentRepository;

    public JudgmentPresenter(){
        this.judgmentRepository = new JudgmentRepository();
    }

    @Override
    public void subscribeCallbacks() {
        onSaveJudgmentCallback = new IJudgmentRepository.OnSaveJudgmentCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {

            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveJudgmentCallback = null;
    }


    @Override
    public void addJudgment(Judgement judgement) {
        judgmentRepository.addJudgment(judgement, onSaveJudgmentCallback);
    }

    @Override
    public RealmList<Judgement> getJudgmentsById(int judgmentId){
        return judgmentRepository.getJudgmentsById(judgmentId);
    }

    @Override
    public void clearAllJudgments() {
        judgmentRepository.clearAllJudgments();
    }
}
