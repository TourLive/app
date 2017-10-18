package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Rider.class, RaceGroup.class, RiderStageConnection.class})
public class RealmModul {
}
