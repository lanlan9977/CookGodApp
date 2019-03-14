package com.cookgod.broadcast;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import com.cookgod.main.MainActivity;
import com.cookgod.order.OrderActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BroadcastSocket extends WebSocketClient {
    private static final String TAG = "BroadcastSocket";
    private Gson gson;
    private Context context;


    public BroadcastSocket(URI serverURI, Context context) {
        // Draft_17是連接協議，就是標準的RFC 6455（JSR356）
        super(serverURI, new Draft_17());
        this.context = context;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        String text = String.format(Locale.getDefault(),
                "onOpen: Http status code = %d; status message = %s",
                handshakeData.getHttpStatus(),
                handshakeData.getHttpStatusMessage());
        Log.e(TAG, "onOpen: " + text);
    }


    @Override
    public void onMessage(String message) {
        Log.e(TAG, "onMessage: " + message);
        String con = "";
        Type stringType = new TypeToken<List<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(message, stringType);
        String type = stringList.get(0);
        Log.e(TAG, type);
        if ("menu_order".equals(type)) {
            BroadcastVO broadcastVO = gson.fromJson(stringList.get(1), BroadcastVO.class);
            con = broadcastVO.getBroadcast_con();
            Log.e(TAG, con);
        } else if ("location".equals(type)) {
            String stringLocation = stringList.get(2);
            con="主廚已傳送位置訊息";

        } else {
            Log.e(TAG, "");
        }

        showNotification(con);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text = String.format(Locale.getDefault(),
                "code = %d, reason = %s, remote = %b",
                code, reason, remote);
        Log.d(TAG, "onClose: " + text);
    }

    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "onError: exception = " + ex.toString());
    }

    private void showNotification(String con) {
        Log.e(TAG, con);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(con)
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(soundUri)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification.build());
    }


}
