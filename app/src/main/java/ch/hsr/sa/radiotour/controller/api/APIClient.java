package ch.hsr.sa.radiotour.controller.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        public static void getRiders(String url, RequestParams params) throws JSONException{
            APIClient.get(url, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                    // If the response is JSONObject instead of expected JSONArray
                    try{
                        JSONArray riders = data.getJSONArray("data");
                        JSONObject rider = riders.getJSONObject(0);
                        System.out.println(rider);
                    } catch (Exception ex){
                        Log.d("error", ex.getMessage());
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray riders) {
                    // Pull out the first event on the public timeline
                    try{
                        JSONObject firstEvent = riders.getJSONObject(0);
                        String riderName = firstEvent.getString("name");
                        System.out.println(riderName);
                    } catch (Exception ex){
                        Log.d("error", ex.getMessage());
                    }
                }

                @Override
                public void onFailure(int error, Header[] headers, Throwable throwable, JSONObject riders){
                    Log.d("failure", throwable.getMessage());
                }
            });

        }
}
