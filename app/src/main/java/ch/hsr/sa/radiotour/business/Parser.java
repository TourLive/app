package ch.hsr.sa.radiotour.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public final class Parser {

    private static String startNr = "startNr";
    private static String country = "country";
    private static String name = "name";
    private static String team = "team";
    private static String teamShort = "teamShort";

    public static void deleteData(){
        Context.deleteRiders();
        Context.deleteRaceGroups();
        Context.deleteJudgments();
        Context.deleteRewards();
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
                        Context.addRider(rider);
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

    public static void parseJudgmentsAndPersist(JSONArray judgments) throws JSONException, InterruptedException {
        final JSONArray judgmentsJson = judgments;
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < judgmentsJson.length(); i++) {
                    try {
                        JSONObject jsonJudgment = judgmentsJson.getJSONObject(i);
                        Judgement judgment = new Judgement();
                        judgment.setDistance(jsonJudgment.getInt("rennkm"));
                        judgment.setName(jsonJudgment.getString("name"));
                        judgment.setRewardId(jsonJudgment.getInt("rewardId"));
                        Context.addJudgment(judgment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread threadJudgments = new Thread(runnable);
        threadJudgments.start();
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
                        Context.addReward(reward);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread threadJudgments = new Thread(runnable);
        threadJudgments.start();
    }



}
