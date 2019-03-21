package com.cookgod.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveBroadcastTask extends AsyncTask<String,String,String> {
    private final static String TAG = "BroadcastDetailActivity";
    private String url,broadcast_ID,cust_id,action;



    public RetrieveBroadcastTask(String url, String broadcast_ID) {
        this.url=url;
        this.broadcast_ID=broadcast_ID;
        this.action="update";
    }

    public RetrieveBroadcastTask(String url, String cust_id, String action) {
        this.url=url;
        this.cust_id=cust_id;
        this.action=action;
    }


    @Override
    protected String doInBackground(String... strings) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", action);
        jsonObject.addProperty("cust_ID", cust_id);
        jsonObject.addProperty("broadcast_ID", broadcast_ID);
        return getRemoteData(url, jsonObject.toString());
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