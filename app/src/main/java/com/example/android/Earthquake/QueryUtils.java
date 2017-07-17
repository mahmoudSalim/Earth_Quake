package com.example.android.Earthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud on 24/02/17.
 */

public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    private static final String TAG = "TEST";

    public static List<EarthquakeData> fetchEarthquakeData(String requestUrl) {

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        URL url = null;
        String jsonResponse = null;
        List<EarthquakeData> mEarthquakesList = null;

        Log.i(TAG, "TEST: Start fetch earthquake data");

        try {
            url = creatUrl(requestUrl);
            Log.i(TAG, "TEST: fetch earthquake data -> url created");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        jsonResponse = makeHttpRequest(url);
        mEarthquakesList = extractFeatureFromJson(jsonResponse);
        Log.i(TAG, "TEST: End fetch earthquake data");
        return mEarthquakesList;
    }


    private static URL creatUrl(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        return url;
    }

    private static String makeHttpRequest(URL url) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String result = "";

        if(url == null){
            return result;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setConnectTimeout(100000);
        urlConnection.setReadTimeout(150000);
        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                result = readFromInputStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static String readFromInputStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader;

        if(inputStream != null) {
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        }

        bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferedReader.readLine();
            while (line !=null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    private static List<EarthquakeData> extractFeatureFromJson(String earthquakeJson) {
        List<EarthquakeData> earthquakesList = new ArrayList<>();

        if(TextUtils.isEmpty(earthquakeJson)) {
            return null;
        }


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(earthquakeJson);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JSONArray features = null;
        try {
            features = jsonObject.getJSONArray("features");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        int i=0;
            double magnitude=0.0;
            String place = "";
            long date=0;
            String url;

            for(i=0; i<features.length(); i++){
                JSONObject currentEarthquake = null;
                try {
                    currentEarthquake = features.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject properties = null;
                try {
                    properties = currentEarthquake.getJSONObject("properties");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                magnitude = properties.optDouble("mag");
                place = properties.optString("place");
                date = properties.optLong("time");
                url = properties.optString("url");

                earthquakesList.add(new EarthquakeData(magnitude, place, date, url));
            }

        return earthquakesList;
    }

}
