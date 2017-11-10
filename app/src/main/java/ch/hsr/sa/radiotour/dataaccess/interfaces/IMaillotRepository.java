package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Maillot;

public interface IMaillotRepository {
    interface OnSaveMaillotCallback {
        void onSuccess();
        void onError(String message);
    }

    void addMaillot(Maillot maillot, OnSaveMaillotCallback callback);

    void clearAllMaillots();
}
