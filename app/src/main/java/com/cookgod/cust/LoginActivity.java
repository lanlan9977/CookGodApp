package com.cookgod.cust;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.main.MainActivity;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveCustTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private RetrieveCustTask retrieveCustTask;
    private AutoCompleteTextView idCust_acc;
    private EditText idCust_pwd;
    private CustVO cust_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        idCust_acc = findViewById(R.id.idCust_Acc);
        idCust_pwd = findViewById(R.id.idCust_Pwd);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            String cust_acc = preferences.getString("custAcc", "");
            String cust_pwd = preferences.getString("custPwd", "");
            if (isMember(cust_acc, cust_pwd)) {
                finish();
            } else{
                Toast.makeText(LoginActivity.this, "帳號密碼錯誤，請重新登入", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLogInClick(View view) {
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        idCust_acc.setError(null);
        idCust_pwd.setError(null);
        String cust_acc = idCust_acc.getText().toString().trim();
        String cust_pwd = idCust_pwd.getText().toString().trim();
        if (cust_acc.isEmpty()) {
            idCust_acc.setError("帳號不得為空");
            return;
        } else if (cust_pwd.isEmpty()) {
            idCust_pwd.setError("密碼不得為空");
            return;
        } else if (!Util.networkConnected(this)) {
            Toast.makeText(LoginActivity.this, "網路連線錯誤", Toast.LENGTH_SHORT).show();
        } else {
            preferences.edit().putBoolean("login", true)
                    .putString("custAcc", cust_acc)
                    .putString("custPwd", cust_pwd).apply();
        }
        if (isMember(cust_acc, cust_pwd)) {
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "帳號密碼錯誤，請重新登入", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isMember(String cust_acc, String cust_pwd) {
        try {
            retrieveCustTask = new RetrieveCustTask(Util.Cust_Servlet_URL, cust_acc, cust_pwd);
            cust_account = retrieveCustTask.execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (cust_account != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("cust_account", cust_account);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        return cust_account != null;
    }

    public void onSimpleInputClick(View view) {
        idCust_acc.setText("ABC");
        idCust_pwd.setText("123");
    }


}