package com.cookgod.broadcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.task.RetrieveBroadcastTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class BroadcastDetailActivity extends AppCompatActivity {
    private static final String TAG = "BroadcastDetailActivity";
    private Gson gson;
    private TextView idMsg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcastdetail);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        idMsg=findViewById(R.id.idMsg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        String stringMsg=intent.getStringExtra("msg");
        idMsg.setText(stringMsg);

    }

}

