/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<earthquake>>{
    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";


    public static final String LOG_TAG = "EARTHQUAKE ACTIVITY";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    static QuakeAdapter quakeAdapter;
    ListView earthquakeListView;
    TextView emptyStateView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.

         emptyStateView = (TextView) findViewById(R.id.empty_view);
         earthquakeListView = (ListView) findViewById(R.id.list);
         earthquakeListView.setEmptyView(emptyStateView);
         progressBar = (ProgressBar) findViewById(R.id.prgress_bar) ;






        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.v(LOG_TAG, "about to start initLoader ");

        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    private void updateUI(List<earthquake> earthquakes)
    {
        quakeAdapter = new QuakeAdapter(this,  (ArrayList<earthquake>) earthquakes);
        earthquakeListView.setAdapter(quakeAdapter);
        Log.v(LOG_TAG, "UI Updated! ");


    }

    @Override
    public Loader<List<earthquake>> onCreateLoader(int i, Bundle bundle) {




        Log.v(LOG_TAG, "Created a loader! ");


        return new EarthquakeLoader(this, REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<earthquake>> loader, List<earthquake> earthquakes) {
        // Clear the adapter of previous earthquake data
          emptyStateView.setText("No earthquakes to show");
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected)
            emptyStateView.setText("No internet connection");
          progressBar.setVisibility(View.GONE);

        Log.v(LOG_TAG, "Loading done!");
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            Log.v(LOG_TAG, "about to update UI");

            updateUI(earthquakes);
        }
    }



    @Override
    public void onLoaderReset(Loader<List<earthquake>> loader) {
        Log.v(LOG_TAG, "Loader has been reset ");
        quakeAdapter.clear();


    }



}

