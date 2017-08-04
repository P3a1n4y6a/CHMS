package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/26/2017.
 */

public class PlantModel {
    private String id, address, area, rate, status, zone_id, fullname, estCut, survey, employee;
    private double latitude, longitude;
    private String labor_cost, repair_cost, decadent_cost, fuel_cost, trans_cost;

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

    PlantModel(String id, String fullname, String survey, String employee) {
        this.id = id;
        this.fullname = fullname;
        this.survey = survey;
        this.employee = employee;
    }

    PlantModel(String labor_cost, String repair_cost, String decadent_cost, String fuel_cost, String trans_cost){
        this.labor_cost = labor_cost;
        this.repair_cost = repair_cost;
        this.decadent_cost = decadent_cost;
        this.fuel_cost = fuel_cost;
        this.trans_cost = trans_cost;
    }

    public String getId() {
        return id;
    }

    public String getArea() {
        return area;
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

    public String getLabor_cost() {
        return labor_cost;
    }

    public String getRepair_cost() {
        return repair_cost;
    }

    public String getDecadent_cost() {
        return decadent_cost;
    }

    public String getFuel_cost() {
        return fuel_cost;
    }

    public String getTrans_cost() {
        return trans_cost;
    }

    public String getSurvey() {
        return survey;
    }

    public String getEmployee() {
        return employee;
    }
}
