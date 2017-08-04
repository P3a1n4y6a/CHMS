package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String authen = loadPreferencesAuthorization();
        if(Objects.equals(authen, "error")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{

            //------------ create account temp data ---------------
            try {
                JSONObject jsonObject = new JSONObject(authen);
                String username = jsonObject.getString("username");
                String department = jsonObject.getString("department");
                JSONArray account_detail = jsonObject.getJSONArray("account_detail");
                String CONTRACTOR_ID = account_detail.getJSONObject(0).getString("CONTRACTOR_ID");
                String token = jsonObject.getString("token");

                // Save share preferences
                SharedPreferences pref = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", username);
                editor.putString("department", department);
                editor.putString("CONTRACTOR_ID", CONTRACTOR_ID);
                editor.putString("token", token);

                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //--------------- finish ------------------------------


            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String loadPreferencesAuthorization() {
        SharedPreferences preferences = this.getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String authen = preferences.getString("Account_data", "error");
        return authen;
    }
}
