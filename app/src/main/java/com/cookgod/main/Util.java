package com.cookgod.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {
    public final static String Servlet_URL ="http://10.0.2.2:8081/CookGodTest/";
    public final static String PREF_FILE = "preference";


    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public static void showToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }


//    private final int REQUEST_LOGIN = 1;
//    private final int REQUEST_ORDER = 2;

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        switch (resultCode) {
//            case RESULT_OK:
//                if (requestCode == REQUEST_LOGIN) {
//                    Bundle bundle = data.getExtras();
//                    if (!bundle.isEmpty()) {
//                        cust_account = (CustVO) bundle.getSerializable("cust_account");
//
//                    }else{
//                        Toast.makeText(MainActivity.this,"Fuck",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//
//
//        }
//    }
}


