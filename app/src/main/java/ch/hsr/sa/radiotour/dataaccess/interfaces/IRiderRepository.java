package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

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

    void clearAllRiders();
}
