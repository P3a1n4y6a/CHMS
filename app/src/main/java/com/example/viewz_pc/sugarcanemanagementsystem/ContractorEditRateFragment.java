package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ContractorEditRateFragment extends Fragment implements View.OnClickListener {
    private View contractorView;
    private EditText editText;
    private Button saveBtn, clearBtn;
    private int[] edit_id = {R.id.laborEdit, R.id.productEdit, R.id.repairEdit, R.id.originEdit
            , R.id.ageEdit, R.id.transEdit, R.id.fuelCostEdit, R.id.fuelRateEdit, R.id.areaEdit};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contractorView = inflater.inflate(R.layout.fragment_contractor_edit_rate, container, false);

        saveBtn = (Button) contractorView.findViewById(R.id.saveBtn);
        clearBtn = (Button) contractorView.findViewById(R.id.clearBtn);

        saveBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        return contractorView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                new OkHttpHandler().execute(); // Update data to db
                break;
            case R.id.clearBtn:
                for(int i = 0; i < edit_id.length; i++) {
                    editText = (EditText) contractorView.findViewById(edit_id[i]);
                    editText.setText("0");
                }
                break;
        }
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        RequestBody formBody;
        String CONTRACTOR_ID, LABOR_COST, PRODUCT_COST, REPAIR_COST, DEAL_COST, LIFE_TIME, TRANS_COST,
            FUEL_COST, FUEL_RATE, AREA_COST;

        @Override
        protected void onPreExecute(){
            CONTRACTOR_ID = loadPreferencesContractor();

            editText = (EditText) contractorView.findViewById(R.id.laborEdit);
            LABOR_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.productEdit);
            PRODUCT_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.repairEdit);
            REPAIR_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.originEdit);
            DEAL_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.ageEdit);
            LIFE_TIME = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.transEdit);
            TRANS_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.fuelCostEdit);
            FUEL_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.fuelRateEdit);
            FUEL_RATE = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.areaEdit);
            AREA_COST = editText.getText().toString();
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/update/rate?CONTRACTOR_ID="+ CONTRACTOR_ID
                    +"&labor_cost="+ LABOR_COST +"&cost_per_weight="+ PRODUCT_COST +"&package_repair_cost=" +
                    REPAIR_COST + "&first_deal_cost="+ DEAL_COST +"&lifetime=" + LIFE_TIME +
                    "&transfer_cost_per_round="+ TRANS_COST +"&fuel_cost=" + FUEL_COST + "&cutter_fuel_rate="
                    + FUEL_RATE + "&cost_per_area=" + AREA_COST;

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
    }

    private String loadPreferencesContractor() {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("username", "Not found");
        return data;
    }
}
