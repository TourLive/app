package ch.hsr.sa.radiotour.controller.api;

import android.os.Handler;
import android.os.Looper;
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
    private APIClient() {
        throw new IllegalStateException("Utility class");
    }

    private static final String BASE_URL = "https://tlng.cnlab.ch/";
    private static String raceId = "";
    private static String stageId = "";
    private static int stageNr = 0;
    private static Handler uiHandler;
    private static boolean demoMode = false;

    private static SyncHttpClient client = new SyncHttpClient();

    public static void setDemoMode(boolean demoMode){
        APIClient.demoMode = demoMode;
    }

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
        return getRiders(UrlLink.RIDERS + stageId, null);
    }

    public static String getJudgments() {
        return getJudgments(UrlLink.JUDGEMENTS + raceId, null);
    }

    public static String getRewards() {
        return getRewards(UrlLink.JUDGEMENTS + raceId, null);
    }

    public static String getStages() {
        return getStages(UrlLink.STAGES + raceId, null);
    }

    public static String getMaillots() {
        return getMaillots(UrlLink.MAILLOTS + raceId, null);
    }

    public static void clearDatabase(){
        Parser.deleteData();
    }

    public static void postData(String url, RequestParams params){
        if(!demoMode) {
            postToAPI(url, params);
        }
    }

    public static<T> T getDataFromAPI(String url, RequestParams params){
        if(!demoMode){
            return getStateFromAPI(url, params);
        } else {
            return null;
        }
    }

    public static String getActualRaceId(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray settings) {
                try{
                    stageId = settings.getJSONObject(0).getString("parameter");
                    raceId = settings.getJSONObject(1).getString("parameter");
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
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {
                // Not needed and therefore not implemented
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
                    Parser.parseJudgmentsAndPersist(data.getJSONObject("data").getJSONArray("judgements"), stageNr);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {
                // Not needed and therefore not implemented
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
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {
                // Not needed and therefore not implemented
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
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray stages) {
                try{
                    for(int i = 0; i < stages.length(); i++){
                        JSONObject stage = stages.getJSONObject(i);
                        if(stage.getInt("stageId") ==  Integer.valueOf(stageId)){
                            stageNr = i; // gets the second last stage, cause of data leak on API
                        }
                    }
                    Parser.parseStagesAndPersist(stages, stageNr);
                    messages[0] = "success";
                } catch (Exception ex){
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

    public static String getMaillots(String url, RequestParams params) {
        final String[] messages = {"success"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray maillots) {
                try{
                    Parser.parseMaillotsAndPersist(maillots);
                    messages[0] = "success";
                } catch (Exception ex){
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

    public static void postToAPI(String url, RequestParams params) {
        Looper.prepare();
        uiHandler =  new Handler();
        APIClient.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    uiHandler.post(() -> {new String("successfully");});
                } catch (Exception ex){
                    uiHandler.post(() -> ex.getMessage());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                } catch (Exception ex){
                    uiHandler.post(() -> ex.getMessage());
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                uiHandler.post(() -> throwable.getMessage());
            }
        });
    }

    public static<T> T getStateFromAPI(String url, RequestParams params) {
        Looper.prepare();
        uiHandler =  new Handler();
        T[] response = (T[])new Object[1];
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    response[0] = (T)data;
                } catch (Exception ex){
                    Log.d("here", "here");
                    uiHandler.post(() -> ex.getMessage());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    response[0] = (T)data;
                } catch (Exception ex){
                    Log.d("here", "here");
                    uiHandler.post(() -> ex.getMessage());
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject data){
                Log.d("here", "here");
                uiHandler.post(() -> throwable.getMessage());
            }
        });
        return response[0];
    }
}
