package ch.hsr.sa.radiotour.dataaccess.repositories;

import java.util.Iterator;
import java.util.UUID;

import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.api.PostHandler;
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
    public void addRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());

        realm.beginTransaction();
        RealmResults<RaceGroup> rGtoUpdate = realm.where(RaceGroup.class).greaterThanOrEqualTo("position", raceGroup.getPosition()).findAllSorted("position");
        for (RaceGroup rG : rGtoUpdate) {
            rG.setPosition(rG.getPosition() + 1);
            rG.setActualGapTime(rG.getActualGapTime() + 1);
            if (rG.getType() == RaceGroupType.LEAD) {
                rG.setType(RaceGroupType.NORMAL);
            }
        }
        realm.commitTransaction();

        for (Rider r : raceGroup.getRiders()) {
            RealmResults<RaceGroup> resRG = realm.where(RaceGroup.class).equalTo("riders.id", r.getId()).findAll();
            if (!resRG.isEmpty()) {
                Iterator<RaceGroup> iterator = resRG.iterator();
                while (iterator.hasNext()) {
                    RaceGroup rG = iterator.next();
                    realm.beginTransaction();
                    rG.removeRider(r);
                    realm.commitTransaction();
                    if (rG.getRiders().isEmpty())
                        deleteRaceGroup(rG);
                }
            }
        }

        realm.beginTransaction();

        if (raceGroup.getId() == "" || raceGroup.getId() == null) {
            raceGroup.setId(UUID.randomUUID().toString());
        }

        RaceGroup realmRaceGroup = realm.createObject(RaceGroup.class, raceGroup.getId());
        realmRaceGroup.setType(raceGroup.getType());
        realmRaceGroup.setActualGapTime(raceGroup.getActualGapTime());
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
            callback.onSuccess(realmRaceGroup);
            RealmResults<RaceGroup> results = realm.where(RaceGroup.class).findAll();
            PostHandler.makeMessage("UpdateRaceGroups", realm.copyFromRealm(results));
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

    @Override
    public  RaceGroup getLeadRaceGroup(){
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        return realm.where(RaceGroup.class).equalTo("type", RaceGroupType.LEAD.toString()).findFirst();
    }

    public void updateRaceGroupRiders(RaceGroup raceGroup, final RealmList<Rider> newRiders, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        RiderRepository riderRepository = new RiderRepository();
        RealmList<Rider> riders = new RealmList<>();
        riders.addAll(newRiders);
        long timeRaceGroup = 0L;

        realm.beginTransaction();

        RaceGroup realmRemoveGroup = realm.where(RaceGroup.class).equalTo("riders.id", newRiders.get(0).getId()).findFirst();
        if (realmRemoveGroup != null) {
            for (Rider r : riders) {
                realmRemoveGroup.removeRider(r);
            }
        }
        realm.commitTransaction();

        if (raceGroup.getType() == RaceGroupType.FELD) {
            Iterator<Rider> iterator = riders.iterator();
            while (iterator.hasNext()) {
                Rider r = iterator.next();
                if (r.isUnknown()) {
                    riderRepository.removeRider(r, null);
                    iterator.remove();
                }
            }
        }

        realm.beginTransaction();
        RaceGroup realmRaceGroup = realm.where(RaceGroup.class).equalTo("type", raceGroup.getType().toString()).equalTo("position", raceGroup.getPosition()).findFirst();

        if (!riders.isEmpty())
            realmRaceGroup.appendRiders(riders);

        realm.commitTransaction();

        if (realmRemoveGroup != null && realmRemoveGroup.getRiders().isEmpty()) {
            deleteRaceGroup(realmRemoveGroup);
            realm.beginTransaction();
            timeRaceGroup = realmRaceGroup.getActualGapTime();
            realmRaceGroup.setHistoryGapTime(timeRaceGroup);
            realmRaceGroup.setActualGapTime(0);
            RealmResults<RaceGroup> posToUpdate = realm.where(RaceGroup.class).findAllSorted("position");
            for (RaceGroup r : posToUpdate) {
                r.setPosition(posToUpdate.indexOf(r));
            }
            realm.commitTransaction();


        }

        if (callback != null) {
            callback.onSuccess(realmRaceGroup);
            RealmResults<RaceGroup> results = realm.where(RaceGroup.class).findAll();
            PostHandler.makeMessage("UpdateRaceGroups", realm.copyFromRealm(results));
        }

    }

    @Override
    public void updateRaceGroupGapTime(RaceGroup raceGroup, long timeStamp, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        long timeBefore = realm.where(RaceGroup.class).equalTo("id", raceGroup.getId()).findFirst().getActualGapTime();

        realm.beginTransaction();
        RaceGroup res = realm.where(RaceGroup.class).equalTo("id", raceGroup.getId()).findFirst();
        res.setHistoryGapTime(res.getActualGapTime());
        res.setActualGapTime(timeStamp);
        realm.commitTransaction();

        if (callback != null) {
            RiderStageConnectionPresenter.getInstance().updateRiderStageConnectionTime(timeBefore, timeStamp, res);
            callback.onSuccess(res);
            PostHandler.makeMessage("UpdateRaceGroupTime", realm.copyFromRealm(res));
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
    public void deleteRaceGroup(RaceGroup raceGroup) {
        int pos;
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        RaceGroup realmRemoveGroup = realm.where(RaceGroup.class).equalTo("id", raceGroup.getId()).findFirst();
        pos = realmRemoveGroup.getPosition();
        realmRemoveGroup.deleteFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        RealmResults<RaceGroup> rGtoUpdate = realm.where(RaceGroup.class).greaterThanOrEqualTo("position", pos).findAllSorted("position");
        for (RaceGroup rG : rGtoUpdate) {
            rG.setPosition(rG.getPosition() - 1);
            if (rG.getPosition() == 0 && rG.getType() != RaceGroupType.FELD) {
                rG.setType(RaceGroupType.LEAD);
            }
        }
        realm.commitTransaction();
    }

    @Override
    public void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider, OnUpdateRaceGroupCallBack callback) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        raceGroup.removeRider(rider);
        realm.commitTransaction();

        if (callback != null) {
            callback.onSuccess(raceGroup);
            RealmResults<RaceGroup> results = realm.where(RaceGroup.class).findAll();
            PostHandler.makeMessage("UpdateRaceGroups", realm.copyFromRealm(results));
        }
    }

    @Override
    public void updateRaceGroupPosition(RaceGroup raceGroup, int position) {
        // Only needed for testing
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        raceGroup.setPosition(position);
        if (position == 0 && raceGroup.getType() != RaceGroupType.FELD) {
            raceGroup.setType(RaceGroupType.LEAD);
        } else if (raceGroup.getType() != RaceGroupType.FELD) {
            raceGroup.setType(RaceGroupType.NORMAL);
        }
        realm.commitTransaction();
    }

    @Override
    public RaceGroup getRaceGroupById(String raceGroupId) {
        Realm realm = Realm.getInstance(RadioTourApplication.getInstance());
        realm.beginTransaction();
        RaceGroup raceGroup = realm.where(RaceGroup.class).equalTo("id", raceGroupId).findFirst();
        realm.commitTransaction();
        return raceGroup;
    }
}
