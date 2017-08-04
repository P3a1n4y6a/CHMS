package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridDashboardAdapter adapter;
    private View headerView;
    private LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initCustomGrid();

        headerView = navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
        headerLayout = (LinearLayout) headerView.findViewById(R.id.header);
        new OkHttpHandler().execute();
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;
        String username;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = loadPreferencesContractor("username");
            Log.d("Dashboard", username);
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/contractor/profile?username=" + username;
            Log.d("Dashboard",url);
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
            Log.d("Nav header", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                TextView name = (TextView) headerLayout.findViewById(R.id.name);
                TextView email = (TextView) headerLayout.findViewById(R.id.email);

                String fullname = jsonObject.getString("TITLE_NAME") + " " +
                        jsonObject.getString("FIRST_NAME") + " " +
                        jsonObject.getString("LAST_NAME");
                name.setText(fullname);
                email.setText(jsonObject.getString("EMAIL"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Clear shared preferences in local memory
            SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            // Back to log in page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String[] button_title = {"ชาวไร่","สำรวจแปลง","จัดโซน","เลือกแปลงตัด","จัดคิว", "การเงิน","จัดการพนักงาน","บัญชีผู้ใช้งาน"};
    int[] icon = {R.drawable.ic_color_farmer, R.drawable.ic_color_survey, R.drawable.ic_color_zone,
            R.drawable.ic_color_selected, R.drawable.ic_color_cutter, R.drawable.ic_color_cost, R.drawable.ic_color_employee, R.drawable.ic_color_profile};

    public void initCustomGrid() {

        GridView grid = (GridView) findViewById(R.id.gridView);
        adapter = new GridDashboardAdapter(this, button_title, icon); //Holder a custom layout
        grid.setAdapter(adapter); //Bind grid with adapter
        //Control action of buttons inside the grid
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(DashboardActivity.this, FarmerModuleActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(DashboardActivity.this, SurveyModuleActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(DashboardActivity.this, ZoneModuleActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        String USERNAME = loadPreferencesContractor("username");
                        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
                        intent = new Intent(DashboardActivity.this, WebContractorActivity.class);
                        intent.putExtra("AppURL", getString(R.string.web_app_url) + "Select.php?USERNAME=" +
                                USERNAME + "&CONTRACTOR_ID=" + CONTRACTOR_ID);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(DashboardActivity.this, CuttingQueueModuleActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(DashboardActivity.this, AccountingPlantActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(DashboardActivity.this, EmployeeModuleActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(DashboardActivity.this, ContractorShowProfileActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
