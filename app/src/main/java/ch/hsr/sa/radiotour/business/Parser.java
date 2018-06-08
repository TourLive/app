package ch.hsr.sa.radiotour.business;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorDistanceInLead;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorMoney;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorMountainPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorOfficalGap;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorPoints;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorTimeInLead;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnectionComparatorVirtualGap;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.dataaccess.models.StageType;
import io.realm.Realm;
import io.realm.RealmList;

public final class Parser {
    private static String startNr = "startNr";
    private static String country = "country";
    private static String name = "name";
    private static String team = "teamName";
    private static String teamShort = "teamShortName";
    private static String riderID = "id";
    private static String unknown = "unknown";
    private Parser() {
        throw new IllegalStateException("Static class");
    }

    public static void deleteData() {
        Context.deleteAllRiderStageConnections();
        Context.deleteRiders();
        Context.deleteRaceGroups();
        Context.deleteJudgementRiderConnections();
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
                        JSONObject jsonRiderStageConnection = ridersJson.getJSONObject(i);

                        Rider rider = new Rider();
                        JSONObject jsonRider = jsonRiderStageConnection.getJSONObject("rider");
                        rider.setId(jsonRider.getLong(riderID));
                        rider.setStartNr(jsonRider.getInt(startNr));
                        rider.setCountry(jsonRider.getString(country));
                        rider.setName(jsonRider.getString(name));
                        rider.setTeamName(jsonRider.getString(team));
                        rider.setTeamShortName(jsonRider.getString(teamShort));
                        rider.setUnknown(jsonRider.getBoolean(unknown));
                        synchronized (this) {
                            Context.addRider(rider);
                        }

                        RiderStageConnection riderStageConnection = new RiderStageConnection();
                        riderStageConnection.setId(jsonRiderStageConnection.getInt("id"));
                        riderStageConnection.setBonusPoint(jsonRiderStageConnection.getInt("bonusPoints"));
                        riderStageConnection.setBonusTime(jsonRiderStageConnection.getInt("bonusTime"));
                        riderStageConnection.setOfficialGap(jsonRiderStageConnection.getLong("officialGap"));
                        riderStageConnection.setOfficialTime(jsonRiderStageConnection.getLong("officialTime"));
                        riderStageConnection.setVirtualGap(jsonRiderStageConnection.getLong("virtualGap"));
                        riderStageConnection.setType(RiderStateType.valueOf(jsonRiderStageConnection.getString("typeState")));
                        riderStageConnection.setMountainBonusPoints(jsonRiderStageConnection.getInt("mountainBonusPoints"));
                        riderStageConnection.setSprintBonusPoints(jsonRiderStageConnection.getInt("sprintBonusPoints"));
                        riderStageConnection.setMoney(jsonRiderStageConnection.getInt("money"));

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
    }

