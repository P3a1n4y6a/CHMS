package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountingPlantActivity extends AppCompatActivity {
    private ArrayList<PlantModel> plantList, dialogData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_plant);

        new OkHttpHandler().execute();
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/get/plantCost?contractor_id=" + CONTRACTOR_ID;

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
                if (response.isSuccessful()) {
                    return response.body().string(); // Read data
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("AccountPlant", data);

            try {
                if (!data.equals("[]")) {
                    plantList = new ArrayList<>();
                    dialogData = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        plantList.add(new PlantModel(jsonObject.getString("PLANT_ID"), jsonObject.getString("farmer"),
                                        jsonObject.getString("surveyor"), jsonObject.getString("harvester")));
                        dialogData.add(new PlantModel(jsonObject.getString("WORK_TYPE"), jsonObject.getString("WORKING_COST"), jsonObject.getString("REPAIR_COST"),
                                jsonObject.getString("DEPRECIATION_COST"), jsonObject.getString("FUEL_COST"),
                                jsonObject.getString("TRANSFER_COST")));
                    }

                    initRecycler(plantList, dialogData);
                } else {
                    Toast.makeText(AccountingPlantActivity.this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initRecycler(ArrayList<PlantModel> plantList, ArrayList<PlantModel> dialogData){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountingPlantActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        AccountingAdapter adapter = new AccountingAdapter(AccountingPlantActivity.this, plantList, dialogData);
        recyclerView.setAdapter(adapter);
    }

    private String loadPreferences(String key) {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
