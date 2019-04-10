package com.cookgod.other;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cookgod.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//直播專區
public class LivesActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,YouTubePlayer.PlaybackEventListener,YouTubePlayer.PlayerStateChangeListener {

    private String URL = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue mRequestQue;
    public  String api = "AIzaSyBSifSVz-yV3KYbzJ75s7MF8hREAz1FkjQ";
    private YouTubePlayerView youTubePlayerView;
    private String video_ID="z5Hh_2H8swA";
    YouTubePlayer.OnInitializedListener listener;
    Button btnLivesPlay,btnLivesLive,btnLivesFcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lives);

        youTubePlayerView=(YouTubePlayerView)findViewById(R.id.idYouTubePlayerView);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        mRequestQue = Volley.newRequestQueue(this);
        btnLivesPlay=findViewById(R.id.btnLivesPlay);
        btnLivesLive=findViewById(R.id.btnLivesLive);
        btnLivesFcm=findViewById(R.id.btnLivesFcm);
        btnLivesPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               youTubePlayerView.initialize(api,LivesActivity.this);
            }
        });
        btnLivesLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMobileLiveIntent(LivesActivity.this);
            }
        });
        btnLivesFcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

    }

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + "news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "主廚直播通知" );
            notificationObj.put("body", "主廚開始直播");
            json.put("notification", notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AIzaSyBtXIbI66YoR04StpbWLjH48LwaHtXMSKo");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//利用MenuInflater建立選單
        inflater.inflate(R.menu.lives_menu, menu);//登記menu id=options_menu的選單
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemStartLive:

                break;
            case R.id.itemStopLive:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }


        return true;
    }
    public String getApi(){
        return api;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlaybackEventListener(this);
        if(!b){
            youTubePlayer.cueVideo(video_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    private boolean canResolveMobileLiveIntent(Context context) {
        Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
                .setPackage("com.google.android.youtube");
        PackageManager pm = context.getPackageManager();
        List resolveInfo =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }


    private void validateMobileLiveIntent(Context context) {
        if (canResolveMobileLiveIntent(context)) {
            startMobileLive(context);
        } else {
            // Prompt user to install or upgrade the YouTube app
        }
    }
    private Intent createMobileLiveIntent(Context context, String description) {
        Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
                .setPackage("com.google.android.youtube");
        Uri referrer = new Uri.Builder()
                .scheme("android-app")
                .appendPath(context.getPackageName())
                .build();
        intent.putExtra(Intent.EXTRA_REFERRER, referrer);
        if (!TextUtils.isEmpty(description)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, description);
        }
        return intent;
    }


    private void startMobileLive(Context context) {
        Intent mobileLiveIntent = createMobileLiveIntent(context, "Streaming via ...");
        startActivity(mobileLiveIntent);
    }


}
