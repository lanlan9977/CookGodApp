package com.cookgod.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cookgod.cust.CustVO;
import com.cookgod.cust.LoginActivity;
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

public class RetrieveCustTask extends AsyncTask<String, Integer, CustVO> {
    private final static String TAG = "LoginActivity";
    String cust_servlet_url, cust_acc, cust_pwd;
    CustVO cust_account;

    public RetrieveCustTask(String cust_servlet_url, String cust_acc, String cust_pwd) {
        this.cust_servlet_url = cust_servlet_url;
        this.cust_acc = cust_acc;
        this.cust_pwd = cust_pwd;
    }

    @Override
    protected CustVO doInBackground(String... strings) {
        String url = cust_servlet_url;
        String cust_Acc = cust_acc;
        String cust_Pwd = cust_pwd;
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
            }
        }
        Log.d(TAG, "inStr：" + instr);
        return instr.toString();//將輸入進來的資料回傳
    }
}