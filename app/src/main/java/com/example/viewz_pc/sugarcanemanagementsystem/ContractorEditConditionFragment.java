package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
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

public class ContractorEditConditionFragment extends Fragment implements View.OnClickListener {
    private View contractorView;
    private Button saveBtn, clearBtn;
    private EditText editText;
    private int[] editText_id = {R.id.numCutter, R.id.cutterBlank, R.id.numTractor, R.id.tractorBlank};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contractorView = inflater.inflate(R.layout.fragment_contractor_edit_condition, container, false);

        saveBtn = (Button) contractorView.findViewById(R.id.saveBtn);
        clearBtn = (Button) contractorView.findViewById(R.id.clearBtn);

        saveBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        initPreviousData();
        return contractorView;
    }

    public void initPreviousData() {
        String data = loadPreferences("ABILITY");
        Log.d("ContractorEditCondition", data);

        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            editText = (EditText) contractorView.findViewById(R.id.numCutter);
            editText.setText(jsonObject.getString("amount_of_cutter"));

            editText = (EditText) contractorView.findViewById(R.id.cutterBlank);
            editText.setText(jsonObject.getString("total_cutter_ability"));

            editText = (EditText) contractorView.findViewById(R.id.numTractor);
            editText.setText(jsonObject.getString("amount_of_tractor"));

            editText = (EditText) contractorView.findViewById(R.id.tractorBlank);
            editText.setText(jsonObject.getString("total_tractor_ability"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        RequestBody formBody;
        String CONTRACTOR_ID, NUM_CUTTER, CUTTER_AB, NUM_TRACTOR, TRACTOR_AB;

        @Override
        protected void onPreExecute(){
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");

            editText = (EditText) contractorView.findViewById(R.id.numCutter);
            NUM_CUTTER = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.cutterBlank);
            CUTTER_AB = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.numTractor);
            NUM_TRACTOR = editText.getText().toString();

            editText = (EditText) contractorView.findViewById(R.id.tractorBlank);
            TRACTOR_AB = editText.getText().toString();
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/update/detail?CONTRACTOR_ID="+
                    CONTRACTOR_ID + "&amount_of_cutter=" + NUM_CUTTER + "&total_cutter_ability="+
                    CUTTER_AB +"&amount_of_tractor=" + NUM_TRACTOR +"&total_tractor_ability=" + TRACTOR_AB;

            Log.d("Edit condition", url);
            formBody = new FormBody.Builder()
                    .add("CONTRACTOR_ID", CONTRACTOR_ID)
                    .add("amount_of_cutter", NUM_CUTTER)
                    .add("total_cutter_ability", CUTTER_AB)
                    .add("amount_of_tractor", NUM_TRACTOR)
                    .add("total_tractor_ability", TRACTOR_AB)
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
