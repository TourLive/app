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
    private static int STAGE_NR = 0;

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

    public static String deleteData(){
        clearDatabase();
        return getActualRaceId(UrlLink.GLOBALSETTINGS, null);
    }

    public static String getRiders() {
        return getRiders(UrlLink.RIDERS + STAGE_ID, null);
    }

    public static String getJudgments() {
        return getJudgments(UrlLink.JUDGEMENTS + RACE_ID, null);
    }

    public static String getRewards() {
        return getRewards(UrlLink.JUDGEMENTS + RACE_ID, null);
    }

    public static String getStages() {
        return getStages(UrlLink.STAGES + RACE_ID, null);
    }

    public static void clearDatabase(){
        Parser.deleteData();
    }

    public static String getActualRaceId(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray settings) {
                try{
                    STAGE_ID = settings.getJSONObject(0).getString("parameter");
                    RACE_ID = settings.getJSONObject(1).getString("parameter");
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders) {
                messages[0] = throwable.getMessage();
            }
        });
        return messages[0];
    }

    public static String getRiders(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseRidersAndPersist(data.getJSONArray("data"));
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                } catch (InterruptedException ex) {
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                messages[0] = throwable.getMessage();
            }
        });
        return messages[0];
    }

    public static String getJudgments(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseJudgmentsAndPersist(data.getJSONObject("data").getJSONArray("judgements"), STAGE_NR);
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                } catch (InterruptedException ex) {
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                messages[0] = throwable.getMessage();
            }
        });
        return messages[0];
    }

    public static String getRewards(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseRewardsAndPersist(data.getJSONObject("data").getJSONArray("rewards"));
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                } catch (InterruptedException ex) {
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                messages[0] = throwable.getMessage();
            }
        });
        return messages[0];
    }

    public static String getStages(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray stages) {
                try{
                    for(int i = 0; i < stages.length(); i++){
                        JSONObject stage = stages.getJSONObject(i);
                        if(stage.getInt("stageId") ==  Integer.valueOf(STAGE_ID)){
                            STAGE_NR = i; // gets the second last stage, cause of data leak on API
                        }
                    };
                    Parser.parseStagesAndPersist(stages, STAGE_NR);
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                } catch (InterruptedException ex) {
                    messages[0] = ex.getMessage();
                }

            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                messages[0] = throwable.getMessage();
            }
        });
        return messages[0];
    }
}
