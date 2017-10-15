package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public interface IRaceGroupPresenter extends IBasePresenter {
    void addRaceGroup(RaceGroup raceGroup);
    void getAllRaceGroups();
    void clearAllRaceGroups();
    void updateRaceGroupRiders(RaceGroup raceGroup, RealmList<Rider> newRiders);
    void deleteRaceGroup();
}
