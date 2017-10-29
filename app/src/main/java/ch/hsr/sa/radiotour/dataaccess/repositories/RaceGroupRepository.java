package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.UUID;

import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IRaceGroupRepository;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RaceGroupRepository implements IRaceGroupRepository {
    @Override
    public void addInitialRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        final RaceGroup transferRaceGroup = raceGroup;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RaceGroup realmRaceGroup = realm.createObject(RaceGroup.class, UUID.randomUUID().toString());
                realmRaceGroup.setType(transferRaceGroup.getType());
                realmRaceGroup.setActualGapTime(transferRaceGroup.getActualGapTime());
                realmRaceGroup.setHistoryGapTime(transferRaceGroup.getHistoryGapTime());
                RealmList<Rider> res = new RealmList<>();
                for (Rider r : transferRaceGroup.getRiders()) {
                    RealmResults<Rider> temp = realm.where(Rider.class).equalTo("startNr", r.getStartNr()).findAll();
                    res.addAll(temp);
                }
                realmRaceGroup.setPosition(transferRaceGroup.getPosition());
                realmRaceGroup.setRiders(res);
            }
        });

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void addRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.beginTransaction();
        RealmResults<RaceGroup> rGtoUpdate = realm.where(RaceGroup.class).greaterThanOrEqualTo("position", raceGroup.getPosition()).findAllSorted("position");
        for (RaceGroup rG : rGtoUpdate) {
            rG.setPosition(rG.getPosition() + 1);
            if (rG.getType() == RaceGroupType.LEAD) {
                rG.setType(RaceGroupType.NORMAL);
            }
        }

        for (Rider r : raceGroup.getRiders()) {
            RealmResults<RaceGroup> resRG = realm.where(RaceGroup.class).equalTo("riders.id", r.getId()).findAll();
            if (!resRG.isEmpty()) {
                for (RaceGroup rG : resRG) {
                    rG.removeRider(r);
                }
            }
        }

        RaceGroup realmRaceGroup = realm.createObject(RaceGroup.class, UUID.randomUUID().toString());
        realmRaceGroup.setType(raceGroup.getType());
        realmRaceGroup.setActualGapTime(0);
        realmRaceGroup.setHistoryGapTime(0);
        realmRaceGroup.setPosition(raceGroup.getPosition());
        RealmList<Rider> res = new RealmList<>();
        for (Rider r : raceGroup.getRiders()) {
            RealmResults<Rider> temp = realm.where(Rider.class).equalTo("startNr", r.getStartNr()).findAll();
            res.addAll(temp);
        }
        realmRaceGroup.setRiders(res);


        realm.commitTransaction();
        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void getAllRaceGroups(OnGetAllRaceGroupsCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RealmResults<RaceGroup> results = realm.where(RaceGroup.class).findAll();
        RealmList<RaceGroup> res = new RealmList<>();
        res.addAll(results);

        if (callback != null) {
            callback.onSuccess(res);
        }
    }

    public void updateRaceGroupRiders(RaceGroup raceGroup, final RealmList<Rider> newRiders, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.beginTransaction();
        for (Rider r : newRiders) {
            RealmResults<RaceGroup> res = realm.where(RaceGroup.class).equalTo("riders.id", r.getId()).findAll();
            if (!res.isEmpty()) {
                for (RaceGroup rG : res) {
                    rG.removeRider(r);
                }
            }
        }
        realm.commitTransaction();

        realm.beginTransaction();
        RaceGroup realmRaceGroup = realm.where(RaceGroup.class).equalTo("type",raceGroup.getType().toString()).equalTo("position", raceGroup.getPosition()).findFirst();
        realmRaceGroup.appendRiders(newRiders);
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess();
        }

    }

    @Override
    public void updateRaceGroupGapTime(RaceGroup raceGroup, long timeStamp, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.beginTransaction();
        RaceGroup res = realm.where(RaceGroup.class).equalTo("id", raceGroup.getId()).findFirst();
        res.setHistoryGapTime(res.getActualGapTime());
        res.setActualGapTime(timeStamp);
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void clearAllRaceGroups() {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        realm.where(RaceGroup.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void deleteRaceGroup() {

    }

    @Override
    public void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        raceGroup.removeRider(rider);
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void updateRaceGroupPosition(RaceGroup raceGroup, int position) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        raceGroup.setPosition(position);
        if (position == 0) {
            raceGroup.setType(RaceGroupType.LEAD);
        } else {
            raceGroup.setType(RaceGroupType.NORMAL);
        }
        realm.commitTransaction();
    }
}
