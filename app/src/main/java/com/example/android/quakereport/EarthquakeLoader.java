package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Pawan on 21-04-2018.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<earthquake>> {

    private static final String LOG_TAG = "EARTHQUAKE LOADER ";
    private String mURL;

    public EarthquakeLoader(Context context, String url) {

        super(context);
        Log.v(LOG_TAG, "Earthquake loader created ");
        mURL = url;

    }

    @Override
    protected void onStartLoading() {

        Log.v(LOG_TAG, "About to force load");
        forceLoad();
    }

    @Override
    public List<earthquake> loadInBackground() {
        if(mURL == null)
            return null;

        Log.v(LOG_TAG, " Started loadInBackground ");
        List<earthquake> earthquakeList = QueryUtils.extractEarthquakes(mURL);

        return earthquakeList;



    }
}
