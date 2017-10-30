package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IStagePresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IStageRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.repositories.StageRepository;
import io.realm.RealmList;

public class StagePresenter implements IStagePresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static StagePresenter instance = null;
    private StageRepository stageRepository = new StageRepository();

    private IStageRepository.OnSaveStageCallback onSaveStageCallback;

    public static StagePresenter getInstance() {
        if(instance == null){
            instance = new StagePresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }


    @Override
    public void subscribeCallbacks() {
        onSaveStageCallback = new IStageRepository.OnSaveStageCallback() {
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
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveStageCallback = null;
    }


    @Override
    public void addStage(Stage stage) {
        stageRepository.addStage(stage, onSaveStageCallback);
    }

    @Override
    public void clearAllStages() {
        stageRepository.clearAllStages();
    }
}
