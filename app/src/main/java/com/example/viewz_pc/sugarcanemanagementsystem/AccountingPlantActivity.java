package com.example.viewz_pc.sugarcanemanagementsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountingPlantActivity extends AppCompatActivity {
    private ArrayList<PlantModel> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_plant);

        plantList = new ArrayList<>();
        plantList.add(new PlantModel("60012-001", "นาย ก. ชาวไร่", "นาย ก. สำรวจ", "นาย ค. ทำงาน"));
        plantList.add(new PlantModel("60012-002", "นาย ก. ชาวไร่", "นาย ก. สำรวจ", "นาย ค. ทำงาน"));
        plantList.add(new PlantModel("60013-004", "นาย ก. ชาวไร่", "นาย ก. สำรวจ", "นาย ค. ทำงาน"));
        initRecycler(plantList);
        //new OkHttpHandler().execute();
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "";

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

        }
    }

    public void initRecycler(ArrayList<PlantModel> plantList){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountingPlantActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        AccountingAdapter adapter = new AccountingAdapter(AccountingPlantActivity.this, plantList);
        recyclerView.setAdapter(adapter);
    }
}
