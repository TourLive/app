package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Rider.class, RaceGroup.class})
public class RealmModul {
}
