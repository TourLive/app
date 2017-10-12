package ch.hsr.sa.radiotour.dataaccess;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Dom on 12.10.2017.
 */

public interface IRiderRepository {

    interface OnSaveRiderCallback {
        void onSuccess();
        void onError(String message);
    }

    interface OnGetAllRidersCallback {
        void onSuccess(RealmList<Rider> riders);
        void onError(String message);
    }

    void addRider(Rider rider, OnSaveRiderCallback callback);

    void getAllRiders(OnGetAllRidersCallback callback);
}
