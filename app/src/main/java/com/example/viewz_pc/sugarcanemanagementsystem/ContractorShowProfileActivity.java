package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContractorShowProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton editBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txt_fullname, txt_company, txt_address, txt_email, txt_phone,txt_requirement_detail;
    private String[] question, unit;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<String> answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_show_profile);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);

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

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_green_light));
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new OkHttpHandler().execute();
                        new OkHttpHandler2().execute();
                        new OkHttpHandler3().execute();

                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1000);
                    }
                }
        );
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

        question = new String[]{"1. ค่าแรง", "2. ค่าหัวตัน", "3. ค่าต่อพื้นที่", "4. ค่าซ่อมบำรุง\nรถตัด", "5. ค่าซ่อมบำรุง\nรถแทรกเตอร์",
                "6. ผลิตอ้อย", "7. ผลิตไร่", "8. ราคาซื้อ\nรถตัดอ้อย", "9. ราคาซื้อ\nรถแทรกเตอร์", "10. อายุการใช้งาน\nรถตัดอ้อย", "11. อายุการใช้งาน\nรถแทรกเตอร์",
                "12. อัตราน้ำมันรถตัดอ้อย", "13. ค่าน้ำมัน", "14. ค่าขนย้ายแปลง"};
        unit = new String[]{"บาท/วัน", "บาท/ตัน", "บาท/ไร่", "บาท/ปี", "บาท/ปี", "ตัน/ปี", "ไร่/ปี", "บาท/ปี", "บาท/ปี",
                "ปี", "ปี", "ลิตร/ตัน", "บาท/ลิตร", "บาท/ครั้ง"};

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
            saveToSharedPrefs("SEND_TO_EDIT", result);
            try {
                if (!result.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String fullname, company, address, email, phone, requirement_detail;
                    fullname = jsonObject.getString("TITLE_NAME") + " " + jsonObject.getString("FIRST_NAME")
                            + " " + jsonObject.getString("LAST_NAME");
                    company = jsonObject.getString("COMPANY_NAME");
                    email = jsonObject.getString("EMAIL");
                    phone = jsonObject.getString("PHONE");
                    address = jsonObject.getString("ADDRESS") + " " + jsonObject.getString("SUB_DISTRICT")
                            + " " + jsonObject.getString("DISTRICT") +
                            jsonObject.getString("PROVINCE") + " " + jsonObject.getString("POSTCODE");
                    requirement_detail = jsonObject.getString("REQUIREMENT_DETAIL");

                    txt_fullname.setText(fullname);
                    txt_company.setText(company);
                    txt_address.setText(address);
                    txt_email.setText(email);
                    txt_phone.setText(phone);
                    txt_requirement_detail.setText(requirement_detail);
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
                    saveToSharedPrefs("RATE", result);
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    answer = new ArrayList<>();
                    answer.add(jsonObject.getString("labor_cost"));
                    answer.add(jsonObject.getString("cost_per_weight"));
                    answer.add(jsonObject.getString("cost_per_area"));
                    answer.add(jsonObject.getString("cutter_repair_cost"));
                    answer.add(jsonObject.getString("tractor_repair_cost"));
                    answer.add(jsonObject.getString("weight_per_year"));
                    answer.add(jsonObject.getString("area_per_year"));
                    answer.add(jsonObject.getString("cutter_cost"));
                    answer.add(jsonObject.getString("tractor_cost"));
                    answer.add(jsonObject.getString("cutter_lifetime"));
                    answer.add(jsonObject.getString("tractor_lifetime"));
                    answer.add(jsonObject.getString("cutter_fuel_rate"));
                    answer.add(jsonObject.getString("fuel_cost"));
                    answer.add(jsonObject.getString("transfer_cost"));

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
        protected void onPreExecute(){
            CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/get/detail?CONTRACTOR_ID="+CONTRACTOR_ID;

            request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Edit Rate", result);

            if(!result.equals("[]")) {
                saveToSharedPrefs("ABILITY", result);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    TextView textView;
                    textView = (TextView) findViewById(R.id.numCutter);
                    textView.setText(jsonObject.getString("amount_of_cutter"));

                    textView = (TextView) findViewById(R.id.cutterBlank);
                    textView.setText(jsonObject.getString("total_cutter_ability"));

                    textView = (TextView) findViewById(R.id.numTractor);
                    textView.setText(jsonObject.getString("amount_of_tractor"));

                    textView = (TextView) findViewById(R.id.tractorBlank);
                    textView.setText(jsonObject.getString("total_tractor_ability"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ContractorShowProfileActivity.this, "เกิดข้อผิดพลาดในการส่งข้อมูล", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveToSharedPrefs(String key, String data) {
        SharedPreferences pref = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, data);
        editor.commit();
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
