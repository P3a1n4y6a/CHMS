package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/27/2017.
 */

public class ZoneModel {
    private String  id, name;
    private boolean onMap;
    private double lat, lng;

    ZoneModel(String id, String name, double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.name = name;
        this.onMap = true;
    }

    public void setOnMap(boolean onMap) {
        this.onMap = onMap;
    }

    public boolean isOnMap() {
        return onMap;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
