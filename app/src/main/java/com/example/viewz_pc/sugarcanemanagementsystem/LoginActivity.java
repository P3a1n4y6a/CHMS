package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String username, password;
    private TextView registLink;
    private EditText txt_username, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registLink = (TextView) findViewById(R.id.signupLink);
        txt_username = (EditText) findViewById(R.id.username);
        txt_password = (EditText) findViewById(R.id.password);

        registLink.setOnClickListener(this);

        Button login_button = (Button) findViewById(R.id.loginBtn);
        login_button.setOnClickListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(isOnline()) {
            if (v.getId() == R.id.loginBtn) {
                username = txt_username.getText().toString();
                password = txt_password.getText().toString();
                new OkHttpHandler().execute();
            } else if (v.getId() == R.id.signupLink) {
                Intent intent = new Intent(LoginActivity.this, ContractorSignUpActivity.class);
                intent.putExtra("AppURL", getResources().getString(R.string.web_app_url)
                        + "Register.php?department=contractor");
                startActivity(intent);
            }
        }else{
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_unconnect),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        Request request;
        Response response;

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v2/account/login?username="+username+"&password="+password;
            Log.d("TAG",url);
            request = new Request.Builder()
                    .url(url)
                    .build();
            try {

                response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG",result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("error")) {
                        Toast.makeText(LoginActivity.this, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                        saveToSharedPrefs("error");
                    }else {
                        saveToSharedPrefs(result); // Go to main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Account_data", data);
        editor.commit();
    }
}
