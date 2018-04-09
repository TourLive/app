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
            Stage realmStage = db.createObject(Stage.class, stage.getId());
            realmStage.setDistance(stage.getDistance());
            realmStage.setEndTime(stage.getEndTime());
            realmStage.setStartTime(stage.getStartTime());
            realmStage.setFrom(stage.getFrom());
            realmStage.setTo(stage.getTo());
            realmStage.setName(stage.getName());
            realmStage.setType(stage.getType());
            realmStage.setStageConnections(stage.getStageConnections());
            realmStage.setMaillotConnections(stage.getMaillotConnections());
            realmStage.setRaceName(stage.getRaceName());
            realmStage.setRaceId(stage.getRaceId());
        });

        Stage dbStage = realm.where(Stage.class).findFirst();

        if (callback != null)
            callback.onSuccess(dbStage);
    }

    public Stage getStage() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(Stage.class).findFirst();
    }

    @Override
    public void updateStageWithRace(String raceName, int raceID) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        Stage realmStage = realm.where(Stage.class).findFirst();
        realmStage.setRaceName(raceName);
        realmStage.setRaceId(raceID);
        realm.commitTransaction();
    }

    public void getStage(OnGetStageCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        Stage stage = realm.where(Stage.class).findFirst();

        if (callback != null) {
            callback.onSuccess(stage);
        }
    }


    @Override
    public void clearAllStages() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> db.where(Stage.class).findAll().deleteAllFromRealm());
    }
}
