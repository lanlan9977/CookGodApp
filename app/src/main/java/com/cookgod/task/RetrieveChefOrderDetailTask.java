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

public class RetrieveChefOrderDetailTask extends AsyncTask<String, Integer, String> {
    private final static String TAG = "OrderActivity";
    String FestOrder_Servlet_URL,chef_ID;


    public RetrieveChefOrderDetailTask(String FestOrder_Servlet_URL, String chef_ID) {
        this.FestOrder_Servlet_URL=FestOrder_Servlet_URL;
        this.chef_ID=chef_ID;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = FestOrder_Servlet_URL;
        String cust_id = chef_ID;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("selectOrder", cust_id);
        return getRemoteData(url, jsonObject.toString());
    }


    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }
}