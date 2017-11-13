package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public interface IMaillotRepository {
    interface OnSaveMaillotCallback {
        void onSuccess();
        void onError(String message);
    }

    void addMaillot(Maillot maillot, OnSaveMaillotCallback callback);
    RealmList<Maillot> getAllMaillots();
    void clearAllMaillots();
}
