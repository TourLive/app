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
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public final class APIClient {
    private APIClient() {
        throw new IllegalStateException("Utility class");
    }

    private static final String BASE_URL = "http://prod-api.tourlive.ch/";
    private static final String BASE_URL_CNLAB = "http://tlng.cnlab.ch/";
    private static String raceId = "";
    private static String stageId = "";
    private static int stageNr = 0;
    private static Handler uiHandler;
    private static boolean demoMode = false;
    private static final String readTimeOutMessage = "Die Internetverbindung wurde während der Übertragung unterbrochen, bitte erneut versuchen!\n\n";
    private static final String throwableType = "Read timed out";

    private static SyncHttpClient client = new SyncHttpClient();

    public static void setDemoMode(boolean demoMode){
        APIClient.demoMode = demoMode;
    }

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        responseHandler.setUseSynchronousMode(true);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void getCnLab(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        responseHandler.setUseSynchronousMode(true);
        client.get(getAbsoluteCnlabUrl(url), params, responseHandler);
    }

    private static void put(String url, String content, AsyncHttpResponseHandler responseHandler) {
        //Adidional Parameters for post get
        StringEntity stringEntity = new StringEntity(content, "UTF-8");
        stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.put(null, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
    }

    private static void post(String url, String content, AsyncHttpResponseHandler responseHandler) {
        StringEntity stringEntity = new StringEntity(content, "UTF-8");
        stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(null, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
    }

    private static void delete(String url, AsyncHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteUrl(url), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static String getAbsoluteCnlabUrl(String relativeUrl) {
        return BASE_URL_CNLAB + relativeUrl;
    }

    public static void deleteData(){
        clearDatabase();
    }

    public static String getSettings(){
        return getActualRaceId(UrlLink.GLOBALSETTINGS, null);
    }

    public static String getRiders() {
        return getRiders(UrlLink.RIDERS + stageId , null);
    }

    public static String getJudgments() {
        return getJudgments(UrlLink.JUDGEMENTS + stageId, null);
    }

    public static String getRewards() {
        return getRewards(UrlLink.REWARDS, null);
    }

    public static String getStages() {
        return getStages(UrlLink.STAGES + stageId, null);
    }

    public static String getRace() {
        return getRace(UrlLink.RACE + raceId, null);
    }

    public static String getMaillots() {
        return getMaillots(UrlLink.MAILLOTS + stageId, null);
    }

    public static String getRacegroups() { return getRacegroups(UrlLink.RACEGROUPS + "/stages/" + stageId, null);}

    public static void clearDatabase(){
        Parser.deleteData();
    }

    public static void postRiderStageConnection(long id, String body) {
        String test = body;
        putData(UrlLink.RIDERSTAGECONNECTION + id, body);
    }

    public static void postJudgmentRiderConnection(String body) {
        postData(UrlLink.JUDGMENTRIDERCONNECTION, body);
    }

    public static void putRaceGroup(String id, long stage, String body) {
        putData(UrlLink.RACEGROUPS + "/" + id + "/stages/" + stage, body);
    }

    public static void deleteJudgmentRiderConnection(String id) {
        deleteAPI(UrlLink.JUDGMENTRIDERCONNECTION + "/" + id);
    }

    public static void postRaceGroups(long stage, String body) {
        postData(UrlLink.RACEGROUPS + "/stages/" + stage, body);
    }

    public static void putData(String url, String body){
        if(!demoMode) {
            putToAPI(url, body);
        }
    }

    public static void postData(String url, String body) {
        if (!demoMode) {
            postToAPI(url, body);
        }
    }

    public static void deleteAPI(String url) {
        if (!demoMode) {
            deleteToAPI(url);
        }
    }

    public static<T> T getGPSFromCnlabAPI(String url, RequestParams params){
        if(!demoMode){
            return getGPSDataFromCnlabAPI(url, params);
        } else {
            return null;
        }
    }

    public static<T> T getStatusFromAPI(String url, RequestParams params){
        if(!demoMode){
            return getStatusDataFromAPI(url, params);
        } else {
            return null;
        }
    }

    public static String getActualRaceId(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    stageId = String.valueOf(data.getLong("stageID"));
                    raceId = String.valueOf(data.getLong("raceID"));
                    messages[0] = "success";
                } catch (JSONException ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray settings) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders) {
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getRiders(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {

                try{
                    Parser.parseRidersAndPersist(riders);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getRacegroups(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray racegroups) {

                try{
                    Parser.parseRacegroups(racegroups);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject racegroups){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getJudgments(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    Parser.parseJudgmentsAndPersist(data, stageNr);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getRewards(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    Parser.parseRewardsAndPersist(data);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getStages(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseStagesAndPersist(data);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray stages) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getRace(String url, RequestParams params) {
        final String[] messages = {"error"};
        APIClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    Parser.parseRaceAndPersist(data);
                    messages[0] = "success";
                } catch (Exception ex){
                    messages[0] = ex.getMessage();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray stages) {
                // Not needed and therefore not implemented
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static String getMaillots(String url, RequestParams params) {
        final String[] messages = {"error"};
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
                if(throwable.getMessage().equals(throwableType)){
                    messages[0] = readTimeOutMessage + throwable.getMessage();
                } else {
                    messages[0] = throwable.getMessage();
                }
            }
        });
        return messages[0];
    }

    public static void putToAPI(String url, String body) {
        if(Looper.myLooper() == null)
            Looper.prepare();
        uiHandler =  new Handler();
        APIClient.put(url, body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    uiHandler.post(() -> {new String("successfully");});
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    // Not needed
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                uiHandler.post(throwable::getMessage);
            }
        });
    }

    public static void postToAPI(String url, String body) {
        if(Looper.myLooper() == null)
            Looper.prepare();
        uiHandler =  new Handler();
        APIClient.post(url, body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    uiHandler.post(() -> {new String("successfully");});
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    // Not needed
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                uiHandler.post(throwable::getMessage);
            }
        });
    }

    public static void deleteToAPI(String url) {
        if(Looper.myLooper() == null)
            Looper.prepare();
        uiHandler =  new Handler();
        APIClient.delete(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    uiHandler.post(() -> {new String("successfully");});
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    // Not needed
                } catch (Exception ex){
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                uiHandler.post(throwable::getMessage);
            }
        });
    }

    public static<T> T getGPSDataFromCnlabAPI(String url, RequestParams params) {
        if(Looper.myLooper() == null)
            Looper.prepare();
        uiHandler =  new Handler();
        T[] response = (T[])new Object[1];
        APIClient.getCnLab(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try{
                    response[0] = (T)data;
                } catch (Exception ex){
                    Log.d("here", "here");
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    response[0] = (T)data;
                } catch (Exception ex){
                    Log.d("here", "here");
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject data){
                Log.d("here", "here");
                uiHandler.post(throwable::getMessage);
            }
        });
        return response[0];
    }

    public static<T> T getStatusDataFromAPI(String url, RequestParams params) {
        if(Looper.myLooper() == null)
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
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
                try{
                    response[0] = (T)data;
                } catch (Exception ex){
                    Log.d("here", "here");
                    uiHandler.post(ex::getMessage);
                }
            }

            @Override
            public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject data){
                Log.d("here", "here");
                uiHandler.post(throwable::getMessage);
            }
        });
        return response[0];
    }
}
