package com.example.android.Earthquake;

/**
 * Created by mahmoud on 24/02/17.
 */

public class EarthquakeData {
    private Double magnitude;
    private String location;
    private long date;
    private long timeInMilliseconds;
    private String url;

    public EarthquakeData(Double magnitude, String location, long date, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.date = date;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getDate() {
        return date;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public String getUrl() {
        return url;
    }
}
