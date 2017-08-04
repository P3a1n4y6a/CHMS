package com.example.viewz_pc.sugarcanemanagementsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CuttingPlantCreateActivity extends AppCompatActivity {
    private TextView txt_gradeA, txt_gradeB, txt_gradeC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new OkHttpHandler().execute();
        setContentView(R.layout.activity_cutting_plant_create);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {
        //String CONTRACTOR_NO = loadPreferencesNo();

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/contractor/cutting_plant_modul/choose_plant_with_grade?CONTRACTOR_NO=1&GRADE_A=1&GRADE_B=1&GRADE_C=1";

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
            Log.d("Grade", data);
            if (data != null) {
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    txt_gradeA = (TextView) findViewById(R.id.gradeA);
                    txt_gradeB = (TextView) findViewById(R.id.gradeB);
                    txt_gradeC = (TextView) findViewById(R.id.gradeC);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        String grade = jsonObject.getString("GRADE");
                        if(grade.matches("1")){ // 1 = Grade C
                            txt_gradeC.setText(jsonObject.getString("PLANT_AREA"));
                        } else if (grade.matches("2")) {
                            txt_gradeB.setText(jsonObject.getString("PLANT_AREA"));
                        } else {
                            txt_gradeA.setText(jsonObject.getString("PLANT_AREA"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
