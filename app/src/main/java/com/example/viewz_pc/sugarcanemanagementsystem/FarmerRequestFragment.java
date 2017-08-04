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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FarmerRequestFragment extends Fragment {
    private View farmerRequestView;
    public static WebView web;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerRequestView = inflater.inflate(R.layout.fragment_farmer_request, container, false);
        web = (WebView) farmerRequestView.findViewById(R.id.web);
        progressBar = (ProgressBar) farmerRequestView.findViewById(R.id.progress);

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        String USERNAME = loadPreferencesContractor("username");
        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        String url = getString(R.string.web_app_url) + "FarmerNew.php?USERNAME="+USERNAME
                +"&CONTRACTOR_ID=" + CONTRACTOR_ID;
        Log.d("Farmer Request", url);

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                System.out.println(progress);
                progressBar.setProgress(progress);

                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    progressBar.setProgress(0);
                } else {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
            }
        });

        web.setWebViewClient(new WebViewClient());
        web.loadUrl(url);

        return farmerRequestView;
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
