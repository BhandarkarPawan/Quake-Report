package com.example.android.quakereport;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {



    static private URL createURL(String urlString)
    {
        URL url;
        try
        {
            url = new URL(urlString);
        }
        catch (MalformedURLException e)
        {
            Log.e("URL ERROR","Malformed URL");
            return null;
        }
            return url;

    }

    static private String getJSONString( URL url) throws IOException
    {

        String JSONResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if (url==null)
            return JSONResponse;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            }
            else {
                Log.e("Request error: ", "Response code not 200");
                return JSONResponse;
            }

        }
        catch (IOException e)
        {

        }
        finally {
            if (urlConnection!=null)
                urlConnection.disconnect();

            if(inputStream!=null)
                inputStream.close();


        }
        return JSONResponse;
    }

    static private String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if(inputStream!= null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();

    }



    private QueryUtils() {
    }

    /**
     * Return a list of {@link earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<earthquake> extractEarthquakes(String urlString) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        URL url;
        url = createURL(urlString);
        String JSONresponse = null;
        try {
            JSONresponse = getJSONString(url);
        }catch (IOException e)
        {
            //TODO: Handle the exception
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            if(JSONresponse == null)
                return null;
                JSONObject root = new JSONObject(JSONresponse);
                JSONArray features = root.getJSONArray("features");

                for(int i =0; i<features.length(); i++)
                {

                    JSONObject currentQuake = (JSONObject) features.get(i);
                    JSONObject properties = currentQuake.getJSONObject("properties");
                    double magnitude = properties.getDouble("mag");
                    String place = properties.getString("place");
                    long time = properties.getLong("time");
                    String itemUrl = properties.getString("url");
                    earthquakes.add(new earthquake(magnitude+"", place, time, itemUrl));
                }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}