package com.cookgod.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
    public final static String MenuOrder_Servlet_URL = "http://10.0.2.2:8081/CookGodTest/MenuOrderServlet";
    public final static String Cust_Servlet_URL = "http://10.0.2.2:8081//CookGodTest/CustServlet";
    //    public final static String URL = "http://192.168.1.108:8081/CookGodTest/MenuOrderServlet";
    public final static String PREF_FILE = "preference";


    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}


