package com.cookgod.cust;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.chef.ChefVO;
import com.cookgod.main.Util;
import com.cookgod.other.Contents;
import com.cookgod.task.RetrieveCustTask;
import com.cookgod.task.RetrieveNewCustTask;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private RetrieveCustTask retrieveCustTask;
    private RetrieveNewCustTask retrieveNewCustTask;
    private AutoCompleteTextView idCust_acc;
    private EditText idCust_pwd;
    private CustVO cust_account;
    private ChefVO chef_account;
    private com.google.android.gms.common.SignInButton btnGoogle_sign;
    private   GoogleSignInClient mGoogleSignInClient;
    private static final int G_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViews();
    }

    private void findViews() {
        idCust_acc = findViewById(R.id.idCust_Acc);
        idCust_pwd = findViewById(R.id.idCust_Pwd);
        btnGoogle_sign = findViewById(R.id.btnGoogle_sign);
        btnGoogle_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*google*/
        if (requestCode == G_SIGN_IN) {
            //登入成功後取得資料
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateGoogleInfo(account);
            Log.e(TAG, "HUHUHUHUHU");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.e(TAG, "signInResult:failed code=" + e.toString());

            updateGoogleInfo(null);
        }
    }

    private void updateGoogleInfo(GoogleSignInAccount account) {
        if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            String picture = String.valueOf(account.getPhotoUrl());
            retrieveNewCustTask = new RetrieveNewCustTask(Util.Servlet_URL + "NewCustServlet", name, email, id);
            if (isMember(name, name)) {
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT);
                custShared();
                finish();


            } else {
                try {
                    String jsonin = retrieveNewCustTask.execute().get();
                    Type custType = new TypeToken<CustVO>() {
                    }.getType();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    cust_account = gson.fromJson(jsonin, custType);
                    Log.e(TAG, jsonin);
                    custShared();
                    finish();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (account.getPhotoUrl() == null) {
                    picture = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg";
                }
            }
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, G_SIGN_IN);

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
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT);
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
            custShared();

            finish();
            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT);
        } else {
            Util.showToast(LoginActivity.this, "帳號密碼輸入錯誤");
        }
    }

    private void custShared() {
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
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
                .putString("cust_status", cust_account.getCust_status())
                .putString("cust_niname", cust_account.getCust_niname())
                .apply();
        if (chef_account != null) {
            preferences.edit().putBoolean("isChef", true)
                    .putString("chef_ID", chef_account.getChef_ID())
                    .putString("chef_name", chef_account.getChef_name())
                    .putString("chef_tel", chef_account.getChef_tel())
                    .putString("chef_addr", chef_account.getChef_addr())
                    .putString("chef_area", chef_account.getChef_area())
                    .putString("chef_status", chef_account.getChef_status())
                    .putString("chef_channel", chef_account.getChef_channel())
                    .putString("chef_resume", chef_account.getChef_resume()).apply();
        }
    }

    public boolean isMember(String cust_acc, String cust_pwd) {
        retrieveCustTask = new RetrieveCustTask(Util.Servlet_URL + "CustServlet", cust_acc, cust_pwd);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveCustTask.execute().get();
            Type listType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = gson.fromJson(jsonIn, listType);
            List<String> list = new ArrayList<>();
            for (String key : map.keySet()) {
                list.add(key);
            }
            cust_account = gson.fromJson(list.get(0), CustVO.class);


            if (!list.get(1).isEmpty()) {
                chef_account = gson.fromJson(map.get(list.get(1)), ChefVO.class);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return cust_account != null;
    }

    public void onSimpleInputClick(View view) {
        idCust_acc.setText("ABC");
        idCust_pwd.setText("123");
    }

    public void onSimpleInputChefClick(View view) {
        idCust_acc.setText("FOOD_SUP");
        idCust_pwd.setText("123456");
    }
}