package com.cookgod.broadcast;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.cookgod.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.squareup.okhttp.OkHttpClient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BroadcastSocket extends WebSocketClient {
    private static final String TAG = "BroadcastSocket";
    private Gson gson;
    private Context context;
    private LocationManager locationManager;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private String commadStr;
    private NotificationManager notificationManager;


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
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(30, TimeUnit.SECONDS);
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
//            String stringLocation =

//            Location location = gson.fromJson(stringLocation, Location.class);
            DateTime now = new DateTime();
            DirectionsResult result = null;
            commadStr = LocationManager.GPS_PROVIDER;
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            String stringDestination = stringList.get(2) + "," + stringList.get(3);
            Log.e(TAG, stringDestination);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            Location myLocation = locationManager.getLastKnownLocation(commadStr);
            String stringMyDestination = "" + myLocation.getLatitude() + "," + myLocation.getLongitude();
            TravelMode travelMode = null;
            switch (stringList.get(4)) {
                case "0":
                    travelMode = TravelMode.DRIVING;
                    break;
                case "1":
                    travelMode = TravelMode.TRANSIT;
                    break;
                case "2":
                    travelMode = TravelMode.WALKING;
                    break;
            }
            Log.e(TAG, stringMyDestination);
            Log.e(TAG, travelMode.toString());
            try {


                result = DirectionsApi.newRequest(getGeoContext()).mode(travelMode).origin(stringMyDestination).destination(stringDestination).departureTime(now).await();
//                Log.e(TAG, result.toString());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            con = getEndLocationTitle(result);
            Log.e(TAG, con);

        }else if("menu_order_finsh".equals(type)){
            con=stringList.get(1);
        }
        else {
            Log.e(TAG, "");
        }

        showNotification(con);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text = String.format(Locale.getDefault(),
                "code = %d, reason = %s, remote = %b",
                code, reason, remote);
        Log.e(TAG, "onClose: " + text);
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "onError: exception = " + ex.toString());
    }

    private void showNotification(String con) {
        Intent intent = new Intent(context, BroadcastDetailActivity.class);
        intent.putExtra("msg", con);
        PendingIntent pendingIntent = PendingIntent.getActivity(context
                , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e(TAG, con);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String id = "1";
        String name = "食神來了";
        Notification notification8 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            Log.e(TAG, mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification8 = new Notification.Builder(context, "1")
                    .setContentTitle(name)
                    .setContentText(con)
                    .setSmallIcon(R.mipmap.ic_launcher).build();
            notificationManager.notify(1, notification8);


        } else {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                    .setContentTitle(name)
                    .setContentText(con)
                    .setSmallIcon(android.R.drawable.ic_menu_info_details)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(soundUri)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification.build());
        }


    }


    private String getEndLocationTitle(DirectionsResult results) {
        long time = results.routes[0].legs[0].duration.inSeconds;

        return "您好，主廚預計"+formatSecond((double)time)+"至府上烹飪您所訂購的嚴選套餐!";



    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).setApiKey("AIzaSyBSifSVz-yV3KYbzJ75s7MF8hREAz1FkjQ").setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS);
    }

    public  String formatSecond(Object second){
        String  html="0秒";
        if(second!=null){
            Double s=(Double) second;
            String format;
            Object[] array;
            Integer hours =(int) (s/(60*60));
            Integer minutes = (int) (s/60-hours*60);
            Integer seconds = (int) (s-minutes*60-hours*60*60);
            if(hours>0){
                format="%1$,d時%2$,d分%3$,d秒";
                array=new Object[]{hours,minutes,seconds};
            }else if(minutes>0){
                format="%1$,d分%2$,d秒";
                array=new Object[]{minutes,seconds};
            }else{
                format="%1$,d秒";
                array=new Object[]{seconds};
            }
            html= String.format(format, array);
        }

        return html;

    }
}



