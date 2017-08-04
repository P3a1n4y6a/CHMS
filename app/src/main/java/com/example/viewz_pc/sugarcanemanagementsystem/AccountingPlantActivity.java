package com.example.viewz_pc.sugarcanemanagementsystem;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountingPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_plant);
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
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
}
