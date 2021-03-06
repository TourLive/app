package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public interface IRaceGroupRepository {
    void addRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback);

    void getAllRaceGroups(OnGetAllRaceGroupsCallback callback);

    RaceGroup getLeadRaceGroup();

    void clearAllRaceGroups();

    void updateRaceGroupRiders(RaceGroup raceGroup, final RealmList<Rider> newRiders, boolean unkownFlag, OnUpdateRaceGroupCallBack callback);

    void updateRaceGroupGapTime(RaceGroup raceGroup, long timeStamp, OnUpdateRaceGroupCallBack callback);

    void deleteRaceGroup(RaceGroup raceGroup);

    void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider, OnUpdateRaceGroupCallBack callback);

    void updateRaceGroupPosition(RaceGroup raceGroup, final int position);

    RaceGroup getRaceGroupById(String raceGroupId);

    interface OnSaveRaceGroupCallback {
        void onSuccess(RaceGroup raceGroup);

        void onError(String message);
    }

    interface OnGetAllRaceGroupsCallback {
        void onSuccess(RealmList<RaceGroup> raceGroups);

        void onError(String message);
    }

    interface OnUpdateRaceGroupCallBack {
        void onSuccess(RaceGroup raceGroup);

        void onError(String message);
    }
}
