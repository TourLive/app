package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IStagePresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IStageRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.repositories.StageRepository;
import ch.hsr.sa.radiotour.presentation.activites.MainActivity;
import ch.hsr.sa.radiotour.presentation.fragments.ImportFragment;

public class StagePresenter implements IStagePresenter {
    private static StagePresenter instance = null;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private StageRepository stageRepository = new StageRepository();

    private IStageRepository.OnSaveStageCallback onSaveStageCallback;
    private IStageRepository.OnGetStageCallback onGetStageCallback;

    public static StagePresenter getInstance() {
        if (instance == null) {
            instance = new StagePresenter();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveStageCallback = new IStageRepository.OnSaveStageCallback() {
            @Override
            public void onSuccess(Stage stage) {
                MainActivity.getInstance().updateStageInfo(stage);
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };

        onGetStageCallback = new IStageRepository.OnGetStageCallback() {
            @Override
            public void onSuccess(Stage stage) {
                for (Fragment frag : fragments) {
                    if (frag instanceof ImportFragment) {
                        ImportFragment fragment = (ImportFragment) frag;
                        fragment.updateActualStage(stage);
                    }
                }
                MainActivity.getInstance().updateStageInfo(stage);
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveStageCallback = null;
        onGetStageCallback = null;
    }


    @Override
    public void addStage(Stage stage) {
        stageRepository.addStage(stage, onSaveStageCallback);
    }

    @Override
    public Stage getStage() {
        return stageRepository.getStage();
    }

    @Override
    public void getStageWithCallback() {
        stageRepository.getStage(onGetStageCallback);
    }

    @Override
    public void clearAllStages() {
        stageRepository.clearAllStages();
    }
}
