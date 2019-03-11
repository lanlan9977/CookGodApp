package com.cookgod.broadcast;


import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Locale;

public class BroadcastSocket extends WebSocketClient {
    private static final String TAG = "BroadcastSocket";
    private Gson gson;
    private Context context;

    public BroadcastSocket(URI serverURI, Context context) {
        // Draft_17是連接協議，就是標準的RFC 6455（JSR356）
        super(serverURI, new Draft_17());
        this.context = context;
        gson = new Gson();
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
        BroadcastVO broadcastVO=gson.fromJson(message, BroadcastVO.class);
        showNotification(broadcastVO);
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

    private void showNotification(BroadcastVO broadcastVO) {
        Log.e(TAG, "onMessage: " + broadcastVO.getBroadcast_con());
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(broadcastVO.getBroadcast_con())
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
