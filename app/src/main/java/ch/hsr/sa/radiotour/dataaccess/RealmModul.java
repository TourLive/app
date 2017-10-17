package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderState;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Rider.class, RaceGroup.class, RiderStageConnection.class, RiderState.class})
public class RealmModul {
}
