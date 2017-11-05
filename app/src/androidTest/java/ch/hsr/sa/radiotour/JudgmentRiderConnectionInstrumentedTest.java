package ch.hsr.sa.radiotour;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IJudgmentRiderConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.JudgmentRiderConnectionRepository;
import io.realm.Realm;

@RunWith(AndroidJUnit4.class)
public class JudgmentRiderConnectionInstrumentedTest {
    private Realm realm;
    private JudgmentRiderConnectionRepository judgmentRiderConnectionRepository;
    private IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback onSaveJudgmentRiderConnectionCallback;

    @Before
    public void initTestData() {
        this.judgmentRiderConnectionRepository = new JudgmentRiderConnectionRepository();
        realm = Realm.getInstance(RadioTourApplication.getInstance());
        initCallbacks();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(JudgmentRiderConnection.class).findAll().deleteAllFromRealm();
            }
        });

    }

    private void initCallbacks() {
        onSaveJudgmentRiderConnectionCallback = new IJudgmentRiderConnectionRepository.OnSaveJudgmentRiderConnectionCallback() {
            @Override
            public void onSuccess() {
                successFullyAdded();
            }

            @Override
            public void onError(String message) {
            }
        };
    }

    private void successFullyAdded() {
        notifyAll();
    }

    @Test
    public void addRewardRiderConnection(){

        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
        judgmentRiderConnection.setRank(1);

        synchronized (this){
            judgmentRiderConnectionRepository.addJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
        }

        Assert.assertEquals(1, realm.where(JudgmentRiderConnection.class).findAll().first().getRank());
    }

    @Test
    public void clearAllRewardRiderConnections(){
        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
        judgmentRiderConnection.setRank(1);

        synchronized (this){
            judgmentRiderConnectionRepository.addJudgmentRiderConnection(judgmentRiderConnection, onSaveJudgmentRiderConnectionCallback);
        }

        synchronized (this){
            judgmentRiderConnectionRepository.clearAllJudgmentRiderConnections();
        }

        Assert.assertEquals(0, realm.where(JudgmentRiderConnection.class).findAll().size());
    }
}
