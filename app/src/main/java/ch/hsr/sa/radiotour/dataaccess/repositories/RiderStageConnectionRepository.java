package ch.hsr.sa.radiotour.dataaccess.repositories;

import android.util.Log;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RiderStageConnectionRepository implements IRiderStageConnectionRepository {
    @Override
    public void addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RiderStageConnection transferRiderStateConnection = riderStageConnection;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RiderStageConnection realmRiderStageConnection = realm.createObject(RiderStageConnection.class, UUID.randomUUID().toString());
                realmRiderStageConnection.setBonusPoint(transferRiderStateConnection.getBonusPoint());
                realmRiderStageConnection.setBonusTime(transferRiderStateConnection.getBonusTime());
                realmRiderStageConnection.setOfficialGap(transferRiderStateConnection.getOfficialGap());
                realmRiderStageConnection.setOfficialTime(transferRiderStateConnection.getOfficialTime());
                realmRiderStageConnection.setRank(transferRiderStateConnection.getRank());
                realmRiderStageConnection.setVirtualGap(transferRiderStateConnection.getVirtualGap());
                realmRiderStageConnection.setType(transferRiderStateConnection.getType());
            }
        });

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void getRiderStageConnections(OnGetAllRiderStageConnectionsCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> results = realm.where(RiderStageConnection.class).findAll();
        RealmList<RiderStageConnection> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    @Override
    public void clearAllRiderStageConnection() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RiderStageConnection.class).findAll().deleteAllFromRealm();
            }
        });
    }

    @Override
    public void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RiderStageConnection res = realm.where(RiderStageConnection.class).equalTo("id", newRiderStageConnection.getId()).findFirst();
                res.setBonusPoint(newRiderStageConnection.getBonusPoint());
                res.setBonusTime(newRiderStageConnection.getBonusTime());
                res.setVirtualGap(newRiderStageConnection.getVirtualGap());
                res.setOfficialGap(newRiderStageConnection.getOfficialGap());
                res.setOfficialTime(newRiderStageConnection.getOfficialTime());
                res.setRank(newRiderStageConnection.getRank());
            }
        });
    }

    @Override
    public void updateRiderState(final RiderStateType type, final Rider rider, OnUpdateRiderStateCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Rider res = realm.where(Rider.class).equalTo("startNr", rider.getStartNr()).findFirst();
                for(RiderStageConnection sC : res.getRiderStages()){
                    sC.setType(type);
                }
            }
        });
    }

    @Override
    public void deleteRiderStageConnection() {

    }
}
