package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowPlantDetailActivity extends AppCompatActivity {
    private TextView txt_plant, txt_farmer, txt_address, txt_area, txt_rate, txt_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plant_detail);

        txt_plant = (TextView) findViewById(R.id.plant);
        txt_farmer = (TextView) findViewById(R.id.farmer);
        txt_address = (TextView) findViewById(R.id.address);
        txt_area = (TextView) findViewById(R.id.area);
        txt_rate = (TextView) findViewById(R.id.rate);
        txt_status = (TextView) findViewById(R.id.status);

        initPreviousData();
    }

    public void initPreviousData() {
        int index = getIntent().getIntExtra("PLANT_INDEX", 0);
        String plant_detail = loadPreferencesPlant();
        Log.d("Show plant", plant_detail);
        String plant, farmer, address, area, rate, status;

        try {
            JSONArray jsonArray = new JSONArray(plant_detail);
            JSONObject jsonObject = jsonArray.getJSONObject(index);

            plant = jsonObject.getString("PLANT_ID");
            farmer = jsonObject.getString("FARMER_ID");
            address = jsonObject.getString("address") + " " + jsonObject.getString("sub_district") + " " +
                    jsonObject.getString("district") + " " + jsonObject.getString("province") + " " +
                    jsonObject.getString("zip_code");
            area = jsonObject.getString("request_plant_area");
            rate = jsonObject.getString("request_plant_rate");
            status = jsonObject.getString("STATUS");

            txt_plant.setText(plant);
            txt_farmer.setText(farmer);
            txt_address.setText(address);
            txt_area.setText(area);
            txt_rate.setText(rate);
            txt_status.setText(status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadPreferencesPlant() {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("PLANT_LIST", "Not found");
        return data;
    }
}
