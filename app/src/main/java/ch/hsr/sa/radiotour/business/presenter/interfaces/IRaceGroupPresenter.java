package ch.hsr.sa.radiotour.business.presenter.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public interface IRaceGroupPresenter extends IBasePresenter {
    void addRaceGroupWithoutCallback(RaceGroup raceGroup);

    void addRaceGroup(RaceGroup raceGroup);

    void getAllRaceGroups();

    RaceGroup getLeadRaceGroup();

    void clearAllRaceGroups();

    void updateRaceGroupRiders(RaceGroup raceGroup, RealmList<Rider> newRiders);

    void updateRaceGroupGapTime(RaceGroup raceGroup, String minutes, String seconds);

    void deleteRiderInRaceGroup(RaceGroup raceGroup, Rider rider);

    RaceGroup getRaceGroupById(String raceGroupId);
}
