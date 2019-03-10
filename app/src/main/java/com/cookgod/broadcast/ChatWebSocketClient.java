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

public class ChatWebSocketClient extends WebSocketClient {
    private static final String TAG = "ChatWebSocketClient";
    private Gson gson;
    private Context context;
    public static String friendInChat;

    public ChatWebSocketClient(URI serverURI, Context context) {
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
        Log.d(TAG, "onOpen: " + text);
    }

    // 訊息內容多(例如：圖片)，server端必須以byte型式傳送，此方法可以接收byte型式資料
    @Override
    public void onMessage(ByteBuffer bytes) {
        int length = bytes.array().length;
        String message = new String(bytes.array());
        Log.d(TAG, "onMessage(ByteBuffer): length = " + length);
        onMessage(message);
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "onMessage: " + message);
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        // type: 訊息種類，有open(有user連線), close(有user離線), chat(其他user傳送來的聊天訊息)
        String type = jsonObject.get("type").getAsString();

        showNotification();
//        sendMessageBroadcast(type, message);
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

//    private void sendMessageBroadcast(String messageType, String message) {
//        Intent intent = new Intent(messageType);
//        intent.putExtra("message", message);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//    }

    private void showNotification() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
//                .setContentTitle("message from " + chatMessage.getSender())
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
