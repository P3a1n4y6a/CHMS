package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Viewz-PC on 7/25/2017.
 */

public class OkhttpHandler extends AsyncTask<Object, Object, String> {
    // Parameter: url, http type
    private Context context;
    private OkHttpClient client = new OkHttpClient();
    private Request request;
    private Response response;
    private String url, type, result;

    public AsyncResponse delegate = null;

    public OkhttpHandler(Context context, String url, String type) {
        this.context = context;
        this.url = url;
        this.type = type;
    }

    @Override
    protected String doInBackground(Object... params) {
        Log.d("URL_TAG", url);
        if (type.equals("get")) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else if (type.equals("post")) {

        } else if (type.equals("delete")) {

        } else if (type.equals("put")) {

        }

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
        Log.d("RESULT_TAG", result);
        delegate.processFinish(result);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

