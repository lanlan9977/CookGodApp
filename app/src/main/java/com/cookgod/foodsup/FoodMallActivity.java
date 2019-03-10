package com.cookgod.foodsup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.chef.ChefOdDetailVO;
import com.cookgod.main.Util;
import com.cookgod.task.FoodMallImageTask;
import com.cookgod.task.RetrieveChefOrderTask;
import com.cookgod.task.RetrieveFoodMallTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodMallActivity extends AppCompatActivity {
    private final static String TAG = "FoodMallActivity";
    private RetrieveFoodMallTask retrieveFoodMallTask;
    private RetrieveChefOrderTask retrieveChefOrderTask;
    private FoodMallImageTask foodMallImageTask;
    private RecyclerView idChefOrederRecyclerView;
    private List<FavFdSupVO> favSupList;
    private List<FoodMallVO> foodMallList;
    private List<FoodSupVO> foodSupList;
    private Dialog dialog, foodDialog;
    private Map<FoodMallVO, ChefOdDetailVO> foodMallMap;
    private Button btnFoodConfitm;
    private String chef_ID;
    private List<ChefOdDetailVO> chefOdDetailList;
    private String menu_od_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order);
        foodMallMap = new LinkedHashMap<>();
        chefOdDetailList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(FoodMallActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        idChefOrederRecyclerView = findViewById(R.id.idChefOrederRecyclerView);
        idChefOrederRecyclerView.setLayoutManager(layoutManager);
        btnFoodConfitm = findViewById(R.id.btnFoodConfitm);
        btnFoodConfitm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodOrder();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        menu_od_ID=intent.getStringExtra("menu_od_ID");
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        chef_ID = preferences.getString("chef_ID", "");
        retrieveFoodMallTask = new RetrieveFoodMallTask(Util.Servlet_URL + "CherOrderServlet", chef_ID);
        try {
            String stringJsonIn = retrieveFoodMallTask.execute().get();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type stringListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(stringJsonIn, stringListType);
            Type favFdSupListType = new TypeToken<List<FavFdSupVO>>() {
            }.getType();
            Type foodMallListType = new TypeToken<List<FoodMallVO>>() {
            }.getType();
            Type foodSupListType = new TypeToken<List<FoodSupVO>>() {
            }.getType();
            favSupList = gson.fromJson(stringList.get(0), favFdSupListType);
            foodMallList = gson.fromJson(stringList.get(1), foodMallListType);
            foodSupList = gson.fromJson(stringList.get(2), foodSupListType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        idChefOrederRecyclerView.setAdapter(new FoodMallListAdapter(FoodMallActivity.this, foodMallList));
    }

    private class FoodMallListAdapter extends RecyclerView.Adapter<FoodMallListAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<FoodMallVO> foodMallList;

        public FoodMallListAdapter(Context context, List<FoodMallVO> foodMallList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.foodMallList = foodMallList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idFood_M_Name, idFood_M_Place, idFood_M_Price, idFood_M_Unit, idCheckQua;
            ImageView idFood_M_Pic, idFav_Sup;
            RatingBar idFood_M_Rate;
            Spinner idFood_Mall_Qty;

            public ViewHolder(View itemView) {
                super(itemView);
                idFood_M_Name = itemView.findViewById(R.id.idFood_M_Name);
                idFood_M_Place = itemView.findViewById(R.id.idFood_M_Place);
                idFood_M_Price = itemView.findViewById(R.id.idFood_M_Price);
                idFood_M_Pic = itemView.findViewById(R.id.idFood_M_Pic);
                idFood_M_Rate = itemView.findViewById(R.id.idFood_M_Rate);
                idFood_M_Unit = itemView.findViewById(R.id.idFood_M_Unit);
                idFav_Sup = itemView.findViewById(R.id.idFav_Sup);
                idFood_Mall_Qty = itemView.findViewById(R.id.idFood_Mall_Qty);
                idCheckQua = itemView.findViewById(R.id.idCheckQua);
                ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(FoodMallActivity.this,
                        R.array.stringFoodMallArray,
                        android.R.layout.simple_spinner_dropdown_item);
                idFood_Mall_Qty.setAdapter(lunchList);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_cheforder, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            final FoodMallVO foodMallVO = foodMallList.get(i);
            foodMallImageTask = new FoodMallImageTask(Util.Servlet_URL + "FoodMallServlet", foodMallVO.getFood_ID(), imageSize, viewHolder.idFood_M_Pic);
            foodMallImageTask.execute();
            viewHolder.idFood_M_Name.setText(foodMallVO.getFood_m_name());
            viewHolder.idFood_M_Place.setText(foodMallVO.getFood_m_place());
            viewHolder.idFood_M_Price.setText("$" + foodMallVO.getFood_m_price().toString());
            viewHolder.idFood_M_Rate.setRating(foodMallVO.getFood_m_rate());
            viewHolder.idFood_M_Unit.setText("單位：" + foodMallVO.getFood_m_unit());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodDialog = new Dialog(FoodMallActivity.this);
                    foodDialog.setTitle("確認訂單食材");
                    foodDialog.setCancelable(true);
                    foodDialog.setContentView(R.layout.dialog_fooddetail);
                    final Window dialogWindow = foodDialog.getWindow();
                    dialogWindow.setGravity(Gravity.CENTER);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = 1000;
                    lp.alpha = 1.0f;
                    dialogWindow.setAttributes(lp);
                    TextView idfood_M_Resume = foodDialog.findViewById(R.id.idfood_M_Resume);
                    Button btnfood_M_Resume_Back = foodDialog.findViewById(R.id.btnfood_M_Resume_Back);
                    btnfood_M_Resume_Back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            foodDialog.cancel();
                        }
                    });
                    idfood_M_Resume.setText(foodMallVO.getFood_m_resume());
                    foodDialog.show();
                }
            });
            for (int j = 0; j < favSupList.size(); j++) {
                FavFdSupVO favFdSupVO = favSupList.get(j);
                if (favFdSupVO.getFood_sup_ID().equals(foodMallVO.getFood_sup_ID())) {
                    viewHolder.idFav_Sup.setVisibility(View.VISIBLE);
                    break;
                }
            }

            viewHolder.idFood_Mall_Qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Integer item = Integer.parseInt(parent.getSelectedItem().toString());
                    ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
                    chefOdDetailVO.setChef_od_qty(item);
                    if (item > 0) {
                        foodMallMap.put(foodMallVO, chefOdDetailVO);
                        viewHolder.idCheckQua.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.idCheckQua.setVisibility(View.GONE);
                        foodMallMap.remove(foodMallVO.getFood_ID());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return foodMallList.size();
        }
    }

    @Override
    protected void onPause() {
        if (foodMallImageTask != null) {
            foodMallImageTask.cancel(true);
            foodMallImageTask = null;
        }
        if (foodMallImageTask != null) {
            foodMallImageTask.cancel(true);
            foodMallImageTask = null;
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foodmall_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idFoodMallOrder:
                showFoodOrder();
                break;
            case R.id.idClick:
                break;
        }
        return false;
    }

    public void showFoodOrder() {
        dialog = new Dialog(FoodMallActivity.this);
        dialog.setTitle("確認訂單食材");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cheffood);
        final Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 1000;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        TextView idConfirmFood = dialog.findViewById(R.id.idConfirmFood);
        TextView idConfirmFoodQua = dialog.findViewById(R.id.idConfirmFoodQua);
        Button idFoodOrderCancel = dialog.findViewById(R.id.idFoodOrderCancel);
        Button idFoodOrderCheckOK = dialog.findViewById(R.id.idFoodOrderCheckOK);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderQua = new StringBuilder();

        for (FoodMallVO key : foodMallMap.keySet()) {
            stringBuilder.append(key.getFood_m_name() + "\n");
            stringBuilderQua.append("X" + foodMallMap.get(key).getChef_od_qty() + "\n");
            ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
            chefOdDetailVO.setFood_ID(key.getFood_ID());
            chefOdDetailVO.setFood_sup_ID(key.getFood_sup_ID());
            chefOdDetailVO.setChef_od_qty(foodMallMap.get(key).getChef_od_qty());
            chefOdDetailVO.setChef_od_stotal(key.getFood_m_price() * foodMallMap.get(key).getChef_od_qty());
            chefOdDetailList.add(chefOdDetailVO);
        }
        idConfirmFood.setText(stringBuilder.toString());
        idConfirmFoodQua.setText(stringBuilderQua.toString());
        idFoodOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        idFoodOrderCheckOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String chefOdDetailJsonIn = gson.toJson(chefOdDetailList);
                retrieveChefOrderTask = new RetrieveChefOrderTask(Util.Servlet_URL + "ChefOdDetailServlet", chef_ID, chefOdDetailJsonIn);
                try {
                    String chef_or_ID = retrieveChefOrderTask.execute().get();
                    SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                            MODE_PRIVATE);
                    preferences.edit().putString(menu_od_ID, chef_or_ID).apply();
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }finally {
                    dialog.dismiss();
                    finish();
                }


            }
        });
        dialog.show();
    }
}
