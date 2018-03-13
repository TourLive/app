package ch.hsr.sa.radiotour.business;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorMountainPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorOfficalGap;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorVirtualGap;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.models.StageType;
import io.realm.RealmList;

public final class Parser {
    private static String startNr = "startNr";
    private static String country = "country";
    private static String name = "name";
    private static String team = "team";
    private static String teamShort = "teamShort";
    private static String riderID = "riderId";
    private Parser() {
        throw new IllegalStateException("Static class");
    }

    public static void deleteData() {
        Context.deleteAllRiderStageConnections();
        Context.deleteRiders();
        Context.deleteRaceGroups();
        Context.deleteJudgments();
        Context.deleteRewards();
        Context.deleteMaillots();
        Context.deleteStages();
        Context.deleteRiderRankings();
    }

    public static void parseRidersAndPersist(JSONArray riders) throws InterruptedException {
        final JSONArray ridersJson = riders;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < ridersJson.length(); i++) {
                    try {
                        JSONObject jsonRider = ridersJson.getJSONObject(i);

                        Rider rider = new Rider();
                        rider.setStartNr(jsonRider.getInt(startNr));
                        rider.setCountry(jsonRider.getString(country));
                        rider.setName(jsonRider.getString(name));
                        rider.setTeamName(jsonRider.getString(team));
                        rider.setTeamShortName(jsonRider.getString(teamShort));
                        rider.setRiderID(jsonRider.getInt(riderID));
                        synchronized (this) {
                            Context.addRider(rider);
                        }

                        RiderStageConnection riderStageConnection = new RiderStageConnection();
                        riderStageConnection.setBonusPoint(0);
                        riderStageConnection.setBonusTime(0);
                        riderStageConnection.setOfficialGap(jsonRider.getLong("timeRueckLong"));
                        riderStageConnection.setOfficialTime(jsonRider.getLong("timeOffLong"));
                        riderStageConnection.setVirtualGap(jsonRider.getLong("timeVirtLong"));
                        String state = jsonRider.getString("active");
                        if (state.equals("true")) {
                            riderStageConnection.setType(RiderStateType.AKTIVE);
                        } else {
                            riderStageConnection.setType(RiderStateType.DNC);
                        }
                        RiderStageConnection riderStageConnectionOne;
                        synchronized (this) {
                            riderStageConnectionOne = Context.addRiderStageConnection(riderStageConnection);
                        }
                        RealmList<RiderStageConnection> riderStageConnections = new RealmList<>();
                        riderStageConnections.add(riderStageConnectionOne);

                        Context.updateRiderStageConnection(rider, riderStageConnections);

                    } catch (JSONException e) {
                        Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERS - " + e.getMessage());
                    }
                }
            }
        };
        Thread threadRiders = new Thread(runnable);
        threadRiders.start();
        threadRiders.join();
        Thread threadGroup = createDefaultGroup();
        threadGroup.start();
        threadGroup.join();
        updateRiderConnectionRankByOfficalGap();
    }

    private static Thread createDefaultGroup() {
        Runnable runnable = (() -> {
            try {
                RaceGroup raceGroupField = new RaceGroup();
                raceGroupField.setActualGapTime(0);
                raceGroupField.setHistoryGapTime(0);
                raceGroupField.setPosition(1);
                raceGroupField.setType(RaceGroupType.FELD);
                RealmList<Rider> activeRiders = new RealmList<>();
                for (Rider r : Context.getAllRiders()) {
                    if (r.getRiderStages().first().getType() == RiderStateType.AKTIVE) {
                        activeRiders.add(r);
                    }
                }
                raceGroupField.setRiders(activeRiders);
                Context.addRaceGroup(raceGroupField);
            } catch (Exception e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - RACEGROUP - " + e.getMessage());
            }
        });
        return new Thread(runnable);
    }

    private static void updateRiderConnectionRankByOfficalGap() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
            try {
                RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
                connections.sort(new RiderStageConnectionComparatorOfficalGap());
                for (int i = 0; i < connections.size(); i++) {
                    RiderRanking rankingOfficial = new RiderRanking();
                    rankingOfficial.setType(RankingType.OFFICAL);
                    rankingOfficial.setRank(i + 1);
                    synchronized (this){
                        Context.addRiderRanking(rankingOfficial);
                    }
                    RiderRanking realmRiderRanking = Context.getRiderRanking(rankingOfficial);
                    Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                }
                connections.sort(new RiderStageConnectionComparatorVirtualGap());
                for (int i = 0; i < connections.size(); i++) {
                    RiderRanking rankingVirtual = new RiderRanking();
                    rankingVirtual.setType(RankingType.VIRTUAL);
                    rankingVirtual.setRank(i + 1);
                    synchronized (this){
                        Context.addRiderRanking(rankingVirtual);
                    }
                    RiderRanking realmRiderRanking = Context.getRiderRanking(rankingVirtual);
                    Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                }
                connections.sort(new RiderStageConnectionComparatorMountainPoints());
                for (int i = 0; i < connections.size(); i++) {
                    RiderRanking rankingMountain = new RiderRanking();
                    rankingMountain.setType(RankingType.MOUNTAIN);
                    rankingMountain.setRank(i + 1);
                    synchronized (this){
                        Context.addRiderRanking(rankingMountain);
                    }
                    RiderRanking realmRiderRanking = Context.getRiderRanking(rankingMountain);
                    Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                }
                connections.sort(new RiderStageConnectionComparatorPoints());
                for (int i = 0; i < connections.size(); i++) {
                    RiderRanking rankingPoints = new RiderRanking();
                    rankingPoints.setType(RankingType.POINTS);
                    rankingPoints.setRank(i + 1);
                    synchronized (this){
                        Context.addRiderRanking(rankingPoints);
                    }
                    RiderRanking realmRiderRanking = Context.getRiderRanking(rankingPoints);
                    Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                }
                connections.sort(new RiderStageConnectionComparatorVirtualGap());
                int rank = 1;
                for (int i = 0; i < connections.size(); i++) {
                    if(connections.get(i).getRiders().getCountry().equals("SUI")){
                        RiderRanking rankingSwiss = new RiderRanking();
                        rankingSwiss.setType(RankingType.SWISS);
                        rankingSwiss.setRank(rank);
                        rank++;
                        synchronized (this){
                            Context.addRiderRanking(rankingSwiss);
                        }
                        RiderRanking realmRiderRanking = Context.getRiderRanking(rankingSwiss);
                        Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                    }
                }
                for (int i = 0; i < connections.size(); i++) {
                    RiderRanking rankingMoney = new RiderRanking();
                    rankingMoney.setType(RankingType.MONEY);
                    rankingMoney.setRank(i + 1);
                    synchronized (this){
                        Context.addRiderRanking(rankingMoney);
                    }
                    RiderRanking realmRiderRanking = Context.getRiderRanking(rankingMoney);
                    Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                }
            } catch (Exception e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
            }
        }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
    }

    public static void parseJudgmentsAndPersist(JSONArray judgments, final int STAGE_NR) throws InterruptedException {
        final JSONArray judgmentsJson = judgments;
        Runnable runnable = (() -> {
            for (int i = 0; i < judgmentsJson.length(); i++) {
                try {
                    JSONObject jsonJudgment = judgmentsJson.getJSONObject(i);
                    if (jsonJudgment.getInt("etappe") == STAGE_NR) {
                        Judgement judgment = new Judgement();
                        judgment.setDistance(jsonJudgment.getInt("rennkm"));
                        judgment.setName(jsonJudgment.getString("name"));
                        judgment.setRewardId(jsonJudgment.getInt("rewardId"));
                        Context.addJudgment(judgment);
                    }
                } catch (JSONException e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - JUDGMENTS - " + e.getMessage());
                }
            }
        });
        Thread threadJudgments = new Thread(runnable);
        threadJudgments.start();
        threadJudgments.join();
    }

    public static void parseRewardsAndPersist(JSONArray rewards) throws InterruptedException {
        final JSONArray rewardsJson = rewards;
        Runnable runnable = (() -> {
            for (int i = 0; i < rewardsJson.length(); i++) {
                try {
                    JSONObject jsonReward = rewardsJson.getJSONObject(i);
                    Reward reward = new Reward();

                    RealmList<Integer> moneyList = new RealmList<>();
                    String[] moneyString = jsonReward.getString("reward").split(",");
                    for (String s : moneyString) {
                        moneyList.add(Integer.valueOf(s));
                    }
                    reward.setMoney(moneyList);

                    String bonusType = jsonReward.getString("bonusType");
                    if (bonusType.equals("time")) {
                        reward.setType(RewardType.TIME);
                    }
                    if (bonusType.equals("points")) {
                        reward.setType(RewardType.POINTS);
                    }

                    RealmList<Integer> bonusList = new RealmList<>();
                    String[] bonusString = jsonReward.getString("bonus").split(",");
                    for (String s : bonusString) {
                        bonusList.add(Integer.valueOf(s));
                    }
                    reward.setPoints(bonusList);

                    reward.setRewardId(jsonReward.getInt("id"));

                    reward.setRewardJudgements(Context.getJudgmentsById(reward.getRewardId()));
                    Context.addReward(reward);
                } catch (JSONException e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - REWARDS - " + e.getMessage());
                }
            }
        });
        Thread threadRewards = new Thread(runnable);
        threadRewards.start();
        threadRewards.join();
    }

    public static void parseStagesAndPersist(JSONArray stages, final int STAGE_NR) throws InterruptedException {
        final JSONArray stagesJson = stages;
        Runnable runnable = (() -> {
            try {
                JSONObject jsonStage = stagesJson.getJSONObject(STAGE_NR);
                Stage stage = new Stage();
                stage.setStageId(jsonStage.getInt("stageId"));
                stage.setType(StageType.valueOf(jsonStage.getString("stagetype")));
                stage.setName(jsonStage.getString("stageName"));
                stage.setTo(jsonStage.getString("to"));
                stage.setFrom(jsonStage.getString("from"));
                stage.setStartTime(new Date(jsonStage.getLong("starttimeAsTimestamp")));
                stage.setEndTime(new Date(jsonStage.getLong("endtimeAsTimestamp")));
                stage.setDistance(jsonStage.getInt("distance"));
                stage.setStageConnections(Context.getAllRiderStageConnections());
                stage.setMaillotConnections(Context.getAllMaillots());
                JSONObject jsonRace = jsonStage.getJSONObject("race");
                stage.setRaceId(jsonRace.getInt("raceId"));
                stage.setRaceName(jsonRace.getString("raceName"));
                Context.addStage(stage);
            } catch (JSONException e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - STAGES - " + e.getMessage());
            }
        });
        Thread threadRewards = new Thread(runnable);
        threadRewards.start();
        threadRewards.join();
    }

    public static void parseMaillotsAndPersist(JSONArray maillots) throws InterruptedException {
        final JSONArray maillotsJson = maillots;
        Runnable runnable = (() -> {
            for (int i = 0; i < maillotsJson.length(); i++) {
                try {
                    JSONObject jsonMaillot = maillotsJson.getJSONObject(i);
                    Maillot maillot = new Maillot();
                    maillot.setType(jsonMaillot.getString("type"));
                    maillot.setPartner(jsonMaillot.getString("partner"));
                    maillot.setName(jsonMaillot.getString("name"));
                    maillot.setDbIDd(jsonMaillot.getInt("dbId"));
                    maillot.setColor(jsonMaillot.getString("color"));
                    Context.addMaillot(maillot);
                } catch (JSONException e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - MAILLOTS - " + e.getMessage());
                }
            }
        });
        Thread threadMaillots = new Thread(runnable);
        threadMaillots.start();
        threadMaillots.join();
    }

    public static void parseMaillotsRiderConnectionAndPersist(JSONArray maillotsrider) throws InterruptedException {
        final JSONArray maillotsJson = maillotsrider;
        Runnable runnable = (() -> {
            for (int i = 0; i < maillotsJson.length(); i++) {
                try {
                    JSONObject jsonMaillot = maillotsJson.getJSONObject(i);
                    int id = Integer.parseInt(jsonMaillot.getString("jerseyId"));
                    Maillot maillot = Context.getMaillotById(id);
                    Context.addRiderToMaillot(maillot, Integer.parseInt(jsonMaillot.getString("riderId")));
                } catch (JSONException e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - MAILLOTS - " + e.getMessage());
                }
            }
        });
        Thread threadMaillots = new Thread(runnable);
        threadMaillots.start();
        threadMaillots.join();
    }
}
