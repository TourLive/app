package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;

public interface IRaceGroupPresenter extends IBasePresenter {
    void addRaceGroup(RaceGroup raceGroup);
    void getAllRaceGroups();
    void clearAllRaceGroups();
    void deleteRaceGroup();
}