    public static void parseRacegroups(JSONArray racegroups) throws InterruptedException {
        final JSONArray racegroupsJSON = racegroups;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < racegroupsJSON.length(); i++) {
                    try {
                        JSONObject jsonRacegroup = racegroupsJSON.getJSONObject(i);

                        RaceGroup raceGroup = new RaceGroup();
                        raceGroup.setDbRaceGroupid(jsonRacegroup.getLong("id"));
                        raceGroup.setActualGapTime(jsonRacegroup.getLong("actualGapTime"));
                        raceGroup.setHistoryGapTime(jsonRacegroup.getLong("historyGapTime"));
                        raceGroup.setPosition(jsonRacegroup.getInt("position"));
                        raceGroup.setId(jsonRacegroup.getString("appId"));
                        raceGroup.setType(RaceGroupType.valueOf(jsonRacegroup.getString("raceGroupType")));
                        JSONArray riderInRaceGroup = jsonRacegroup.getJSONArray("riders");
                        RealmList<Rider> riders = new RealmList<>();
                        for (int u = 0; u < riderInRaceGroup.length(); u++) {
                            JSONObject rider = riderInRaceGroup.getJSONObject(u);
                            riders.add(Context.getRiderByStartNr(rider.getInt("startNr")));
                        }
                        raceGroup.setRiders(riders);

                        synchronized (this) {
                            Context.addRaceGroup(raceGroup);
                        }

                    } catch (JSONException e) {
                        Log.d(Parser.class.getSimpleName(), "APP - PARSER - RACEGROUPS - " + e.getMessage());
                    }
                }
            }
        };
        Thread threadRaceGroups = new Thread(runnable);
        threadRaceGroups.start();
        threadRaceGroups.join();
    }

    public static String updateRiderConnectionRankByOfficalGap() throws InterruptedException {
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
            } catch (Exception e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
            }
        }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByVirtualGap() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
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
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByMountainPoints() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
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
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByPoints() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
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
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByBestSwiss() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
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
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByMoney() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
                    connections.sort(new RiderStageConnectionComparatorMoney());
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
        return  "success";
    }

    public static String updateRiderConnectionRankByTimeInLead() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
                    connections.sort(new RiderStageConnectionComparatorTimeInLead());
                    for (int i = 0; i < connections.size(); i++) {
                        RiderRanking rankingTimeInLead = new RiderRanking();
                        rankingTimeInLead.setType(RankingType.TIME_IN_LEAD);
                        rankingTimeInLead.setRank(i + 1);
                        synchronized (this){
                            Context.addRiderRanking(rankingTimeInLead);
                        }
                        RiderRanking realmRiderRanking = Context.getRiderRanking(rankingTimeInLead);
                        Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                    }
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static String updateRiderConnectionRankByDistanceInLead() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
                    connections.sort(new RiderStageConnectionComparatorDistanceInLead());
                    for (int i = 0; i < connections.size(); i++) {
                        RiderRanking rankingTimeInLead = new RiderRanking();
                        rankingTimeInLead.setType(RankingType.DISTANCE_IN_LEAD);
                        rankingTimeInLead.setRank(i + 1);
                        synchronized (this){
                            Context.addRiderRanking(rankingTimeInLead);
                        }
                        RiderRanking realmRiderRanking = Context.getRiderRanking(rankingTimeInLead);
                        Context.updateRiderStageConnectionRanking(realmRiderRanking, connections.get(i));
                    }
                } catch (Exception e) {
                    Log.d(Parser.class.getSimpleName(), "APP - PARSER - RIDERCONNECTION - " + e.getMessage());
                }
            }};
        Thread threadRanking = new Thread(runnable);
        threadRanking.start();
        threadRanking.join();
        return  "success";
    }

    public static void parseJudgmentsAndPersist(JSONArray judgments, final int STAGE_NR) throws InterruptedException {
        final JSONArray judgmentsJson = judgments;
        Runnable runnable = (() -> {
            for (int i = 0; i < judgmentsJson.length(); i++) {
                try {
                    JSONObject jsonJudgment = judgmentsJson.getJSONObject(i);
                    Judgement judgment = new Judgement();
                    judgment.setId(jsonJudgment.getInt("id"));
                    judgment.setDistance(jsonJudgment.getDouble("distance"));
                    judgment.setName(jsonJudgment.getString("name"));
                    judgment.setRewardId(jsonJudgment.getJSONObject("reward").getInt("id"));
                    Context.addJudgment(judgment);
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
                    reward.setId(jsonReward.getInt("id"));

                    RealmList<Integer> moneyList = new RealmList<>();
                    JSONArray moneyArray = jsonReward.getJSONArray("money");
                    for (int m = 0; m < moneyArray.length(); m++) {
                        moneyList.add(Integer.valueOf(moneyArray.getInt(m)));
                    }
                    reward.setMoney(moneyList);

                    String bonusType = jsonReward.getString("rewardType");
                    if (bonusType.equals("TIME")) {
                        reward.setType(RewardType.TIME);
                    }
                    if (bonusType.equals("POINTS")) {
                        reward.setType(RewardType.POINTS);
                    }

                    RealmList<Integer> bonusList = new RealmList<>();
                    JSONArray bonusArray = jsonReward.getJSONArray("points");
                    for (int b = 0; b < bonusArray.length(); b++) {
                        bonusList.add(Integer.valueOf(bonusArray.getInt(b)));
                    }
                    reward.setPoints(bonusList);

                    reward.setRewardJudgements(Context.getJudgmentsByRewardId(reward.getId()));
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

    public static void parseStagesAndPersist(JSONObject jsonStage) throws InterruptedException {
        Runnable runnable = (() -> {
            try {
                Stage stage = new Stage();
                stage.setId(jsonStage.getLong("id"));
                stage.setType(StageType.valueOf(jsonStage.getString("stageType")));
                stage.setName(jsonStage.getString("stageName"));
                stage.setTo(jsonStage.getString("start"));
                stage.setFrom(jsonStage.getString("destination"));
                stage.setStartTime(new Date(jsonStage.getLong("startTime")));
                stage.setEndTime(new Date(jsonStage.getLong("endTime")));
                stage.setDistance(jsonStage.getInt("distance"));
                stage.setStageConnections(Context.getAllRiderStageConnections());
                stage.setMaillotConnections(Context.getAllMaillots());
                Context.addStage(stage);
            } catch (JSONException e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - STAGES - " + e.getMessage());
            }
        });
        Thread threadRewards = new Thread(runnable);
        threadRewards.start();
        threadRewards.join();
    }

    public static void parseJudgementRiderConnections(JSONArray jsonJRC) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < jsonJRC.length(); i++) {
                    try {
                        JSONObject jrC = jsonJRC.getJSONObject(i);
                        JSONObject rider = jrC.getJSONObject("rider");
                        JSONObject judgement = jrC.getJSONObject("judgment");
                        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
                        RealmList<Rider> riderRealmList = new RealmList<>();
                        riderRealmList.add(Context.getRiderByStartNr(rider.getInt("startNr")));
                        judgmentRiderConnection.setRider(riderRealmList);
                        RealmList<Judgement> judgements = new RealmList<>();
                        judgements.add(Context.getJudgmentsById(judgement.getLong("id")));
                        judgmentRiderConnection.setJudgements(judgements);
                        judgmentRiderConnection.setRank(jrC.getInt("rank"));
                        judgmentRiderConnection.setId(jrC.getString("appId"));
                        synchronized (this) {
                            Context.addJudgementRiderConnection(judgmentRiderConnection);
                        }
                    } catch (JSONException e) {
                        Log.d(Parser.class.getSimpleName(), "APP - PARSER - JUDGEMENTRIDERCONNECTION - " + e.getMessage());
                    }
                }
            }};
        Thread t = new Thread(runnable);
        t.start();
        t.join();
    }

    public static void parseRaceAndPersist(JSONObject jsonRace) throws InterruptedException {
        Runnable runnable = (() -> {
            try {
                Context.updateStage(jsonRace.getString("name"), jsonRace.getInt("id"));
            } catch (JSONException e) {
                Log.d(Parser.class.getSimpleName(), "APP - PARSER - RACE - " + e.getMessage());
            }
        });
        Thread threadRace = new Thread(runnable);
        threadRace.start();
        threadRace.join();
    }

    public static void parseMaillotsAndPersist(JSONArray maillots) throws InterruptedException {
        final JSONArray maillotsJson = maillots;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < maillotsJson.length(); i++) {
                    try {
                        JSONObject jsonMaillot = maillotsJson.getJSONObject(i);
                        Maillot maillot = new Maillot();
                        maillot.setType(jsonMaillot.getString("type"));
                        maillot.setPartner(jsonMaillot.getString("partner"));
                        maillot.setName(jsonMaillot.getString("name"));
                        maillot.setId(jsonMaillot.getInt("id"));
                        maillot.setColor(jsonMaillot.getString("color"));
                        synchronized (this) {
                            Context.addMaillot(maillot);
                        }
                        Maillot dbMaillot = Context.getMaillotById(jsonMaillot.getInt("id"));
                        Context.addRiderToMaillot(dbMaillot, Integer.parseInt(jsonMaillot.getString("riderId")));
                    } catch (JSONException e) {
                        Log.d(Parser.class.getSimpleName(), "APP - PARSER - MAILLOTS - " + e.getMessage());
                    }
                }
            }};
        Thread threadMaillots = new Thread(runnable);
        threadMaillots.start();
        threadMaillots.join();
    }
}
