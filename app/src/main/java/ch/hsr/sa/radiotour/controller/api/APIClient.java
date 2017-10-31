package ch.hsr.sa.radiotour.controller.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.sa.radiotour.business.Parser;
import cz.msebera.android.httpclient.Header;

public final class APIClient {
    private static final String BASE_URL = "https://tlng.cnlab.ch/";
    private static String RACE_ID = "";
    private static String STAGE_ID = "";

    private static SyncHttpClient client = new SyncHttpClient();

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        responseHandler.setUseSynchronousMode(true);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        responseHandler.setUseSynchronousMode(true);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void deleteData() throws JSONException{
        clearDatabase();
        getActualRaceId(UrlLink.GLOBALSETTINGS, null);
    }

    public static void getRiders() throws JSONException{
        getRiders(UrlLink.RIDERS + STAGE_ID, null);
    }

    public static void getJudgments() throws JSONException{
        getJudgments(UrlLink.JUDGEMENTS + RACE_ID, null);
    }

    public static void getRewards() throws JSONException{
        getRewards(UrlLink.JUDGEMENTS + RACE_ID, null);
    }

    public static void getStages() throws JSONException{
        getStages(UrlLink.STAGES + RACE_ID, null);
    }

    public static void clearDatabase(){
        Parser.deleteData();
    }

    public static void getActualRaceId(String url, RequestParams params) throws JSONException {
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray settings) {
                try{
                    STAGE_ID = settings.getJSONObject(0).getString("parameter");
                    RACE_ID = settings.getJSONObject(1).getString("parameter");
                } catch (JSONException ex){
                    Log.d("error", ex.getMessage());
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                Log.d("failure", throwable.getMessage());
            }
        });

    }

    public static void getRiders(String url, RequestParams params) throws JSONException{
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseRidersAndPersist(data.getJSONArray("data"));
                } catch (JSONException ex){
                    Log.d("error", ex.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                Log.d("failure", throwable.getMessage());
            }
        });
    }

    public static void getJudgments(String url, RequestParams params) throws JSONException{
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseJudgmentsAndPersist(data.getJSONObject("data").getJSONArray("judgements"));
                } catch (JSONException ex){
                    Log.d("error", ex.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                Log.d("failure", throwable.getMessage());
            }
        });
    }

    public static void getRewards(String url, RequestParams params) throws JSONException{
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseRewardsAndPersist(data.getJSONObject("data").getJSONArray("rewards"));
                } catch (JSONException ex){
                    Log.d("error", ex.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                Log.d("failure", throwable.getMessage());
            }
        });
    }

    public static void getStages(String url, RequestParams params) throws JSONException{
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray stages) {
                try{
                    Parser.parseStagesAndPersist(stages);

                } catch (JSONException ex){
                    Log.d("error", ex.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                Log.d("failure", throwable.getMessage());
            }
        });
    }
}
