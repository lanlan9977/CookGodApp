package com.cookgod.cust;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.broadcast.BroadcastVO;
import com.cookgod.main.MainActivity;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveCustTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            String cust_acc = preferences.getString("cust_acc", "");
            String cust_pwd = preferences.getString("cust_pwd", "");
            if (isMember(cust_acc, cust_pwd)) {
                finish();
            }
        }
    }

    public void onLogInClick(View view) {
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
            return;
        }
        if (isMember(cust_acc, cust_pwd)) {
            finish();
        } else {
//            Toast.makeText(LoginActivity.this, "帳號密碼錯誤，請重新登入", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isMember(String cust_acc, String cust_pwd) {
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        try {
            retrieveCustTask = new RetrieveCustTask(Util.Cust_Servlet_URL, cust_acc, cust_pwd);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveCustTask.execute().get();
            Type mapType = new TypeToken<Map<String, List<BroadcastVO>>>() {
            }.getType();
            Type custType = new TypeToken<CustVO>() {
            }.getType();
            Map<String, List<BroadcastVO>> map = gson.fromJson(jsonIn, mapType);
            for (String key : map.keySet()) {
                cust_account = gson.fromJson(key, custType);
                List<BroadcastVO> broadcastList = map.get(key);
            }
        } catch (Exception e) {
            Util.showToast(LoginActivity.this, "FUCK");
            Log.e(TAG, e.toString());
        }
        if (cust_account != null) {
            preferences.edit().putBoolean("login", true)
                    .putString("cust_ID", cust_account.getCust_ID())
                    .putString("cust_acc", cust_account.getCust_acc())
                    .putString("cust_pwd", cust_account.getCust_pwd())
                    .putString("cust_name", cust_account.getCust_name())
                    .putString("cust_sex", cust_account.getCust_sex())
                    .putString("cust_tel", cust_account.getCust_tel())
                    .putString("cust_addr", cust_account.getCust_addr())
                    .putString("cust_pid", cust_account.getCust_pid())
                    .putString("cust_mail", cust_account.getCust_mail())
                    .putString("cust_brd", cust_account.getCust_brd().toString())
                    .putString("cust_reg", cust_account.getCust_reg().toString())
                    .putString("cust_status", cust_account.getCust_status())
                    .putString("cust_niname", cust_account.getCust_niname())
                    .apply();
            setResult(RESULT_OK);
        }
        return cust_account != null;
    }

    public void onSimpleInputClick(View view) {
        idCust_acc.setText("ABC");
        idCust_pwd.setText("123");
    }

}