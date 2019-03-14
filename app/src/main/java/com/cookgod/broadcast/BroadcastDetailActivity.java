package com.cookgod.broadcast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cookgod.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

public class BroadcastDetailActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final String TAG = "BroadcastDetailActivity";
    private String commadStr;
    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcastdetail);
        commadStr = LocationManager.GPS_PROVIDER;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BroadcastDetailActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        Location myLocation = locationManager.getLastKnownLocation(commadStr);

        Intent intent=getIntent();
        String stringLocation=intent.getExtras().getString("location");

        Location location = gson.fromJson(stringLocation, Location.class);
        DateTime now = new DateTime();
        DirectionsResult result = null;
        String stringDestination = "" + location.getLatitude() + "," + location.getLongitude();
        con = getEndLocationTitle(result);
        Log.e(TAG, stringDestination);
        try {
            result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin("taipei").destination(stringDestination).departureTime(now).await();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;

    }
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).setApiKey("AIzaSyBSifSVz-yV3KYbzJ75s7MF8hREAz1FkjQ").setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS);
    }
}

