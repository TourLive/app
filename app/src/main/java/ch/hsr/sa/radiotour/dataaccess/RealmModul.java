package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Rider.class, RaceGroup.class, RiderStageConnection.class, Judgement.class, Reward.class, JudgmentRiderConnection.class, Stage.class, Maillot.class})
public class RealmModul {
}
