package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public interface IMaillotRepository {
    void addMaillot(Maillot maillot, OnSaveMaillotCallback callback);

    void getAllMaillots(OnGetAllMaillotsCallback callback);

    RealmList<Maillot> getAllMaillotsReturned();

    void clearAllMaillots();

    void addRiderToMaillot(Maillot maillot, int riderDbId);

    Maillot getMaillotById(int id);

    interface OnSaveMaillotCallback {
        void onSuccess();

        void onError(String message);
    }

    interface OnGetAllMaillotsCallback {
        void onSuccess(RealmList<Maillot> maillots);

        void onError(String message);
    }
}
