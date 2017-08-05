package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONArray;
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
    private int[] editText_id = {R.id.laborEdit, R.id.costWeightEdit, R.id.costAreaEdit, R.id.repairCutterEdit,
            R.id.repairTractorEdit, R.id.weightYearEdit, R.id.areaYearEdit, R.id.cutterCostEdit,
            R.id.tractorCostEdit, R.id.cutterLifeEdit, R.id.tractorLifeEdit, R.id.fuelRateEdit,
            R.id.fuelCostEdit, R.id.transEdit};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contractorView = inflater.inflate(R.layout.fragment_contractor_edit_rate, container, false);

        saveBtn = (Button) contractorView.findViewById(R.id.saveBtn);
        clearBtn = (Button) contractorView.findViewById(R.id.clearBtn);

        saveBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        initPreviousData();
        return contractorView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                if (isOnline()) new OkHttpHandler().execute(); // Update data to db
                else Toast.makeText(getActivity(), getResources().getString(R.string.error_unconnect),
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.clearBtn:
                for(int i = 0; i < editText_id.length; i++) {
                    editText = (EditText) contractorView.findViewById(editText_id[i]);
                    editText.setText("0");
                }
                break;
        }
    }

    public void initPreviousData() {
        String data = loadPreferences("RATE");
        Log.d("ContractorEditCondition", data);

        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            editText = (EditText) contractorView.findViewById(R.id.laborEdit);
            editText.setText(jsonObject.getString("labor_cost"));

            editText = (EditText) contractorView.findViewById(R.id.costWeightEdit);
            editText.setText(jsonObject.getString("cost_per_weight"));

            editText = (EditText) contractorView.findViewById(R.id.costAreaEdit);
            editText.setText(jsonObject.getString("cost_per_area"));

            editText = (EditText) contractorView.findViewById(R.id.repairCutterEdit);
            editText.setText(jsonObject.getString("cutter_repair_cost"));

            editText = (EditText) contractorView.findViewById(R.id.repairTractorEdit);
            editText.setText(jsonObject.getString("tractor_repair_cost"));

            editText = (EditText) contractorView.findViewById(R.id.weightYearEdit);
            editText.setText(jsonObject.getString("weight_per_year"));

            editText = (EditText) contractorView.findViewById(R.id.areaYearEdit);
            editText.setText(jsonObject.getString("area_per_year"));

            editText = (EditText) contractorView.findViewById(R.id.cutterCostEdit);
            editText.setText(jsonObject.getString("cutter_cost"));

            editText = (EditText) contractorView.findViewById(R.id.tractorCostEdit);
            editText.setText(jsonObject.getString("tractor_cost"));

            editText = (EditText) contractorView.findViewById(R.id.cutterLifeEdit);
            editText.setText(jsonObject.getString("cutter_lifetime"));

            editText = (EditText) contractorView.findViewById(R.id.tractorLifeEdit);
            editText.setText(jsonObject.getString("tractor_lifetime"));

            editText = (EditText) contractorView.findViewById(R.id.fuelRateEdit);
            editText.setText(jsonObject.getString("cutter_fuel_rate"));

            editText = (EditText) contractorView.findViewById(R.id.fuelCostEdit);
            editText.setText(jsonObject.getString("fuel_cost"));

            editText = (EditText) contractorView.findViewById(R.id.transEdit);
            editText.setText(jsonObject.getString("transfer_cost"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        RequestBody formBody;
        String CONTRACTOR_ID, LABOR_COST, COST_WEIGHT, COST_AREA, REPAIR_CUTTER,
                REPAIR_TRACTOR, WEIGHT_YEAR, AREA_YEAR, CUTTER_COST, TRACTOR_COST, CUTTER_LIFE,
                TRACTOR_LIFE, TRANS_COST, FUEL_COST, FUEL_RATE;

        @Override
        protected void onPreExecute(){
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");

            editText = (EditText) contractorView.findViewById(R.id.laborEdit);
            LABOR_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.costWeightEdit);
            COST_WEIGHT = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.costAreaEdit);
            COST_AREA = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.repairCutterEdit);
            REPAIR_CUTTER = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.repairTractorEdit);
            REPAIR_TRACTOR = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.weightYearEdit);
            WEIGHT_YEAR = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.areaYearEdit);
            AREA_YEAR = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.cutterCostEdit);
            CUTTER_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.tractorCostEdit);
            TRACTOR_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.cutterLifeEdit);
            CUTTER_LIFE = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.tractorLifeEdit);
            TRACTOR_LIFE = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.fuelRateEdit);
            FUEL_RATE = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.fuelCostEdit);
            FUEL_COST = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.transEdit);
            TRANS_COST = editText.getText().toString();
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/update/rate?CONTRACTOR_ID=" + CONTRACTOR_ID +
                    "&labor_cost="+LABOR_COST+"&cost_per_weight="+COST_WEIGHT+"&cost_per_area="+COST_AREA+
                    "&cutter_repair_cost="+REPAIR_CUTTER+"&tractor_repair_cost="+REPAIR_TRACTOR+
                    "&weight_per_year="+WEIGHT_YEAR+"&area_per_year="+AREA_YEAR+"&cutter_cost="+CUTTER_COST
                    +"&tractor_cost="+TRACTOR_COST+"&cutter_lifetime="+CUTTER_LIFE+"&tractor_lifetime="+
                    TRACTOR_LIFE+"&cutter_fuel_rate="+FUEL_RATE+"&fuel_cost="+FUEL_COST+"&transfer_cost=" + TRANS_COST;

            formBody = new FormBody.Builder()
                    .add("CONTRACTOR_ID", CONTRACTOR_ID)
                    .add("labor_cost", LABOR_COST)
                    .add("cost_per_weight", COST_WEIGHT)
                    .add("cost_per_area", COST_AREA)
                    .add("cutter_repair_cost", REPAIR_CUTTER)
                    .add("tractor_repair_cost", REPAIR_TRACTOR)
                    .add("weight_per_year", WEIGHT_YEAR)
                    .add("area_per_year", AREA_YEAR)
                    .add("cutter_cost", CUTTER_COST)
                    .add("tractor_cost", TRACTOR_COST)
                    .add("cutter_lifetime", CUTTER_LIFE)
                    .add("tractor_lifetime", TRACTOR_LIFE)
                    .add("cutter_fuel_rate", FUEL_RATE)
                    .add("fuel_cost", FUEL_COST)
                    .add("transfer_cost", TRANS_COST)
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

            if(!result.equals("[]")) {
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private String loadPreferences(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
