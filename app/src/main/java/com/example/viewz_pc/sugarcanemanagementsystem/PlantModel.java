package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/26/2017.
 */

public class PlantModel {
    private String id, address, area, rate, status, zone_id, fullname, estCut;
    private double latitude, longitude;

    PlantModel(String id, String area) {
        this.id = id;
        this.area = area;
    }

    PlantModel(String id, String zone_id, double latitude, double longitude, String fullname, String estCut) {
        this.id = id;
        this.fullname = fullname;
        this.estCut = estCut;
        this.zone_id = zone_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    PlantModel(String id, String address, String area, String rate, String status) {
        this.id = id;
        this.address = address;
        this.area = area;
        this.rate = rate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getRate() {
        return rate;
    }

    public String getStatus() {
        return status;
    }

    public String getZone() {
        return zone_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEstCut() {
        return estCut;
    }
}
