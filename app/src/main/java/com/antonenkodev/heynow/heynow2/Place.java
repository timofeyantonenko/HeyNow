package com.antonenkodev.heynow.heynow2;

/**
 * Created by root on 10.06.15.
 */
public class Place {
    String name = null;
    long begin_time= Long.parseLong(null);
    long end_time = Long.parseLong(null);
    double lon = Double.parseDouble(null);
    double lat = Double.parseDouble(null);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(long begin_time) {
        this.begin_time = begin_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
