package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContractorShowProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton editBtn;
    private TextView txt_fullname, txt_company, txt_address, txt_email, txt_phone,txt_requirement_detail;
    private String[] question, unit;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<String> answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_show_profile);

        txt_fullname = (TextView) findViewById(R.id.fullname);
        txt_company = (TextView) findViewById(R.id.company);
        txt_address = (TextView) findViewById(R.id.address);
        txt_email = (TextView) findViewById(R.id.email);
        txt_phone = (TextView) findViewById(R.id.phone);
        txt_requirement_detail = (TextView) findViewById(R.id.requirement_detail);

        editBtn = (FloatingActionButton) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);

        new OkHttpHandler().execute();
        new OkHttpHandler2().execute();
        new OkHttpHandler3().execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ContractorEditModuleActivity.class);
        startActivity(intent);
    }

    public void showSatisfiedCriteria(ArrayList<String> answers) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(ContractorShowProfileActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        question = new String[]{"1. ค่าแรง", "2. ค่าหัวตัน", getResources().getString(R.string.repair_cost),
                "4. ค่าเริ่มแรก", "5. อายุการใช้งาน", getResources().getString(R.string.transport_cost),
                getResources().getString(R.string.fuel_cutter_cost), getResources().getString(R.string.fuel_rate_use),
                "9. พื้นที่"};
        unit = new String[]{"บาท/ตัน", "บาท/ตัน", "บาท", "บาท", "วัน", "บาท/รอบ", "บาท/ลิตร", "ลิตร/ตัน", "บาท/ไร่"};

        RecyclerView.Adapter adapter = new CriteriaListAdapter(ContractorShowProfileActivity.this, question, unit, answers);
        recyclerView.setAdapter(adapter);
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String Username;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Username = loadPreferencesContractor("username");
            Log.d("Profile", Username);
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/profile?username="+Username;
            Log.d("Profile",url);
            request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                response = client.newCall(request).execute();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Profile here",result);
            saveToSharedPrefs(result);
            try {
                if (!result.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String fullname, company, address, email, phone;
                    fullname = jsonObject.getString("TITLE_NAME") + " " + jsonObject.getString("FIRST_NAME")
                            + " " + jsonObject.getString("LAST_NAME");
                    company = jsonObject.getString("COMPANY_NAME");
                    email = jsonObject.getString("EMAIL");
                    phone = jsonObject.getString("PHONE");
                    address = jsonObject.getString("ADDRESS") + " " + jsonObject.getString("SUB_DISTRICT")
                            + " " + jsonObject.getString("DISTRICT") +
                            jsonObject.getString("PROVINCE") + " " + jsonObject.getString("POSTCODE");

                    txt_fullname.setText(fullname);
                    txt_company.setText(company);
                    txt_address.setText(address);
                    txt_email.setText(email);
                    txt_phone.setText(phone);
                } else {
                    Toast.makeText(ContractorShowProfileActivity.this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class OkHttpHandler2 extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
            Log.d("Profile", CONTRACTOR_ID);
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/get/rate?CONTRACTOR_ID=" + CONTRACTOR_ID;
            Log.d("Profile",url);
            request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                response = client.newCall(request).execute();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Profile", result);
            try {
                if (!result.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    answer = new ArrayList<>();
                    answer.add(jsonObject.getString("labor_cost"));
                    answer.add(jsonObject.getString("cost_per_weight"));
                    answer.add(jsonObject.getString("package_repair_cost"));
                    answer.add(jsonObject.getString("first_deal_cost"));
                    answer.add(jsonObject.getString("lifetime"));
                    answer.add(jsonObject.getString("transfer_cost_per_round"));
                    answer.add(jsonObject.getString("fuel_cost"));
                    answer.add(jsonObject.getString("cutter_fuel_rate"));
                    answer.add(jsonObject.getString("cost_per_area"));

                    showSatisfiedCriteria(answer);
                } else {
                    Toast.makeText(ContractorShowProfileActivity.this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class OkHttpHandler3 extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
            Log.d("Profile", CONTRACTOR_ID);
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/get/detail?CONTRACTOR_ID=" + CONTRACTOR_ID;
            Log.d("Profile",url);
            request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                response = client.newCall(request).execute();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Show Profile",result);
            /*try {
                if (!result.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String fullname, company, address, email, phone;
                    fullname = jsonObject.getString("TITLE_NAME") + " " + jsonObject.getString("FIRST_NAME")
                            + " " + jsonObject.getString("LAST_NAME");
                    company = jsonObject.getString("COMPANY_NAME");
                    email = jsonObject.getString("EMAIL");
                    phone = jsonObject.getString("PHONE");
                    address = jsonObject.getString("ADDRESS") + " " + jsonObject.getString("SUB_DISTRICT")
                            + " " + jsonObject.getString("DISTRICT") +
                            jsonObject.getString("PROVINCE") + " " + jsonObject.getString("POSTCODE");

                    txt_fullname.setText(fullname);
                    txt_company.setText(company);
                    txt_address.setText(address);
                    txt_email.setText(email);
                    txt_phone.setText(phone);
                } else {
                    Toast.makeText(ContractorShowProfileActivity.this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SEND_TO_EDIT", data);
        editor.commit();
    }
}
