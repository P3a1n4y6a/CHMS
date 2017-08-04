package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FarmerRequestPlantFragment extends Fragment {
    private View farmerRequestView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PlantAdapter adapter;
    private ArrayList<PlantModel> plantList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerRequestView = inflater.inflate(R.layout.fragment_farmer_request_plant, container, false);

        new OkHttpHandler().execute();
        return farmerRequestView;
    }

    public void initRecycler(){
        recyclerView = (RecyclerView) farmerRequestView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlantAdapter(getContext(), plantList);
        recyclerView.setAdapter(adapter);
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String contractor_id, farmer_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contractor_id = loadPreferences("CONTRACTOR_ID");
            farmer_id = loadPreferences("FRAMER_ID");
        }

        @Override
        protected String doInBackground(Object... params) {

            // Set status, using query plant in database
            String FARMER_STATUS = getArguments().getString("FARMER_STATUS2");
            String STATUS = null;
            if(FARMER_STATUS.equals("ACCEPT")){
                STATUS = "PROCESS";
            }else{
                STATUS = "NO_QUEUE";
            }

            String url = "http://188.166.191.60/api/v2/contractor/farmer/request_plant_list?" +
                    "FARMER_ID=" + farmer_id + "&CONTRACTOR_ID=" + contractor_id + "&STATUS=" + STATUS;
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

            try {
                if (result != null) {
                    JSONArray jsonArray = new JSONArray(result);
                    plantList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index

                        plantList.add(new PlantModel(jsonObject.getString("PLANT_ID"),
                                jsonObject.getString("request_plant_area")));
                    }

                    saveToSharedPrefs(result);
                    initRecycler(); //Create multiple cards
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String loadPreferences(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }

    private void saveToSharedPrefs(String data) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PLANT_LIST", data);
        editor.commit();
    }
}
