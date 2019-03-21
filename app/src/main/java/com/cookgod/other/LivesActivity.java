package com.cookgod.other;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.cookgod.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


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

    public void videoPlay(View view) {
        youTubePlayerView.initialize(getApi(),this);
    }


//    private void sendNotification() {
//
//        JSONObject json = new JSONObject();
//        try {
//            json.put("to", "/topics/" + "news");
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title", "any title");
//            notificationObj.put("body", "主廚開始直播");
//            json.put("notification", notificationObj);
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
//                    json,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            Log.d("MUR", "onResponse: ");
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("MUR", "onError: " + error.networkResponse);
//                }
//            }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=AIzaSyBtXIbI66YoR04StpbWLjH48LwaHtXMSKo");
//                    return header;
//                }
//            };
//            mRequestQue.add(request);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
