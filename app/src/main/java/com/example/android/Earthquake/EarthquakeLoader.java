package com.example.android.Earthquake;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by mahmoud on 03/03/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader {

    String urls = null;
    private static final String TAG = "TEST";

    public EarthquakeLoader(Context context, String urls) {
        super(context);
        this.urls = urls;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i(TAG, "TEST: onStartLoading");
        forceLoad();
    }

    @Override
    public List<EarthquakeData> loadInBackground() {
        if(urls == null){
            Log.i(TAG, "TEST: loadInBackground() -> urls == null");
            return null;
        }

        Log.i(TAG, "TEST: loadInBackground() -> urls != null");
        List<EarthquakeData> result = QueryUtils.fetchEarthquakeData(urls);
        return result;
    }
}
