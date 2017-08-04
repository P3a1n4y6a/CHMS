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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ContractorEditProfileFragment extends Fragment implements View.OnClickListener {
    private View contractorView;
    private EditText editText;
    private int[] editText_id = {R.id.title, R.id.firstname, R.id.lastname, R.id.company, R.id.email,
            R.id.phone, R.id.address, R.id.subDistrict, R.id.district, R.id.province, R.id.postCode};
    private String Url = null;
    private String USERNAME,  CONTRACTOR_ID, CONFIRM_PASSWORD, DEPARTMENT, PASSWORD, COMPANY_NAME, EMAIL, TITLE_NAME, FIRST_NAME,
            LAST_NAME, PHONE, ADDRESS, SUB_DISTRICT, DISTRICT, PROVINCE, POSTCODE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contractorView = inflater.inflate(R.layout.fragment_contractor_edit_profile, container, false);
        initPreviousData();

        Button saveBtn = (Button) contractorView.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        return contractorView;
    }

    public void initPreviousData() {
        String profile = loadPreferencesContractor();
        Log.d("Edit profile edit", profile);
        String[] edit_data = new String[11];
        JSONArray jsonArray = null;
        try {
            if (!profile.equals("[]")) {
                jsonArray = new JSONArray(profile);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                edit_data[0] = jsonObject.getString("TITLE_NAME");
                edit_data[1] = jsonObject.getString("FIRST_NAME");
                edit_data[2] = jsonObject.getString("LAST_NAME");
                edit_data[3] = jsonObject.getString("COMPANY_NAME");
                edit_data[4] = jsonObject.getString("EMAIL");
                edit_data[5] = jsonObject.getString("PHONE");
                edit_data[6] = jsonObject.getString("ADDRESS");
                edit_data[7] = jsonObject.getString("SUB_DISTRICT");
                edit_data[8] = jsonObject.getString("DISTRICT");
                edit_data[9] = jsonObject.getString("PROVINCE");
                edit_data[10] = jsonObject.getString("POSTCODE");
            }else {
                Toast.makeText(getActivity(), "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < edit_data.length; i++) {
            //Log.d("edit profile", edit_data[i]);
            editText = (EditText) contractorView.findViewById(editText_id[i]);
            editText.setText(edit_data[i]);
        }
    }

    @Override
    public void onClick(View v) {
        editText = (EditText) contractorView.findViewById(R.id.company);
        COMPANY_NAME = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.email);
        EMAIL = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.phone);
        PHONE = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.title);
        TITLE_NAME = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.firstname);
        FIRST_NAME = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.lastname);
        LAST_NAME = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.address);
        ADDRESS = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.subDistrict);
        SUB_DISTRICT = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.district);
        DISTRICT = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.province);
        PROVINCE = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.postCode);
        POSTCODE = editText.getText().toString();

        editText = (EditText) contractorView.findViewById(R.id.edit_confirm_password);
        CONFIRM_PASSWORD = editText.getText().toString();

        // Load share preference
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        USERNAME = preferences.getString("username", "Not found");
        CONTRACTOR_ID = preferences.getString("CONTRACTOR_ID", "Not found");
        PASSWORD = CONFIRM_PASSWORD;

        DEPARTMENT = "contractor";
        String query = "&EMAIL=" + EMAIL + "&TITLE_NAME=" + TITLE_NAME + "&FIRST_NAME=" + FIRST_NAME + "&LAST_NAME=" + LAST_NAME + "&PHONE=" + PHONE + "&ADDRESS=" + ADDRESS + "&SUB_DISTRICT=" + SUB_DISTRICT + "&DISTRICT=" + DISTRICT + "&PROVINCE=" + PROVINCE + "&POSTCODE=" + POSTCODE + "&CONTRACTOR_ID=" + CONTRACTOR_ID + "&COMPANY_NAME=" + COMPANY_NAME;
        Url = "http://188.166.191.60/api/v2/account/update/profile?username=" + USERNAME + "&password=" + PASSWORD + "&department=" + DEPARTMENT + query;
        Log.d("Edit profile",Url);

        new OkHttpHandler().execute();
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        RequestBody formBody;

        @Override
        protected String doInBackground(Object... params) {

            formBody = new FormBody.Builder()
                    .add("username", USERNAME)
                    .add("password", PASSWORD)
                    .add("department", DEPARTMENT)
                    .add("EMAIL", EMAIL)
                    .add("TITLE_NAME", TITLE_NAME)
                    .add("FIRST_NAME", FIRST_NAME)
                    .add("LAST_NAME", LAST_NAME)
                    .add("PHONE", PHONE)
                    .add("ADDRESS", ADDRESS)
                    .add("SUB_DISTRICT", SUB_DISTRICT)
                    .add("DISTRICT", DISTRICT)
                    .add("PROVINCE", PROVINCE)
                    .add("POSTCODE", POSTCODE)
                    .add("CONTRACTOR_ID", CONTRACTOR_ID)
                    .add("COMPANY_NAME", COMPANY_NAME)
                    .build();

            request = new Request.Builder()
                    .put(formBody)
                    .url(Url)
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
            Log.d("Edit Profile", result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("error")) {
                        Toast.makeText(getActivity(), "เกิดข้อผิดพลาด ไม่สามารถเปลี่ยนแปลงข้อมูลได้", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "เปลี่ยนแปลงข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), ContractorShowProfileActivity.class);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String loadPreferencesContractor() {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String profile = preferences.getString("SEND_TO_EDIT", "Not found");
        return profile;
    }
}
