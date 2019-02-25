package com.cookgod.cust;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private ProgressDialog progressDialog;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK ) {
            Log.d(TAG,"成功");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG,"失敗");
        }
    }

    private void findViews() {
        idCust_acc = findViewById(R.id.idCust_Acc);
        idCust_pwd = findViewById(R.id.idCust_Pwd);
    }

    private boolean networkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //判斷網路功能是否可用
        NetworkInfo info = manager.getActiveNetworkInfo();//獲取網路連接的訊息
        return info != null && info.isConnected();
    }

    public void onLogInClick(View view) {
        idCust_acc.setError(null);
        idCust_pwd.setError(null);
        String cust_acc = idCust_acc.getText().toString().trim();
        String cust_pwd = idCust_pwd.getText().toString().trim();
        if (cust_acc.isEmpty()) {
            idCust_acc.setError("帳號不得為空");
        } else if (cust_pwd.isEmpty()) {
            idCust_pwd.setError("密碼不得為空");
        } else if (!networkConnected()) {
            Toast.makeText(LoginActivity.this, "網路連線錯誤", Toast.LENGTH_SHORT).show();
        } else {
            retrieveCustTask = (RetrieveCustTask) new RetrieveCustTask().execute(Util.Cust_Servlet_URL, cust_acc, cust_pwd);
        }
    }

    public void onSimpleInputClick(View view) {
        idCust_acc.setText("ABC");
        idCust_pwd.setText("123");

    }

    private class RetrieveCustTask extends AsyncTask<String, Integer, CustVO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);//進度條元件
            progressDialog.setMessage("登入中");
            progressDialog.show();
        }

        @Override
        protected CustVO doInBackground(String... strings) {
            String url = strings[0];
            String cust_Acc = strings[1];
            String cust_Pwd = strings[2];
            cust_account = new CustVO(cust_Acc, cust_Pwd);//顧客輸入的帳號密碼(CustVO)
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("selectCust", gson.toJson(cust_account));
            jsonIn = getRemoteData(url, jsonObject.toString());
            Type custType = new TypeToken<CustVO>() {
            }.getType();
            return gson.fromJson(jsonIn, custType);
        }

        @Override
        protected void onPostExecute(CustVO cust_account) {
            if (cust_account != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cust_account", cust_account);
                intent.putExtras(bundle);
                progressDialog.setMessage("登入中");
                progressDialog.cancel();
                setResult(RESULT_OK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "帳號密碼錯誤，請重新登入", Toast.LENGTH_SHORT).show();
            }
            progressDialog.cancel();
        }
    }

    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;//產生連線物件
        StringBuilder instr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();//取得連線物件，需做例外處理
            connection.setDoInput(true);//是否向HttpURLConnection讀入
            connection.setDoOutput(true);//是否向HttpURLConnection輸出
            connection.setChunkedStreamingMode(0);//不知道請求內容大小時可以呼叫此方法將請求內容分端傳輸，設定0代表預設長度
            connection.setUseCaches(false);//是否設定暫存，預設為true
            connection.setRequestMethod("POST");//設定請求類型
            connection.setRequestProperty("charset", "UTF-8");//設定編碼類型信息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())); //字串輸出流(從位元轉換)
            bw.write(outStr);
            Log.d(TAG, "outStr：" + outStr);//blue(debug)
            bw.close();//關水管
            int responseCode = connection.getResponseCode(); //判斷是否連線成功
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    instr.append(line);//開始寫入從Servlet請求過來的字串資料
                }
            } else {
                Log.d(TAG, "response：" + responseCode);//顯示錯誤訊息(錯誤代碼)
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());//Error錯誤
        } finally {
            if (connection != null) {
                connection.disconnect();//關閉連線
            } else {
                Toast.makeText(LoginActivity.this, "沒有可用的網路連線", Toast.LENGTH_SHORT).show();
            }
        }
        Log.d(TAG, "inStr：" + instr);
        return instr.toString();//將輸入進來的資料回傳
    }
}