package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/27/2017.
 */

public class CuttingModel {
    private String id, est_cutting;

    CuttingModel (String id, String est_cutting) {
        this.id = id;
        this.est_cutting = est_cutting;
    }

    public String getId() {
        return id;
    }

    public String getEst_cutting() {
        return est_cutting;
    }
}
