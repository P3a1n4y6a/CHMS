package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CuttingPlantModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<CuttingModel> cutplantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_plant_module);

        Button CreateCuttingPlant_button = (Button) findViewById(R.id.button7);
        CreateCuttingPlant_button.setOnClickListener(this);

        new OkHttpHandler().execute();
    }

    public void initRecycler(ArrayList<CuttingModel> queueList){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(CuttingPlantModuleActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CuttingAdapter(CuttingPlantModuleActivity.this, queueList);
        recyclerView.setAdapter(adapter);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {
        //String CONTRACTOR_NO = loadPreferencesNo();

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/contractor/get_est_cutting_queue_list?CONTRACTOR_NO=1";

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
            Log.d("CuttingPlantModule", data);
            if (data != null) {
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    cutplantList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        cutplantList.add(new CuttingModel(jsonObject.getString("ID"),
                                jsonObject.getString("EST_CUTTING_CODE")));
                    }
                    initRecycler(cutplantList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button7){
            Intent intent = new Intent(CuttingPlantModuleActivity.this, CuttingPlantCreateActivity.class);
            startActivity(intent);
        }
    }
}
