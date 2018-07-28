package com.example.android.quakereport;

/**
 * Created by Pawan on 13-04-2018.
 */

public class earthquake {

    private String mMagnitude;
    private String mLocation;
    private long mTime;
    private String mUrl;

    earthquake(String m, String l, long t, String url)
    {
        mMagnitude = m;
        mLocation = l;
        mTime = t;
        mUrl = url;

    }

    public String getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getmTime() {
        return mTime;
    }

    public String getmUrl() {
        return mUrl;
    }
}
