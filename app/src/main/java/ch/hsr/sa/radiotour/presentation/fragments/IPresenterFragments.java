package ch.hsr.sa.radiotour.presentation.fragments;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public interface IPresenterFragments {
    void addRaceGroupToList();

    void addRiderToList();

    void showRaceGroups(RealmList<RaceGroup> raceGroups);

    void showRiders(RealmList<Rider> riders);

    void updateRiderStateOnGUI(RiderStageConnection connection);
}
