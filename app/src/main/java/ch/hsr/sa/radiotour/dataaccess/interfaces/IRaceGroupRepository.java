package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.controller.adapter.OnStartDragListener;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public interface IRaceGroupRepository {
    interface OnSaveRaceGroupCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllRaceGroupsCallback {
        void onSuccess(RealmList<RaceGroup> raceGroups);
        void onError(String message);
    }

    interface OnUpdateRaceGroupCallBack {
        void onSuccess();
        void onError(String message);
    }

    void addInitialRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback);

    void addRaceGroup(RaceGroup raceGroup, OnSaveRaceGroupCallback callback);

    void getAllRaceGroups(OnGetAllRaceGroupsCallback callback);

    void clearAllRaceGroups();

    void updateRaceGroupRiders(RaceGroup raceGroup, final RealmList<Rider> newRiders, OnUpdateRaceGroupCallBack callback);

    void updateRaceGroupGapTime(RaceGroup raceGroup, long timeStamp, OnUpdateRaceGroupCallBack callback);

    void deleteRaceGroup();

    void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider, OnUpdateRaceGroupCallBack callback);

    void updateRaceGroupPosition(RaceGroup raceGroup, final int position);
}
