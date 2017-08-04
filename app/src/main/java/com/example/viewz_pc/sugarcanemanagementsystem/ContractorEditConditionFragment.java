package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContractorEditConditionFragment extends Fragment {
    private View contractorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contractorView = inflater.inflate(R.layout.fragment_contractor_edit_condition, container, false);
        //new OkHttpHandler().execute();
        return contractorView;
    }

    /*private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        RequestBody formBody;
        String CONTRACTOR_ID, LABOR_COST, PRODUCT_COST, REPAIR_COST, DEAL_COST, LIFE_TIME, TRANS_COST,
                FUEL_COST, FUEL_RATE, AREA_COST;

        @Override
        protected void onPreExecute(){
            CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");

        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/update/detail?CONTRACTOR_ID="+ CONTRACTOR_ID +
                    "&amount_of_cutter="+ NUM_CUTTER +"&cutter_ability=" + CUTTER_AB + "&amount_of_tractor="+
                    NUM_TRACTOR + "&tractor_ability=" + TRACTOR_AB;

            formBody = new FormBody.Builder()
                    .add("CONTRACTOR_ID", CONTRACTOR_ID)
                    .add("labor_cost", LABOR_COST)
                    .add("cost_per_weight", PRODUCT_COST)
                    .add("package_repair_cost", REPAIR_COST)
                    .add("first_deal_cost", DEAL_COST)
                    .add("lifetime", LIFE_TIME)
                    .add("transfer_cost_per_round", TRANS_COST)
                    .add("fuel_cost", FUEL_COST)
                    .add("cutter_fuel_rate", FUEL_RATE)
                    .add("cost_per_area", AREA_COST)
                    .build();

            request = new Request.Builder()
                    .put(formBody)
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

            if(result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("result").contains("successful")) {
                        Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "ข้อมูลไม่ถูกบันทึก", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "เกิดข้อผิดพลาดในการส่งข้อมูล", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
