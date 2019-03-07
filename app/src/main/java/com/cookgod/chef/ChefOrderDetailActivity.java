package com.cookgod.chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.cookgod.R;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveChefOrderDetailTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ChefOrderDetailActivity extends AppCompatActivity {
    private final static String TAG = "ChefOrderDetailActivity";
    private String chef_ID;
    private RetrieveChefOrderDetailTask retrieveChefOrderDetailTask;
    private List<ChefOdDetailVO> chefOdDetailList;
    private List<ChefOrderVO> chefOrderList;
    private RecyclerView idChefOrderDetailRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheforderdetail);
        idChefOrderDetailRecyclerView=findViewById(R.id.idChefOrderDetailRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChefOrderDetailActivity.this);
        idChefOrderDetailRecyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        chef_ID = preferences.getString("chef_ID", "");
        retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveChefOrderDetailTask.execute().get();
            Type stringListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(jsonIn, stringListType);
            Type chefOrderType = new TypeToken<List<ChefOrderVO>>() {
            }.getType();
            Type chefOdDetailType = new TypeToken<List<ChefOdDetailVO>>() {
            }.getType();
            chefOrderList = gson.fromJson(stringList.get(0), chefOrderType);
            chefOdDetailList = gson.fromJson(stringList.get(1), chefOdDetailType);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }idChefOrderDetailRecyclerView.setAdapter(new ChefOrderDetailAdapter(ChefOrderDetailActivity.this,chefOdDetailList));

    }

    private class ChefOrderDetailAdapter extends RecyclerView.Adapter {
        public ChefOrderDetailAdapter(Context context, List<ChefOdDetailVO> chefOdDetailList) {
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
