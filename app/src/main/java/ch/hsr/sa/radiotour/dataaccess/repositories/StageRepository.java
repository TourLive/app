package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IStageRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import io.realm.Realm;

public class StageRepository implements IStageRepository {
    @Override
    public void addStage(final Stage stage, OnSaveStageCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.executeTransaction((Realm db) -> {
            Stage realmStage = db.createObject(Stage.class, UUID.randomUUID().toString());
            realmStage.setStageId(stage.getStageId());
            realmStage.setDistance(stage.getDistance());
            realmStage.setEndTime(stage.getEndTime());
            realmStage.setStartTime(stage.getStartTime());
            realmStage.setFrom(stage.getFrom());
            realmStage.setTo(stage.getTo());
            realmStage.setName(stage.getName());
            realmStage.setType(stage.getType());
            realmStage.setStageConnections(stage.getStageConnections());
        });

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void clearAllStages() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.executeTransaction((Realm db) -> {
            db.where(Stage.class).findAll().deleteAllFromRealm();
        });
    }
}
