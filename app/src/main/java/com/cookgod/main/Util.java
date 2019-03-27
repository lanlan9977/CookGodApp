package com.cookgod.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.cookgod.broadcast.BroadcastSocket;

import java.net.URI;
import java.net.URISyntaxException;

public class Util {
//    public final static String Servlet_URL = "http://10.120.25.16:8081/CookGodTest/";//電腦
    public final static String Servlet_URL = "http://10.120.25.31:8081/CA106G3/";
//    public final static String Servlet_URL = "http://35.229.216.156:8081//CA106G3/";
    //        public final static String Servlet_URL ="http://10.0.2.2:8081/CookGodTest/";
//    public final static String Servlet_URL ="http://192.168.1.110:8081/CookGodTest/";
    public final static String PREF_FILE = "preference";


    private static final String TAG = "Utils";
//            public final static String SERVER_URI ="ws://192.168.1.110:8081/CookGodTest/BroadcastSocket/";
//    public final static String SERVER_URI ="ws://10.120.25.16:8081/CookGodTest/BroadcastSocket/";
    public final static String SERVER_URI ="ws://10.120.25.31:8081/CA106G3/BroadcastSocket/";
//    public final static String SERVER_URI = "ws://35.229.216.156:8081//CA106G3/BroadcastSocket/";
    public static BroadcastSocket broadcastSocket;

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void connectServer(String userName, Context context) {
        if (broadcastSocket == null) {
            URI uri = null;
            try {
                uri = new URI(SERVER_URI + userName);
            } catch (URISyntaxException e) {
                Log.e(TAG, e.toString());
            }
            broadcastSocket = new BroadcastSocket(uri, context);
            broadcastSocket.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (broadcastSocket != null) {
            broadcastSocket.close();
            broadcastSocket = null;
        }
    }
    //            public void showNotification() {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("title", "通知訊息1");
//                bundle.putString("content", "通知訊息2");
//                intent.putExtras(bundle);
//
//        /*
//            Intent指定好要幹嘛後，就去做了，如startActivity(intent);
//            而PendingIntent則是先把某個Intent包好，以後再去執行Intent要幹嘛
//         */
//                PendingIntent pendingIntent = PendingIntent.getActivity(getContext()
//                        , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Uri uri = Uri.parse(URL);
//                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
//                PendingIntent pendingIntent2 = PendingIntent.getActivity(
//                        getContext(), 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
//
//                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
//
//                NotificationCompat.Action action = new NotificationCompat.Action.Builder(
//                        android.R.drawable.ic_menu_share, "Go!", pendingIntent2
//                ).build();
//
//                NotificationCompat.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//                    manager.createNotificationChannel(channel);
//                    builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
//                } else {
//                    builder = new NotificationCompat.Builder(getContext());
//                    builder.setPriority(Notification.PRIORITY_MAX);
//                }
//
//                Notification notification = builder
//                        // 訊息面板的標題
//                        .setContentTitle("套餐訂單已")
//                        // 訊息面板的內容文字
//                        .setContentText("前往開發官網")
//                        // 訊息的小圖示
//                        .setSmallIcon(R.drawable.ic_chef_icon)
//                        // 訊息的大圖示
//                        .setLargeIcon(bitmap)
//                        // 使用者點了之後才會執行指定的Intent
//                        .setContentIntent(pendingIntent)
//                        // 加入音效
//                        .setSound(soundUri)
//                        // 點擊後會自動移除狀態列上的通知訊息
//                        .setAutoCancel(true)
//                        // 加入狀態列下拉後的進一步操作
//                        .addAction(action)
//                        .build();
//
//                manager.notify(1, notification);
//            }

}


