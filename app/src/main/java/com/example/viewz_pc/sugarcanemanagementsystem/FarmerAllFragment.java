package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class FarmerAllFragment extends Fragment {
    private View farmerAllView;
    public static WebView web;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerAllView = inflater.inflate(R.layout.fragment_farmer_all, container, false);
        web = (WebView) farmerAllView.findViewById(R.id.web);
        progressBar = (ProgressBar) farmerAllView.findViewById(R.id.progress);

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        String USERNAME = loadPreferencesContractor("username");
        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        String url = getString(R.string.web_app_url) + "FarmerAll.php?USERNAME="+USERNAME
                +"&CONTRACTOR_ID=" + CONTRACTOR_ID;
        Log.d("Farmer All", url);

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
        return farmerAllView;
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
