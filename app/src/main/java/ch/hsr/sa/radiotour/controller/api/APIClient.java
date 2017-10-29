package ch.hsr.sa.radiotour.controller.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import ch.hsr.sa.radiotour.business.Context;
import ch.hsr.sa.radiotour.business.Parser;
import cz.msebera.android.httpclient.Header;

public final class APIClient {
    private static final String BASE_URL = "https://tlng.cnlab.ch/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void importData() throws JSONException{
        clearDatabase();
        getRiders(UrlLink.RIDERS, null);
        getJudgments(UrlLink.JUDGEMENTS, null);
        getRewards(UrlLink.JUDGEMENTS, null);
    }

    public static void clearDatabase(){
        Parser.deleteData();
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
}
