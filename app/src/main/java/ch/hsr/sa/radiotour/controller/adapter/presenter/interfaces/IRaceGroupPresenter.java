package ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;

/**
 * Created by Urs Forrer on 13.10.2017.
 */

public interface IRaceGroupPresenter extends IBasePresenter {
    void addRaceGroup(RaceGroup raceGroup);
    void getAllRaceGroups();
    void clearAllRaceGroups();
    void deleteRaceGroup();
}
