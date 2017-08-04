package com.example.viewz_pc.sugarcanemanagementsystem;

/**
 * Created by Viewz-PC on 7/27/2017.
 */

public class SurveyQueueModel {
    private String id, status;

    SurveyQueueModel(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
