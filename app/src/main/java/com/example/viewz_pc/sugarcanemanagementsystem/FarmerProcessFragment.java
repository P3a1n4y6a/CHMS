package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class FarmerProcessFragment extends Fragment {
    private View farmerProcessView;
    public static WebView web;
    //private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        farmerProcessView = inflater.inflate(R.layout.fragment_farmer_process, container, false);
        web = (WebView) farmerProcessView.findViewById(R.id.web);
        swipeRefreshLayout = (SwipeRefreshLayout)farmerProcessView.findViewById(R.id.swipeContainer);

        String USERNAME = loadPreferencesContractor("username");
        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        currentUrl = getString(R.string.web_app_url) + "FarmerProcess.php?USERNAME="+USERNAME
                +"&CONTRACTOR_ID=" + CONTRACTOR_ID;
        Log.d("Farmer Processs", currentUrl);

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new MyWebChromeClient());
        web.setWebViewClient(new MyWebViewClient());
        web.loadUrl(currentUrl);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(android.R.color.holo_red_light),
                getActivity().getResources().getColor(android.R.color.holo_orange_light),
                getActivity().getResources().getColor(android.R.color.holo_green_light));
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        web.loadUrl(currentUrl);
                    }
                }
        );

        return farmerProcessView;
    }

    private class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);
            currentUrl = url;
            super.onPageFinished(view, url);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("LOG_TAG", message);
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
