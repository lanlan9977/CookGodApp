package com.cookgod.other;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.cookgod.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;


//直播專區
public class LivesActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,YouTubePlayer.PlaybackEventListener,YouTubePlayer.PlayerStateChangeListener {

    private RequestQueue mRequestQue;
//    private String URL = "https://fcm.googleapis.com/fcm/send";
    public  String api = "AIzaSyBSifSVz-yV3KYbzJ75s7MF8hREAz1FkjQ";
    private YouTubePlayerView youTubePlayerView;
    private String video_ID="z5Hh_2H8swA";
    YouTubePlayer.OnInitializedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lives);
//        mRequestQue = Volley.newRequestQueue(this);
        youTubePlayerView=(YouTubePlayerView)findViewById(R.id.idYouTubePlayerView);
//        FirebaseMessaging.getInstance().subscribeToTopic("news");

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
//                sendNotification();
                validateMobileLiveIntent(this);

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
