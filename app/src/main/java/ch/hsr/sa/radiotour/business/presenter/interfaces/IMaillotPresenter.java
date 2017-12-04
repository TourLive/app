package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public interface IMaillotPresenter extends IBasePresenter {
    void addMaillot(Maillot maillot);
    void clearAllMaillots();
    void getAllMaillots();
    RealmList<Maillot> getAllMaillotsReturned();
    void addRiderToMaillot(Maillot maillot, int riderDbId);
    Maillot getMaillotById(int id);
}
