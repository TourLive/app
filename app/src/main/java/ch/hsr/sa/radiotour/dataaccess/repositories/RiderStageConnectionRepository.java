package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.List;
import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderStageConnectionRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorMoney;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorMountainPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorTimeInLead;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorVirtualGap;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RiderStageConnectionRepository implements IRiderStageConnectionRepository {
    @Override
    public RiderStageConnection addRiderStageConnection(RiderStageConnection riderStageConnection, OnSaveRiderStageConnectionCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RiderStageConnection transferRiderStateConnection = riderStageConnection;
        RiderStageConnection returnValue;

        realm.beginTransaction();
        RiderStageConnection realmRiderStageConnection = realm.createObject(RiderStageConnection.class, UUID.randomUUID().toString());
        realmRiderStageConnection.setBonusPoint(transferRiderStateConnection.getBonusPoint());
        realmRiderStageConnection.setBonusTime(transferRiderStateConnection.getBonusTime());
        realmRiderStageConnection.setMountainBonusPoints(transferRiderStateConnection.getMountainBonusPoints());
        realmRiderStageConnection.setSprintBonusPoints(transferRiderStateConnection.getSprintBonusPoints());
        realmRiderStageConnection.setOfficialGap(transferRiderStateConnection.getOfficialGap());
        realmRiderStageConnection.setOfficialTime(transferRiderStateConnection.getOfficialTime());
        realmRiderStageConnection.setVirtualGap(transferRiderStateConnection.getVirtualGap());
        realmRiderStageConnection.setType(transferRiderStateConnection.getType());
        realmRiderStageConnection.setMoney(transferRiderStateConnection.getMoney());
        realm.commitTransaction();
        returnValue = realmRiderStageConnection;

        if (callback != null) {
            callback.onSuccess();
        }
        return returnValue;
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

        realm.executeTransaction((Realm db) -> {
            RealmResults<RiderStageConnection> connections = db.where(RiderStageConnection.class).findAll();
            List<RiderStageConnection> cons = realm.copyFromRealm(connections);
            cons.sort(new RiderStageConnectionComparatorPoints());
            for (int i = 0; i < cons.size(); i++) {
                db.where(RiderStageConnection.class).equalTo("id", cons.get(i).getId()).findFirst().getRiderRanking(RankingType.POINTS).setRank(i+1);
            }
            if (riderStageConnection.getMountainBonusPoints() != 0) {
                cons.sort(new RiderStageConnectionComparatorMountainPoints());
                for (int i = 0; i < cons.size(); i++) {
                    db.where(RiderStageConnection.class).equalTo("id", cons.get(i).getId()).findFirst().getRiderRanking(RankingType.MOUNTAIN).setRank(i+1);
                }
            }
            if (riderStageConnection.getMoney() != 0) {
                cons.sort(new RiderStageConnectionComparatorMoney());
                for (int i = 0; i < cons.size(); i++) {
                    db.where(RiderStageConnection.class).equalTo("id", cons.get(i).getId()).findFirst().getRiderRanking(RankingType.MONEY).setRank(i+1);
                }
            }
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
        RealmResults<RiderStageConnection> connections = realm.where(RiderStageConnection.class).findAll();
        List<RiderStageConnection> cons = realm.copyFromRealm(connections);
        cons.sort(new RiderStageConnectionComparatorVirtualGap());
        for (int i = 0; i < cons.size(); i++) {
            RiderRanking riderRanking = realm.where(RiderStageConnection.class).equalTo("id", cons.get(i).getId()).findFirst().getRiderRanking(RankingType.VIRTUAL);
            if(riderRanking != null) {riderRanking.setRank(i+1);}
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
                sC.setType(RiderStateType.ACTIVE);
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
    public void appendTimeInLeadGroup(final RiderStageConnection riderStageConnection, int value){
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.executeTransaction((Realm db) -> {
            RiderStageConnection res = db.where(RiderStageConnection.class).equalTo("id", riderStageConnection.getId()).findFirst();
            res.appendTimeInLeadGroup(value);
            List<RiderStageConnection> cons = realm.copyFromRealm(db.where(RiderStageConnection.class).findAll());
            cons.sort(new RiderStageConnectionComparatorTimeInLead());
            for (int i = 0; i < cons.size(); i++) {
                db.where(RiderStageConnection.class).equalTo("id", cons.get(i).getId()).findFirst().getRiderRanking(RankingType.TIME_IN_LEAD).setRank(i+1);
            }
        });
    }
}
