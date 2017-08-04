package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SurveyRequestFragment extends Fragment {
    public static WebView web;
    private View surveyView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        surveyView = inflater.inflate(R.layout.fragment_survey_request, container, false);
        web = (WebView) surveyView.findViewById(R.id.web);
        progressBar = (ProgressBar) surveyView.findViewById(R.id.progress);

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        String USERNAME = loadPreferencesContractor("username");
        String CONTRACTOR_ID = loadPreferencesContractor("CONTRACTOR_ID");
        String url = getString(R.string.web_app_url) + "SurveyNew.php?USERNAME="+USERNAME
                +"&CONTRACTOR_ID=" + CONTRACTOR_ID;
        Log.d("Survey Request", url);

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
        return surveyView;
    }

    private String loadPreferencesContractor(String key) {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString(key, "Not found");
        return data;
    }
}
