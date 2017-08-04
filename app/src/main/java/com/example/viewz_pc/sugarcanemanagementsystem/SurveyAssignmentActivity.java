package com.example.viewz_pc.sugarcanemanagementsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SurveyAssignmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<SurveyQueueModel> queueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new OkHttpHandler().execute();
        setContentView(R.layout.activity_survey_assignment);
    }

    public void initRecycler(ArrayList<SurveyQueueModel> queueList){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(SurveyAssignmentActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SurveyQueueAdapter(SurveyAssignmentActivity.this, queueList);
        recyclerView.setAdapter(adapter);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {
        //String CONTRACTOR_NO = loadPreferencesNo();

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
            Log.d("Assignment", data);
            /*if (data != null) {
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    queueList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        queueList.add(new SurveyQueueModel(jsonObject.getString("PLANT_ID"),
                                jsonObject.getString("STATUS")));
                    }
                    initRecycler(queueList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
