package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZoneModuleActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private SupportMapFragment mapFragment;
    private FloatingActionButton addBtn, removeBtn;
    private RelativeLayout notifyLabel;
    private GoogleMap mMap;
    private AlertDialog dialog;
    private ArrayList<Marker> allListPin, plantListPin, zoneListPin;
    private ArrayList<PlantModel> plantList;
    private ArrayList<ZoneModel> zoneList;
    private Marker currentMarker = null;
    private boolean isAdd = true;
    private String name;
    private double cur_latitude, cur_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_module);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        zoneListPin = new ArrayList<>();
        plantListPin = new ArrayList<>();

        notifyLabel = (RelativeLayout) findViewById(R.id.notifyLabel);
        notifyLabel.setVisibility(View.GONE);

        addBtn = (FloatingActionButton) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        removeBtn = (FloatingActionButton) findViewById(R.id.removeBtn);
        removeBtn.setVisibility(View.GONE);
        removeBtn.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("On map ready", "inside");
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this); // Listen when user touch to add a pin
        mMap.setInfoWindowAdapter(new CustomInfoWindow(ZoneModuleActivity.this));

        if (isOnline()) {
            new OkHttpHandler().execute(); // Zone location
            new OkHttpHandler2().execute(); // Plant location
        } else {
            Toast.makeText(ZoneModuleActivity.this, getResources().getString(R.string.error_unconnect),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBtn:
                isAdd = false;
                addBtn.setVisibility(View.GONE);
                notifyLabel.setVisibility(View.VISIBLE);
                break;
            case R.id.removeBtn:
                addBtn.setVisibility(View.VISIBLE);
                removeBtn.setVisibility(View.GONE);
                if (isOnline()) {
                    new OkHttpHandler4().execute(); // remove zone
                } else {
                    Toast.makeText(ZoneModuleActivity.this, getResources().getString(R.string.error_unconnect),
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private class OkHttpHandler4 extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID, ZONE_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
            ZONE_ID = currentMarker.getSnippet();
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/algorithm/zone/delete_zone?CONTRACTOR_ID=" +
                    CONTRACTOR_ID + "&ZONE_ID=" + ZONE_ID;

            OkHttpClient okHttpClient = new OkHttpClient();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .delete()
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
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

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("Zone Remove Zone", data);
            currentMarker.remove();
            Intent intent = new Intent(ZoneModuleActivity.this, ZoneModuleActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class OkHttpHandler3 extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cur_latitude = currentMarker.getPosition().latitude;
            cur_longitude = currentMarker.getPosition().longitude;
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/algorithm/zone/creat_zone?ZONE_NAME=" + name + "&CONTRACTOR_ID=" +
                    CONTRACTOR_ID + "&LAT=" + cur_latitude + "&LNG=" + cur_longitude;

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("ZONE_NAME", name)
                    .add("CONTRACTOR_NO", CONTRACTOR_ID)
                    .add("LAT", "" + cur_latitude)
                    .add("LNG", "" + cur_longitude)
                    .build();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .post(formBody)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
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

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("Zone Create Zone", data);
            Intent intent = new Intent(ZoneModuleActivity.this, ZoneModuleActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isAdd) {
            addBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        //Add marker on LongClick position
        if(!isAdd){
            AlertDialog.Builder builder = new AlertDialog.Builder(ZoneModuleActivity.this);
            builder.setCancelable(false);

            View view = getLayoutInflater().inflate(R.layout.zone_dialog, null);
            final EditText zoneName = (EditText) view.findViewById(R.id.zoneName);
            Button okBtn = (Button) view.findViewById(R.id.okBtn);
            ImageView exitBtn = (ImageView) view.findViewById(R.id.exitBtn);

            okBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!zoneName.getText().toString().isEmpty()) {
                        isAdd = true;
                        addBtn.setVisibility(View.VISIBLE);
                        currentMarker = mMap.addMarker(
                                new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                        .draggable(true));
                        if (isOnline()) {
                            name = zoneName.getText().toString();
                            new OkHttpHandler3().execute(); // Create
                        } else {
                            Toast.makeText(ZoneModuleActivity.this, getResources().getString(R.string.error_unconnect),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            exitBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    isAdd = true;
                    dialog.dismiss();
                    addBtn.setVisibility(View.VISIBLE);
                }
            });
            notifyLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("onMarkerDragStart", "inside");
        marker.showInfoWindow();
        marker.setAlpha(0.5f);
        currentMarker = marker;

        removeBtn.setVisibility(View.VISIBLE);
        addBtn.setVisibility(View.GONE);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("onMarkerDrag", "inside");
        marker.showInfoWindow();
        marker.setAlpha(0.5f);
        currentMarker = marker;
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("onMarkerDragEnd", "inside");
        marker.showInfoWindow();
        marker.setAlpha(1.0f);
        currentMarker = marker;

        if (isOnline()) {
            new OkHttpHandler5().execute(); // Update zone
        } else {
            Toast.makeText(ZoneModuleActivity.this, getResources().getString(R.string.error_unconnect),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class OkHttpHandler5 extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID, ZONE_NAME, ZONE_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cur_latitude = currentMarker.getPosition().latitude;
            cur_longitude = currentMarker.getPosition().longitude;
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
            ZONE_ID = currentMarker.getSnippet();
            ZONE_NAME = currentMarker.getTitle();
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/algorithm/zone/update_zone?ZONE_ID="+
                    ZONE_ID + "&CONTRACTOR_ID=" + CONTRACTOR_ID + "&ZONE_NAME=" + ZONE_NAME +
                    "&LAT=" + cur_latitude + "&LNG=" + cur_longitude;

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("ZONE_ID", ZONE_ID)
                    .add("CONTRACTOR_NO", CONTRACTOR_ID)
                    .add("ZONE_NAME", "" + ZONE_NAME)
                    .add("LAT", "" + cur_latitude)
                    .add("LNG", "" + cur_longitude)
                    .build();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .put(formBody)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
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

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("ZonePage Update", data);
            Intent intent = new Intent(ZoneModuleActivity.this, ZoneModuleActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
        }


        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/algorithm/zone/sorted_zone_position?CONTRACTOR_ID=" + CONTRACTOR_ID;

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
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

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("ZonePage", data);
            try {
                if (!data.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    zoneList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index

                        double lat = 0, lng = 0;
                        String lat_str, lng_str;
                        lat_str = jsonObject.getString("zone_lat");
                        lng_str = jsonObject.getString("zone_lng");

                        try {
                            lat = Double.parseDouble(lat_str);
                            lng = Double.parseDouble(lng_str);
                        } catch (NumberFormatException e) {
                            // Nothing
                        }
                        zoneList.add(new ZoneModel(jsonObject.getString("ZONE_ID"), jsonObject.getString("ZONE_NAME"), lat, lng));
                        //Log.d("ZonePage ", "latitude : " + lat + " longitude : " + lng);
                    }
                    pinZoneLocation(zoneList);

                } else {
                    Toast.makeText(ZoneModuleActivity.this, "ไม่มีข้อมูลแปลง", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pinZoneLocation(ArrayList<ZoneModel> zoneList) {
        if (zoneList != null) {
            LatLng position = null;
            for (int i = 0; i < zoneList.size(); i++) {
                position = new LatLng(zoneList.get(i).getLat(), zoneList.get(i).getLng());
                createMarkerZone(mMap, position, zoneList.get(i).getName(), zoneList.get(i).getId());
            }

            /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : zoneListPin) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);*/
        } else {
            Toast.makeText(ZoneModuleActivity.this, "ไม่มีข้อมูลโซน", Toast.LENGTH_LONG).show();
        }
    }

    protected GoogleMap createMarkerZone(GoogleMap googleMap, LatLng position, String title, String key) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).snippet(key).title(title).draggable(true));
        zoneListPin.add(marker);
        return googleMap;
    }

    private class OkHttpHandler2 extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CONTRACTOR_ID = loadPreferences("CONTRACTOR_ID");
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v2/algorithm/zone/sorted_plant_position?CONTRACTOR_ID=" + CONTRACTOR_ID;

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
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

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("ZonePage plant", data);
            try {
                if (!data.equals("[]")) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    plantList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index

                        String fullname, lat_str, lng_str;
                        fullname = jsonObject.getString("TITLE_NAME") + " " +
                                jsonObject.getString("FIRST_NAME") + " " + jsonObject.getString("LAST_NAME");

                        double lat = 0, lng = 0;
                        lat_str = jsonObject.getString("lat_center");
                        lng_str = jsonObject.getString("lng_center");

                        try {
                            lat = Double.parseDouble(lat_str);
                            lng = Double.parseDouble(lng_str);
                        } catch (NumberFormatException e) {
                            // Nothing
                        }
                        plantList.add(new PlantModel(jsonObject.getString("PLANT_ID"),
                                jsonObject.getString("ZONE_ID"), lat, lng, fullname,
                                jsonObject.getString("est_start_cut_date")));
                        Log.d("Pin plant ", "latitude : " + lat + " longitude : " + lng);
                    }
                    pinPlantLocation(plantList);

                } else {
                    Toast.makeText(ZoneModuleActivity.this, "ไม่มีข้อมูลแปลง", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pinPlantLocation(ArrayList<PlantModel> plantList) {
        if (plantList != null) {
            LatLng position = null;
            for (int i = 0; i < plantList.size(); i++) {
                position = new LatLng(plantList.get(i).getLatitude(),
                        plantList.get(i).getLongitude());
                createMarkerPlant(mMap, position, plantList.get(i).getId(), plantList.get(i).getZone());
            }

            allListPin = new ArrayList<>();
            allListPin.addAll(zoneListPin);
            allListPin.addAll(plantListPin);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : allListPin) {
                builder.include(marker.getPosition());
            }

            // Show all the markers
            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else {
            Toast.makeText(ZoneModuleActivity.this, "ไม่มีข้อมูลแปลง หรือ โซน", Toast.LENGTH_LONG).show();
        }
    }

    protected GoogleMap createMarkerPlant(GoogleMap googleMap, LatLng position, String title, String key) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).snippet(key).title(title));
        plantListPin.add(marker);
        return googleMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("onMarkerClick", "inside");
        marker.showInfoWindow();
        if (zoneListPin.contains(marker) && plantList != null) {
            String selectedZone = marker.getSnippet();
            for (int j = 0; j < plantList.size(); j++) {
                //Log.d("Zone clicked", selectedZone + " : " + plantList.get(j).getZone());
                if (selectedZone.equalsIgnoreCase(plantList.get(j).getZone())) {
                    if (plantListPin.get(j).isVisible()) {
                        plantListPin.get(j).setVisible(false);
                    } else {
                        plantListPin.get(j).setVisible(true);
                    }
                }
            }
        }
        removeBtn.setVisibility(View.VISIBLE);
        addBtn.setVisibility(View.GONE);
        currentMarker = marker;
        return false;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private String loadPreferences(String key) {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        return preferences.getString(key, "Not found");
    }

    private class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
        private LayoutInflater inflater = null;
        private TextView txt_zone, txt_id, txt_pos;
        private TextView txt_fullname, txt_plant, txt_cut_date;

        CustomInfoWindow(Context context) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (marker != null) {
                if(zoneListPin.contains(marker)) {
                    View view = inflater.inflate(R.layout.info_zone, null);
                    txt_zone = ((TextView) view.findViewById(R.id.zone));
                    txt_zone.setText(marker.getTitle());

                    txt_id = ((TextView) view.findViewById(R.id.id));
                    txt_id.setText(marker.getSnippet());

                    txt_pos = ((TextView) view.findViewById(R.id.position));
                    txt_pos.setText("ละติจูด " + marker.getPosition().latitude + "\n" +
                            "ลองจิจูด " + marker.getPosition().longitude);

                    return view;
                }
                else{
                    String selectedPlant = marker.getSnippet();
                    for (int i = 0; i < plantList.size(); i++) {
                        if (selectedPlant.equalsIgnoreCase(plantList.get(i).getZone())) {
                            View view = inflater.inflate(R.layout.info_plant, null);
                            txt_fullname = (TextView) view.findViewById(R.id.fullname);
                            txt_fullname.setText(plantList.get(i).getFullname());

                            txt_plant = (TextView) view.findViewById(R.id.plantId);
                            txt_plant.setText(plantList.get(i).getId());

                            txt_cut_date = (TextView) view.findViewById(R.id.estCut);
                            txt_cut_date.setText(plantList.get(i).getEstCut());
                            return view;
                        }
                    }
                }
            }
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            return (null);
        }
    }
}
