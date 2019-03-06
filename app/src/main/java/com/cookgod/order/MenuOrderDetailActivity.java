package com.cookgod.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.main.Util;
import com.cookgod.task.DishImageTask;
import com.cookgod.task.ImageTask;
import com.cookgod.task.RetrieveMenuOrderDetailTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

public class MenuOrderDetailActivity extends AppCompatActivity {
    private final static String TAG = "MenuOrderDetailActivity";
    private RetrieveMenuOrderDetailTask retrieveMenuOrderDetailTask;
    private MenuVO menuVO;
    private String menu_ID;
    private List<DishVO> dishList;
    private TextView idMenuName, idMenuPrice, idMenuClob;
    private ImageTask imageTask;
    private DishImageTask dishImageTask;
    private ImageView imageView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuorder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuOrderDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.idMenuOrderRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        findViews();
    }

    private void findViews() {
        idMenuName = findViewById(R.id.idMenuName);
        idMenuPrice = findViewById(R.id.idMenuPrice);
        idMenuClob = findViewById(R.id.idMenuClob);
        imageView = findViewById(R.id.idMenuBlobView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        menu_ID = intent.getStringExtra("menu_ID");
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        imageTask = new ImageTask(Util.Servlet_URL+"MenuServlet", menu_ID, imageSize, imageView);
        retrieveMenuOrderDetailTask = new RetrieveMenuOrderDetailTask(Util.Servlet_URL+"MenuDishServlet", menu_ID);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveMenuOrderDetailTask.execute().get();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            Type menuType = new TypeToken<MenuVO>() {
            }.getType();
            Type dishType = new TypeToken<List<DishVO>>() {
            }.getType();
            List<String> stringList = gson.fromJson(jsonIn, listType);
            String menuJsonIn = stringList.get(0);
            String dsihJsonIn = stringList.get(1);
            menuVO = gson.fromJson(menuJsonIn, menuType);
            dishList = gson.fromJson(dsihJsonIn, dishType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        imageTask.execute();
        recyclerView.setAdapter(new MenuOrderDishListAdapter(this, dishList));
        DecimalFormat df = new DecimalFormat("$#,###");
        idMenuName.setText(menuVO.getMenu_name());
        idMenuPrice.setText(df.format(menuVO.getMenu_price()));
        idMenuClob.setText(menuVO.getMenu_resume());
    }

    private class MenuOrderDishListAdapter extends RecyclerView.Adapter<MenuOrderDishListAdapter.ViewHolder> {
        private List<DishVO> dishList;
        private Context context;
        private LayoutInflater layoutInflater;

        public MenuOrderDishListAdapter(Context context, List<DishVO> dishList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.dishList = dishList;

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idDish_name, idDish_Resume;
            ImageView idDish_View;

            public ViewHolder(View itemView) {
                super(itemView);
                idDish_name = itemView.findViewById(R.id.idDish_name);
                idDish_Resume = itemView.findViewById(R.id.idDish_Resume);
                idDish_View = itemView.findViewById(R.id.idDish_View);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_menuorderdish, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            DishVO dishVO = dishList.get(i);
            viewHolder.idDish_name.setText(dishVO.getDish_name());
            viewHolder.idDish_Resume.setText(dishVO.getDish_resume());
            int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            dishImageTask = new DishImageTask(Util.Servlet_URL+"DishServlet", dishVO.getDish_ID(), imageSize, viewHolder.idDish_View);
            dishImageTask.execute();
        }

        @Override
        public int getItemCount() {
            return dishList.size();
        }
    }
}
