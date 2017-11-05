package ch.hsr.sa.radiotour.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
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

    public static void deleteData(){
        Context.deleteAllRiderStageConnections();
        Context.deleteRiders();
        Context.deleteRaceGroups();
        Context.deleteJudgments();
        Context.deleteRewards();
        Context.deleteStages();
    }

    public static void parseRidersAndPersist(JSONArray riders) throws JSONException, InterruptedException {
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
                        synchronized (this){
                            Context.addRider(rider);
                        }

                        RiderStageConnection riderStageConnection = new RiderStageConnection();
                        riderStageConnection.setBonusPoint(0);
                        riderStageConnection.setBonusTime(0);
                        riderStageConnection.setOfficialGap(new Date(jsonRider.getLong("timeRueckLong")));
                        riderStageConnection.setOfficialTime(new Date(jsonRider.getLong("timeOffLong")));
                        riderStageConnection.setVirtualGap(new Date(jsonRider.getLong("timeVirtLong")));
                        riderStageConnection.setRank(i+1);
                        String state = jsonRider.getString("active");
                        if(state.equals("true")){
                            riderStageConnection.setType(RiderStateType.AKTIVE);
                        } else {
                            riderStageConnection.setType(RiderStateType.DNC);
                        }
                        synchronized (this){
                            Context.addRiderStageConnection(riderStageConnection);
                        }
                        RealmList<RiderStageConnection> riderStageConnections = new RealmList<RiderStageConnection>();
                        riderStageConnections.add(Context.getRiderStageConnectionByRank(i+1));

                        Context.updateRiderStageConnection(rider, riderStageConnections);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        Thread threadRank = updateRiderConnectionRankByOfficalGap();
        threadRank.start();
        threadRank.join();
    }

    private static Thread createDefaultGroup(){
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RaceGroup raceGroupField = new RaceGroup();
                    raceGroupField.setActualGapTime(0);
                    raceGroupField.setHistoryGapTime(0);
                    raceGroupField.setPosition(1);
                    raceGroupField.setType(RaceGroupType.FELD);
                    raceGroupField.setRiders(Context.getAllRiders());
                    Context.addRaceGroup(raceGroupField);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Thread threadGroup = new Thread(runnable);
        return threadGroup;
    }

    private static Thread updateRiderConnectionRankByOfficalGap(){
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    RealmList<RiderStageConnection> connections = Context.getAllRiderStageConnections();
                    HashMap<Long, RiderStageConnection> gapConnectionMap = new HashMap<>();
                    ArrayList<Long> gaps = new ArrayList<>();
                    for(RiderStageConnection con : connections){
                        gapConnectionMap.put(con.getOfficialGap().getTime(), con);
                        gaps.add(con.getOfficialGap().getTime());
                    }
                    gaps.sort(Comparator.naturalOrder());
                    for(int i = 0; i < gaps.size(); i++){
                        RiderStageConnection connection = gapConnectionMap.get(gaps.get(i));
                        Context.updateRiderStageConnectionRank(i+1,connection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Thread threadRank= new Thread(runnable);
        return threadRank;
    }

    public static void parseJudgmentsAndPersist(JSONArray judgments) throws JSONException, InterruptedException {
        final JSONArray judgmentsJson = judgments;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < judgmentsJson.length(); i++) {
                    try {
                        JSONObject jsonJudgment = judgmentsJson.getJSONObject(i);
                        if (jsonJudgment.getInt("etappe") == 8) {
                            Judgement judgment = new Judgement();
                            judgment.setDistance(jsonJudgment.getInt("rennkm"));
                            judgment.setName(jsonJudgment.getString("name"));
                            judgment.setRewardId(jsonJudgment.getInt("rewardId"));
                            Context.addJudgment(judgment);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread threadJudgments = new Thread(runnable);
        threadJudgments.start();
        threadJudgments.join();
    }

    public static void parseRewardsAndPersist(JSONArray rewards) throws JSONException, InterruptedException {
        final JSONArray rewardsJson = rewards;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < rewardsJson.length(); i++) {
                    try {
                        JSONObject jsonReward = rewardsJson.getJSONObject(i);
                        Reward reward = new Reward();

                        RealmList<Integer> moneyList = new RealmList<>();
                        String[] moneyString = jsonReward.getString("reward").split(",");
                        for(String s : moneyString){
                            moneyList.add(Integer.valueOf(s));
                        }
                        reward.setMoney(moneyList);

                        String bonusType = jsonReward.getString("bonusType");
                        if(bonusType.equals("time")) {
                            reward.setType(RewardType.TIME);
                        }
                        if(bonusType.equals("points")){
                            reward.setType(RewardType.POINTS);
                        }

                        RealmList<Integer> bonusList = new RealmList<>();
                        String[] bonusString = jsonReward.getString("bonus").split(",");
                        for(String s : bonusString){
                            bonusList.add(Integer.valueOf(s));
                        }
                        reward.setPoints(bonusList);

                        reward.setRewardId(jsonReward.getInt("id"));

                        reward.setRewardJudgements(Context.getJudgmentsById(reward.getRewardId()));
                        Context.addReward(reward);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread threadRewards = new Thread(runnable);
        threadRewards.start();
        threadRewards.join();
    }

    public static void parseStagesAndPersist(JSONArray stages) throws JSONException, InterruptedException {
        final JSONArray stagesJson = stages;
        Runnable runnable = new Runnable() {
            public void run() {
                    try {
                        JSONObject jsonStage = stagesJson.getJSONObject(stagesJson.length() - 1);
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
                        Context.addStage(stage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        };
        Thread threadRewards = new Thread(runnable);
        threadRewards.start();
        threadRewards.join();
    }



}
