package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebContractorActivity extends AppCompatActivity {
    WebView CHMSWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_contractor);

        String AppURL = getIntent().getStringExtra("AppURL");

        CHMSWebView = (WebView) findViewById(R.id.webView);
        CHMSWebView.setWebViewClient(new WebViewClient());
        CHMSWebView.setWebChromeClient(new MyWebChromeClient());
        CHMSWebView.getSettings().setJavaScriptEnabled(true);
        CHMSWebView.loadUrl(AppURL);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (CHMSWebView.canGoBack()) {
                        CHMSWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
