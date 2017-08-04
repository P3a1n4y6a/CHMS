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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FarmerRequestDetailFragment extends Fragment {
    private View farmerRequestView;
    private TextView txt_id, txt_fullname, txt_email, txt_address, txt_tel;
    private String farmer_status, CONTRACTOR_ID, farmer_id, fullname, email, address, tel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerRequestView = inflater.inflate(R.layout.fragment_farmer_request_detail, container, false);

        txt_id = (TextView) farmerRequestView.findViewById(R.id.id);
        txt_fullname = (TextView) farmerRequestView.findViewById(R.id.fullname);
        txt_email = (TextView) farmerRequestView.findViewById(R.id.email);
        txt_address = (TextView) farmerRequestView.findViewById(R.id.address);
        txt_tel = (TextView) farmerRequestView.findViewById(R.id.tel);

        initPreviousData();
        return farmerRequestView;
    }

    public void initPreviousData() {
        int index = getArguments().getInt("FARMER_INDEX2");
        farmer_status = getArguments().getString("FARMER_STATUS2");

        String farmer_detail = loadFarmerDetailPreferences();
        Log.d("FarmerDetail", farmer_detail);

        try {
            JSONArray jsonArray = new JSONArray(farmer_detail);
            JSONObject jsonObject = jsonArray.getJSONObject(index);

            farmer_id = jsonObject.getString("FARMER_ID");
            fullname = jsonObject.getString("TITLE_NAME") + " " + jsonObject.getString("FIRST_NAME") +
                    " " + jsonObject.getString("LAST_NAME");
            address = jsonObject.getString("ADDRESS") + " " + jsonObject.getString("DISTRICT") + " " +
                    jsonObject.getString("PROVINCE") + " " + jsonObject.getString("POSTCODE");
            email = jsonObject.getString("EMAIL");
            tel = jsonObject.getString("PHONE");
            saveToSharedPrefs(farmer_id);

            txt_id.setText(farmer_id);
            txt_fullname.setText(fullname);
            txt_address.setText(address);
            txt_email.setText(email);
            txt_tel.setText(tel);

            if (isOnline()) {
                new OkHttpHandler().execute();
            } else {
                Toast.makeText(getActivity(), "คุณไม่ได้เชื่อมต่อเครือข่ายอินเตอร์เน็ต", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadFarmerDetailPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("FARMER_REQUEST_LIST", "Not found");
        if(farmer_status.equals("ACCEPT")){
            data = preferences.getString("FARMER_ACCEPT_LIST", "Not found");
        } else if (farmer_status.equals("REQUEST")) {
            data = preferences.getString("FARMER_REQUEST_LIST", "Not found");
        }

        CONTRACTOR_ID = preferences.getString("CONTRACTOR_ID", "error");
        return data;
    }

    String REPLY_MSG = null;

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/farmer/reply_request?FARMER_ID=" +
                    farmer_id + "&CONTRACTOR_ID=" + CONTRACTOR_ID + "&REPLY_MSG=" + REPLY_MSG;

            Log.d("TAG",url);
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
            Log.d("TAG",result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("error")) {
                        Toast.makeText(getActivity(), "เกิดข้อผิดพลาดขณะแก้ไขข้อมูล", Toast.LENGTH_LONG).show();
                    }else {
                        getActivity().onBackPressed();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void saveToSharedPrefs(String farmer_id) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("FRAMER_ID", farmer_id);
        editor.commit();
    }
}
