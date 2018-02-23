package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class RiderStageConnectionRepository implements IRiderStageConnectionRepository {
    @Override
    public void addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RiderStageConnection transferRiderStateConnection = riderStageConnection;

        realm.executeTransaction((Realm db) -> {
            RiderStageConnection realmRiderStageConnection = db.createObject(RiderStageConnection.class, UUID.randomUUID().toString());
            realmRiderStageConnection.setBonusPoint(transferRiderStateConnection.getBonusPoint());
            realmRiderStageConnection.setBonusTime(transferRiderStateConnection.getBonusTime());
            realmRiderStageConnection.setMountainBonusPoints(transferRiderStateConnection.getMountainBonusPoints());
            realmRiderStageConnection.setSprintBonusPoints(transferRiderStateConnection.getSprintBonusPoints());
            realmRiderStageConnection.setOfficialGap(transferRiderStateConnection.getOfficialGap());
            realmRiderStageConnection.setOfficialTime(transferRiderStateConnection.getOfficialTime());
            realmRiderStageConnection.setRank(transferRiderStateConnection.getRank());
            realmRiderStageConnection.setVirtualGap(transferRiderStateConnection.getVirtualGap());
            realmRiderStageConnection.setType(transferRiderStateConnection.getType());
            realmRiderStageConnection.setMoney(transferRiderStateConnection.getMoney());
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
        realm.executeTransaction((Realm db) -> db.where(RiderStageConnection.class).findAll().deleteAllFromRealm());
    }

    @Override
    public void updateRiderStageConnection(final RiderStageConnection newRiderStageConnection, final RiderStageConnection oldRiderStageConnection, OnUpdateRiderStageConnectionCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> {
            RiderStageConnection res = db.where(RiderStageConnection.class).equalTo("id", oldRiderStageConnection.getId()).findFirst();
            res.setBonusPoint(newRiderStageConnection.getBonusPoint());
            res.setSprintBonusPoints(newRiderStageConnection.getSprintBonusPoints());
            res.setMountainBonusPoints(newRiderStageConnection.getMountainBonusPoints());
            res.setBonusTime(newRiderStageConnection.getBonusTime());
            res.setVirtualGap(newRiderStageConnection.getVirtualGap());
            res.setOfficialGap(newRiderStageConnection.getOfficialGap());
            res.setOfficialTime(newRiderStageConnection.getOfficialTime());
            res.setRank(newRiderStageConnection.getRank());
            res.setType(newRiderStageConnection.getType());
            res.setMoney(newRiderStageConnection.getMoney());
        });
    }

    @Override
    public void updateRiderStageConnectionReward(final RiderStageConnection riderStageConnection, OnUpdateRiderStageConnectionCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> {
            RiderStageConnection res = db.where(RiderStageConnection.class).equalTo("id", riderStageConnection.getId()).findFirst();
            res.appendBonusPoint(riderStageConnection.getBonusPoint());
            res.appendBonusTime(riderStageConnection.getBonusTime());
            res.appendMoney(riderStageConnection.getMoney());
            res.appendMountainBonusPoints(riderStageConnection.getMountainBonusPoints());
            res.appendSprintBonusPoints(riderStageConnection.getSprintBonusPoints());

        });

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void updateRiderStageConnectionTime(long timeBefore, long timeStamp, final RaceGroup res, OnUpdateRiderStageConnectionCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        if (timeBefore > 0 && !res.getRiders().isEmpty()) {
            for (Rider r : res.getRiders()) {
                if (!r.getRiderStages().isEmpty()) {
                    r.getRiderStages().first().setVirtualGap(r.getRiderStages().first().getVirtualGap() - timeBefore);
                }
            }
        }
        if (!res.getRiders().isEmpty()) {
            for (Rider r : res.getRiders()) {
                if (!r.getRiderStages().isEmpty()) {
                    r.getRiderStages().first().setVirtualGap(r.getRiderStages().first().getVirtualGap() + timeStamp);
                }
            }
        }
        realm.commitTransaction();
        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void updateRiderState(final RiderStateType type, final Rider rider, OnUpdateRiderStateCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        Rider res = realm.where(Rider.class).equalTo("id", rider.getId()).findFirst();
        for (RiderStageConnection sC : res.getRiderStages()) {
            if (sC.getType() == type) {
                sC.setType(RiderStateType.AKTIVE);
            } else {
                sC.setType(type);
            }
        }
        realm.commitTransaction();
        RiderStageConnection state = realm.where(RiderStageConnection.class).equalTo("riders.id", rider.getId()).findFirst();
        if (callback != null)
            callback.onSuccess(state);
    }

    @Override
    public void deleteRiderStageConnection() {
        // Not implemented yet
    }

    @Override
    public RiderStageConnection getRiderByRank(final int rank) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        RiderStageConnection res = realm.where(RiderStageConnection.class).equalTo("rank", rank).findFirst();
        realm.commitTransaction();
        return res;
    }

    @Override
    public RealmList<RiderStageConnection> getAllRiderStateConnections() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> res = realm.where(RiderStageConnection.class).findAll();
        RealmList<RiderStageConnection> resList = new RealmList<>();
        resList.addAll(res);
        return resList;
    }

    @Override
    public void updateRiderStageConnectionRanking(final RiderRanking riderRanking, final RiderStageConnection connection) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> {
            RiderStageConnection res = db.where(RiderStageConnection.class).equalTo("id", connection.getId()).findFirst();
            res.addRiderRanking(riderRanking);
        });
    }

    @Override
    public RealmList<RiderStageConnection> getRiderStageConnectionsSortedByVirtualGap() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> results = realm.where(RiderStageConnection.class).findAll().sort("virtualGap", Sort.ASCENDING);
        RealmList<RiderStageConnection> res = new RealmList<>();
        res.addAll(results);
        realm.close();
        return res;
    }

    @Override
    public RealmList<RiderStageConnection> getRiderStageConnectionsSortedByPoints() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> results = realm.where(RiderStageConnection.class).findAll().sort("sprintBonusPoints", Sort.DESCENDING);
        RealmList<RiderStageConnection> res = new RealmList<>();
        res.addAll(results);
        realm.close();
        return res;
    }

    @Override
    public RealmList<RiderStageConnection> getRiderStageConnectionsSortedByMountain() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> results = realm.where(RiderStageConnection.class).findAll().sort("mountainBonusPoints", Sort.DESCENDING);
        RealmList<RiderStageConnection> res = new RealmList<>();
        res.addAll(results);
        realm.close();
        return res;
    }

    @Override
    public RealmList<RiderStageConnection> getRiderStageConnectionsSortedByBestSwiss() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RiderStageConnection> results = realm.where(RiderStageConnection.class).equalTo("riders.country", "SUI").findAll().sort("virtualGap", Sort.ASCENDING);
        RealmList<RiderStageConnection> res = new RealmList<>();
        res.addAll(results);
        realm.close();
        return res;
    }

    @Override
    public RiderStageConnection getLeader() {
        return getRiderStageConnectionsSortedByVirtualGap().first();
    }

    @Override
    public RiderStageConnection getSprintLeader() {
        return getRiderStageConnectionsSortedByPoints().first();
    }

    @Override
    public RiderStageConnection getMountainLeader() {
        return getRiderStageConnectionsSortedByMountain().first();
    }

    @Override
    public RiderStageConnection getSwissLeader() {
        return getRiderStageConnectionsSortedByBestSwiss().first();
    }

    @Override
    public RiderStageConnection getRiderStageConnectionByStartNr(int startNr){
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        Rider rider = realm.where(Rider.class).equalTo("startNr", startNr).findFirst();
        return rider.getRiderStages().first();
    }


}
