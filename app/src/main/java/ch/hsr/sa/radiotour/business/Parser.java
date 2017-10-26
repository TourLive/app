package ch.hsr.sa.radiotour.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public final class Parser {

    private static String startNr = "startNr";
    private static String country = "country";
    private static String name = "name";
    private static String team = "team";
    private static String teamShort = "teamShort";

    public static void parseRidersAndPersist(JSONArray riders) throws JSONException, InterruptedException {
        final JSONArray ridersJson = riders;
        deleteData();
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
                        rider.setShortTeamName(jsonRider.getString(teamShort));
                        Context.addRider(rider);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();


        runnable = new Runnable() {
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
        mythread = new Thread(runnable);
        mythread.start();
    }

    private static void deleteData(){
        Context.deleteRiders();
        Context.deleteRaceGroups();
    }

}
