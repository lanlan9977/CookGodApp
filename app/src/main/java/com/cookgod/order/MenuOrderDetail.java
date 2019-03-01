package com.cookgod.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.main.Util;
import com.cookgod.task.ImageTask;
import com.cookgod.task.RetrieveMenuOrderDetailTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

public class MenuOrderDetail extends AppCompatActivity {
    private final static String TAG = "OrderActivity";
    private RetrieveMenuOrderDetailTask retrieveMenuOrderDetailTask;
    private MenuVO menuVO;
    private String menu_ID;
    private List<DishVO> dishList;
    private TextView idMenuName,idMenuPrice,idMenuClob;
    private ImageTask imageTask;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuorder);
        findViews();
    }

    private void findViews() {
        idMenuName=findViewById(R.id.idMenuName);
        idMenuPrice=findViewById(R.id.idMenuPrice);
        idMenuClob=findViewById(R.id.idMenuClob);
        imageView=findViewById(R.id.idMenuBlobView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        menu_ID = intent.getStringExtra("menu_ID");
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;

        imageTask=new ImageTask(Util.Menu_Servlet_URL,menu_ID,imageSize,imageView);
        retrieveMenuOrderDetailTask=new RetrieveMenuOrderDetailTask(Util.MenuDish_Servlet_URL,menu_ID);
        try {
            Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveMenuOrderDetailTask.execute().get();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            Type menuType = new TypeToken<MenuVO>() {
            }.getType();
            Type dishType = new TypeToken<List<DishVO>>() {
            }.getType();
            List<String> stringList=gson.fromJson(jsonIn,listType);
            String menuJsonIn=stringList.get(0);
            String dsihJsonIn=stringList.get(1);
            menuVO=gson.fromJson(menuJsonIn,menuType);
            dishList=gson.fromJson(dsihJsonIn,dishType);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        imageTask.execute();
        DecimalFormat df = new DecimalFormat("$#,###");
        Log.e(TAG,menu_ID);
        idMenuName.setText(menuVO.getMenu_name());
        idMenuPrice.setText(df.format(menuVO.getMenu_price()));
        idMenuClob.setText(menuVO.getMenu_resume());
    }
}

