package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class FarmerProcessFragment extends Fragment {
    private View farmerProcessView;
    public static WebView web;
    //private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String url = "www.google.com";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerProcessView = inflater.inflate(R.layout.fragment_farmer_process, container, false);
        web = (WebView) farmerProcessView.findViewById(R.id.web);
        swipeRefreshLayout = (SwipeRefreshLayout)farmerProcessView.findViewById(R.id.swipeContainer);

        //progressBar = (ProgressBar) farmerProcessView.findViewById(R.id.progress);

        //progressBar.setVisibility(ProgressBar.INVISIBLE);
        String USERNAME = loadPreferencesContractor("username");
        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        url = getString(R.string.web_app_url) + "FarmerProcess.php?USERNAME="+USERNAME
                +"&CONTRACTOR_ID=" + CONTRACTOR_ID;
        Log.d("Farmer Processs", url);

        web.getSettings().setJavaScriptEnabled(true);
        /*web.setWebChromeClient(new WebChromeClient() {
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
        });*/

        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient());

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(android.R.color.holo_red_light),
                getActivity().getResources().getColor(android.R.color.holo_orange_light),
                getActivity().getResources().getColor(android.R.color.holo_green_light));
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        web.loadUrl(url);
                        web.setWebViewClient(new WebViewClient());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return farmerProcessView;
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
