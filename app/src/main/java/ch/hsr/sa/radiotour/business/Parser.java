package ch.hsr.sa.radiotour.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public final class Parser {

    private static String startNr = "startNr";
    private static String country = "country";
    private static String name = "name";

    public static void parseRidersAndPersist(JSONArray riders){
        for(int i = 0; i <riders.length(); i++){
            try{
                JSONObject jsonRider = riders.getJSONObject(i);
                Rider rider = new Rider();
                rider.setStartNr(jsonRider.getInt(startNr));
                rider.setCountry(jsonRider.getString(country));
                rider.setName(jsonRider.getString(name));
                Context.addRider(rider);
            } catch (JSONException ex){

            }
        }
    }

}
