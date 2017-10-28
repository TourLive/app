package ch.hsr.sa.radiotour.dataaccess;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.SpecialRanking;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Rider.class, RaceGroup.class, RiderStageConnection.class, Judgement.class, Reward.class, RewardRiderConnection.class, SpecialRanking.class})
public class RealmModul {
}
