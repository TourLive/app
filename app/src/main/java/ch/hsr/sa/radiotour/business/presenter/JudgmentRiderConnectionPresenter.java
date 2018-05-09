package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IJudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import io.realm.RealmList;

public class JudgmentRiderConnectionPresenter implements IJudgmentRiderConnectionPresenter {
    private static JudgmentRiderConnectionPresenter instance = null;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private JudgmentRiderConnectionRepository judgmentRiderConnectionRepository = new JudgmentRiderConnectionRepository();

    private IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback onSaveJudgmentRiderConnectionCallback;

    public static JudgmentRiderConnectionPresenter getInstance() {
        if (instance == null) {
            instance = new JudgmentRiderConnectionPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag) {
        this.fragments.add(frag);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveJudgmentRiderConnectionCallback = new IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback() {
            @Override
            public void onSuccess() {
                for (Fragment frag : fragments) {
                    if (frag instanceof SpecialFragment) {
                        SpecialFragment specialFragment = (SpecialFragment) frag;
                        specialFragment.updateList();
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveJudgmentRiderConnectionCallback = null;
    }

    @Override
    public void addJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection) {
        judgmentRiderConnectionRepository.addJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
    }

    @Override
    public void addJudgmentRiderConnectionImport(JudgmentRiderConnection judgmentRiderConnection) {
        judgmentRiderConnectionRepository.addJudgmentRiderConnectionImport(judgmentRiderConnection);
    }

    @Override
    public void deleteJudgmentRiderConnection(JudgmentRiderConnection judgmentRiderConnection) {
        judgmentRiderConnectionRepository.deleteJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
    }

    @Override
    public RealmList<JudgmentRiderConnection> getJudgmentRiderConnectionsReturnedByJudgment(Judgement judgement) {
        return judgmentRiderConnectionRepository.getJudgmentRiderConnectionsReturnedByJudgment(judgement);
    }

    @Override
    public void clearAllJudgmentRiderConnections() {
        judgmentRiderConnectionRepository.clearAllJudgmentRiderConnections();
    }
}
