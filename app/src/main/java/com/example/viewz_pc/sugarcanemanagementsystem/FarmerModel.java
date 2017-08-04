package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/26/2017.
 */

public class FarmerModel {
    private String id, fullname;

    FarmerModel(String id, String fullname){
        this.id = id;
        this.fullname = fullname;

    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }
}
