package com.cookgod;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.cookgod.cust.CustVO;
import com.google.gson.Gson;
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
    public static CustVO custVO;
    private ProgressDialog progressDialog;
    private final static String TAG = "LoginActivity";
    private RetrieveCustTask retrieveCustTask;
    private AutoCompleteTextView idCust_Acc,idCust_Pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idCust_Acc=findViewById(R.id.idCust_Acc);
        idCust_Pwd=findViewById(R.id.idCust_Pwd);
        custVO = new CustVO();
    }

    private boolean networkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //判斷網路功能是否可用
        NetworkInfo info = manager.getActiveNetworkInfo();//獲取網路連接的訊息
        return info != null && info.isConnected();
    }

    public void onLogIn(View view) {
        String Cust_Acc=idCust_Acc.getText().toString();

        if (networkConnected()) {
            retrieveCustTask= new RetrieveCustTask().execute(Util.URL,Cust_Acc);
        }

    }


    private class RetrieveCustTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);//進度條元件
            progressDialog.setMessage("登入中");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("selectCust", "cust");
            jsonIn = getRemoteData(url, jsonObject.toString());
            Gson gson = new Gson();
            Type custType = new TypeToken<CustVO>() {
            }.getType();
            return gson.fromJson(jsonIn, custType);
        }

        @Override
        protected void onPostExecute(String cust_acc) {
            super.onPostExecute(custVO);
        }
    }


    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;//產生連線物件
        StringBuilder instr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();//取得連線物件，需做例外處理
            connection.setDoInput(true);//是否向HttpURLConnection讀入
            connection.setDoOutput(true);//是否向HttpURLConnection輸出
            connection.setChunkedStreamingMode(0);
            //不知道請求內容大小時可以呼叫此方法將請求內容分端傳輸，設定0代表預設長度
            connection.setUseCaches(false);//是否設定暫存，預設為true
            connection.setRequestMethod("POST");//設定請求類型
            connection.setRequestProperty("charset", "UTF-8");//設定編碼類型信息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            //字串輸出流(從位元轉換)
            bw.write(outStr);
            Log.d(TAG, "outStr：" + outStr);//blue(debug)
            bw.close();//關水管

            int responseCode = connection.getResponseCode();
            //判斷是否連線成功
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