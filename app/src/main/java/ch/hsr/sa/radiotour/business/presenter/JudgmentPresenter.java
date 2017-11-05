package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IJudgmentPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRepository;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import io.realm.RealmList;


public class JudgmentPresenter implements IJudgmentPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static JudgmentPresenter instance = null;
    private JudgmentRepository judgmentRepository = new JudgmentRepository();

    private IJudgmentRepository.OnSaveJudgmentCallback onSaveJudgmentCallback;
    private IJudgmentRepository.OnGetAllJudgmentCallback onGetAllJudgmentCallback;

    public static JudgmentPresenter getInstance() {
        if(instance == null){
            instance = new JudgmentPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }


    @Override
    public void subscribeCallbacks() {
        onSaveJudgmentCallback = new IJudgmentRepository.OnSaveJudgmentCallback() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {

            }
        };
        onGetAllJudgmentCallback = new IJudgmentRepository.OnGetAllJudgmentCallback() {
            @Override
            public void onSuccess(RealmList<Judgement> judgements) {
                for(Fragment frag : fragments){
                    SpecialFragment specialFragment = (SpecialFragment) frag;
                    specialFragment.showJudgments(judgements);
                }
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
    public void getAllJudgments() {
        judgmentRepository.getAllJudgments(onGetAllJudgmentCallback);
    }

    @Override
    public void clearAllJudgments() {
        judgmentRepository.clearAllJudgments();
    }
}
