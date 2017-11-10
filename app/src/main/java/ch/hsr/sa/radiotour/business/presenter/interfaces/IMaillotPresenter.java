package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Maillot;

public interface IMaillotPresenter extends IBasePresenter {
    void addMaillot(Maillot maillot);
    void clearAllMaillots();
}
