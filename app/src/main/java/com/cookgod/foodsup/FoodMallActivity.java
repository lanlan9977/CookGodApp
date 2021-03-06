package com.cookgod.foodsup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.chef.ChefOdDetailVO;
import com.cookgod.main.Page;
import com.cookgod.main.Util;
import com.cookgod.order.DishVO;
import com.cookgod.task.RetrieveChefOrderTask;
import com.cookgod.task.RetrieveDishTask;
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
    private List<FavFdSupVO> favSupList;
    private List<FoodMallVO> foodMallList, foodMallListOne, foodMallListTwo, foodMallListThree, foodMallListFour, foodMallListFive;
    private FoodMallListFragment foodMallListFragment, foodMallListFragmentOne, foodMallListFragmentTwo, foodMallListFragmentThree, foodMallListFragmentFour, foodMallListFragmentFive;
    private List<FoodSupVO> foodSupList;
    private List<FoodVO> foodList;
    private List<DishFoodVO> dishFoodList, newFoodList, checkList;
    private Dialog dialog;
    private Map<FoodMallVO, ChefOdDetailVO> foodMallMap, newFoofMallMap;
    private Button btnFoodConfitm;
    private String chef_ID, chef_addr, chef_tel, chef_name;
    private List<ChefOdDetailVO> chefOdDetailList;
    private String menu_od_ID;
    private RetrieveDishTask retrieveDishTask;
    private List<DishVO> newDishList;
    private Dialog newDishDialog;
    private List<String> checkDishIDList;

    public Map<FoodMallVO, ChefOdDetailVO> getFoodMallMap() {
        return foodMallMap;
    }

    public List<FavFdSupVO> getFavSupList() {
        return favSupList;
    }

    public List<FoodSupVO> getFoodSupList() {
        return foodSupList;
    }

    public List<FoodMallVO> getFoodMallList() {
        return foodMallList;
    }

    public List<FoodVO> getFoodList() {
        return foodList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order);
        foodMallMap = new LinkedHashMap<>();

        chefOdDetailList = new ArrayList<>();
        foodMallListOne = new ArrayList<>();
        foodMallListTwo = new ArrayList<>();
        foodMallListThree = new ArrayList<>();
        foodMallListFour = new ArrayList<>();
        foodMallListFive = new ArrayList<>();
        checkList = new ArrayList<>();
        btnFoodConfitm = findViewById(R.id.btnFoodConfitm);
        btnFoodConfitm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodOrder();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idFoodMallOrder:
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                retrieveDishTask = new RetrieveDishTask(Util.Servlet_URL + "DishSelectServlet", "selectAll");
                try {
                    String jsonIn = retrieveDishTask.execute().get();
                    Type stringDishListType = new TypeToken<List<DishVO>>() {
                    }.getType();
                    newDishList = gson.fromJson(jsonIn, stringDishListType);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                newDishDialog = new Dialog(FoodMallActivity.this, R.style.PauseCustomDialog);
                newDishDialog.setTitle("選擇需要食材的菜色");
                newDishDialog.setCancelable(true);
                newDishDialog.setContentView(R.layout.dialog_newdish);
                final Window dialogWindow = newDishDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = 700;
                lp.alpha = 1.0f;
                dialogWindow.setAttributes(lp);
                RecyclerView idfood_M_Detail_Handler_View = newDishDialog.findViewById(R.id.idfood_M_Detail_newDish_View);
                idfood_M_Detail_Handler_View.setLayoutManager(new LinearLayoutManager(FoodMallActivity.this));
                idfood_M_Detail_Handler_View.setAdapter(new NewDishAdapter(FoodMallActivity.this, newDishList));
                Button benNewDish_go = newDishDialog.findViewById(R.id.benNewDish_go);
                Log.e(TAG, "" + foodMallMap.size());
                benNewDish_go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkDishIDList = new ArrayList<>();
                        for (DishVO dishVO : newDishList) {
                            if (dishVO.isCheck()) {
                                checkDishIDList.add(dishVO.getDish_ID());
                            }
                        }
                        String jsonin = gson.toJson(checkDishIDList);
                        retrieveDishTask = new RetrieveDishTask(Util.Servlet_URL + "DishSelectServlet", "dishAll", jsonin);
                        try {
                            String newJson = retrieveDishTask.execute().get();
                            Type stringDishListType = new TypeToken<List<DishFoodVO>>() {
                            }.getType();
                            newFoodList = gson.fromJson(newJson, stringDishListType);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                        if (newFoodList != null) {
                            for (int i = 0; i < foodMallList.size(); i++) {
                                for (int j = 0; j < newFoodList.size(); j++) {
                                    if (foodMallList.get(i).getFood_ID().contains(newFoodList.get(j).getFood_ID())) {
                                        ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
                                        chefOdDetailVO.setFood_ID(foodMallList.get(i).getFood_ID());
                                        chefOdDetailVO.setChef_od_qty(newFoodList.get(j).getDish_f_qty());
                                        chefOdDetailVO.setFood_sup_ID(foodMallList.get(i).getFood_sup_ID());
                                        if (foodMallMap.size() == 0) {

                                            foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                        } else {
                                            if (foodMallMap.containsKey(foodMallList.get(i))) {

                                                Log.e(TAG, "此食材重複");
                                                int newQty = foodMallMap.get(foodMallList.get(i)).getChef_od_qty();
                                                newQty += chefOdDetailVO.getChef_od_qty();
                                                chefOdDetailVO.setChef_od_qty(newQty);
                                                foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                            } else {
                                                for (FoodMallVO foodMallVO : foodMallMap.keySet()) {
                                                    Log.e(TAG, "" + foodMallVO.getFood_ID() + "-" + foodMallList.get(i).getFood_ID());
                                                }

                                                Log.e(TAG, "此食材不重複");
                                                foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                            }
                                        }
                                    }
                                }
                            }
                            if (foodMallListFragmentOne != null)
                                foodMallListFragmentOne.reMapData(foodMallMap);
                            if (foodMallListFragmentTwo != null)
                                foodMallListFragmentTwo.reMapData(foodMallMap);
                            if (foodMallListFragmentThree != null)
                                foodMallListFragmentThree.reMapData(foodMallMap);
                            if (foodMallListFragmentFour != null)
                                foodMallListFragmentFour.reMapData(foodMallMap);
                            if (foodMallListFragmentFive != null)
                                foodMallListFragmentFive.reMapData(foodMallMap);
                            foodMallListFragment.reMapData(foodMallMap);

                        }
                        newDishDialog.dismiss();
//                        Util.showToast(FoodMallActivity.this,""+checkDishIDList.get(0));
                    }
                });
                Button benNewDish_back = newDishDialog.findViewById(R.id.benNewDish_back);
                benNewDish_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newDishDialog.cancel();
                    }
                });

                newDishDialog.show();

                break;
            case R.id.idClick:
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodMallActivity.this);
                builder.setTitle("請否要一鍵購買該套餐菜色所需食材");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foodMallMap = new LinkedHashMap<>();
                        newFoofMallMap = new LinkedHashMap<>();
                        for (int i = 0; i < foodMallList.size(); i++) {
                            for (int j = 0; j < dishFoodList.size(); j++) {

                                if (dishFoodList.get(j).getFood_ID().equals(foodMallList.get(i).getFood_ID())) {
                                    ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
                                    chefOdDetailVO.setFood_ID(foodMallList.get(i).getFood_ID());
                                    chefOdDetailVO.setFood_sup_ID(foodMallList.get(i).getFood_sup_ID());
                                    chefOdDetailVO.setChef_od_qty(dishFoodList.get(j).getDish_f_qty());
                                    Log.e(TAG, "增加食材");
                                    if (foodMallMap.size() == 0) {

                                        foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                    } else {
                                        if (foodMallMap.containsKey(foodMallList.get(i))) {

                                            Log.e(TAG, "此食材重複");
                                            int newQty = foodMallMap.get(foodMallList.get(i)).getChef_od_qty();
                                            newQty += chefOdDetailVO.getChef_od_qty();
                                            chefOdDetailVO.setChef_od_qty(newQty);
                                            foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                        } else {
                                            for (FoodMallVO foodMallVO : foodMallMap.keySet()) {
                                                Log.e(TAG, "" + foodMallVO.getFood_ID() + "-" + foodMallList.get(i).getFood_ID());
                                            }

                                            Log.e(TAG, "此食材不重複");
                                            foodMallMap.put(foodMallList.get(i), chefOdDetailVO);
                                        }
                                    }
                                }
                            }
                        }
                        if (foodMallListFragmentOne != null)
                            foodMallListFragmentOne.reMapData(foodMallMap);
                        if (foodMallListFragmentTwo != null)
                            foodMallListFragmentTwo.reMapData(foodMallMap);
                        if (foodMallListFragmentThree != null)
                            foodMallListFragmentThree.reMapData(foodMallMap);
                        if (foodMallListFragmentFour != null)
                            foodMallListFragmentFour.reMapData(foodMallMap);
                        if (foodMallListFragmentFive != null)
                            foodMallListFragmentFive.reMapData(foodMallMap);
                        foodMallListFragment.reMapData(foodMallMap);

                        Util.showToast(FoodMallActivity.this, "已自動選取食材");
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


                break;
        }
        return false;
    }

    public void showFoodOrder() {
        dialog = new Dialog(FoodMallActivity.this, R.style.PauseDialog);
        dialog.setTitle("確認訂單食材");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cheffood);
        final Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 700;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        TextView idConfirmFood = dialog.findViewById(R.id.idConfirmFood);
        TextView idConfirmFoodQua = dialog.findViewById(R.id.idConfirmFoodQua);
        TextView idConfirmFoodStotal = dialog.findViewById(R.id.idConfirmFoodStotal);
        TextView idFoodMall_Total = dialog.findViewById(R.id.idFoodMall_Total);
        Button idFoodOrderCancel = dialog.findViewById(R.id.idFoodOrderCancel);
        final Button idFoodOrderCheckOK = dialog.findViewById(R.id.idFoodOrderCheckOK);
        final Button idFoodOrderCheckNext = dialog.findViewById(R.id.idFoodOrderCheckNext);
        final LinearLayout idChef_Or_Data = dialog.findViewById(R.id.idChef_Or_Data);
        final LinearLayout idChef_Or = dialog.findViewById(R.id.idChef_Or);
        final EditText idCheckName = dialog.findViewById(R.id.idCheckName);
        final EditText idCheckTel = dialog.findViewById(R.id.idCheckTel);
        final EditText idCheckAddr = dialog.findViewById(R.id.idCheckAddr);
        RadioButton rbtnChefData = dialog.findViewById(R.id.rbtnChefData);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderQua = new StringBuilder();
        StringBuilder stringBuilderStoral = new StringBuilder();
        int total = 0;
        if (foodMallMap != null) {
            for (FoodMallVO key : foodMallMap.keySet()) {
                stringBuilder.append(key.getFood_m_name() + "\n");
                stringBuilderQua.append("x" + foodMallMap.get(key).getChef_od_qty() + "\n");
                stringBuilderStoral.append("$" + (key.getFood_m_price() * foodMallMap.get(key).getChef_od_qty()) + "\n");
                total += key.getFood_m_price() * foodMallMap.get(key).getChef_od_qty();
                ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
                chefOdDetailVO.setFood_ID(key.getFood_ID());
                chefOdDetailVO.setFood_sup_ID(key.getFood_sup_ID());
                chefOdDetailVO.setChef_od_qty(foodMallMap.get(key).getChef_od_qty());
                chefOdDetailVO.setChef_od_stotal(key.getFood_m_price() * foodMallMap.get(key).getChef_od_qty());
                chefOdDetailList.add(chefOdDetailVO);
            }
        }
        idConfirmFood.setText(stringBuilder.toString());
        idConfirmFoodQua.setText(stringBuilderQua.toString());
        idConfirmFoodStotal.setText(stringBuilderStoral.toString());
        idFoodMall_Total.setText("＄" + String.valueOf(total) + "元");
        idFoodOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        idFoodOrderCheckOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodMallActivity.this);
                builder.setTitle("是否送出訂單");
                builder.setPositiveButton("送出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                                MODE_PRIVATE);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        String chefOdDetailJsonIn = gson.toJson(chefOdDetailList);
                        retrieveChefOrderTask = new RetrieveChefOrderTask(Util.Servlet_URL + "ChefOdDetailServlet", chef_ID, chefOdDetailJsonIn);

                        try {
                            String chef_or_ID = retrieveChefOrderTask.execute().get();
                            if(!chef_or_ID.equals("")) {
                                preferences.edit().putString(menu_od_ID, chef_or_ID).apply();
                            }else{
                                chef_or_ID="CF20190329-000008";
                                preferences.edit().putString(menu_od_ID, chef_or_ID).apply();




                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }finally {
                            Util.showToast(FoodMallActivity.this, "發送訂單完畢");
                            dialog.dismiss();
                            finish();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });
        idFoodOrderCheckNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idFoodOrderCheckOK.setVisibility(View.VISIBLE);

                idFoodOrderCheckNext.setVisibility(View.GONE);


                TranslateAnimation mShowAction1 = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, -1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);

                TranslateAnimation mShowAction2 = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, +1.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f);


                mShowAction2.setDuration(400);
                mShowAction1.setDuration(400);
                idChef_Or.setAnimation(mShowAction1);


                idChef_Or_Data.setAnimation(mShowAction2);
                idChef_Or.setVisibility(View.GONE);
                idChef_Or_Data.setVisibility(View.VISIBLE);
            }
        });
        rbtnChefData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    idCheckName.setText(chef_name);
                    idCheckTel.setText(chef_tel);
                    idCheckAddr.setText(chef_addr);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        menu_od_ID = intent.getStringExtra("menu_od_ID");
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        chef_ID = preferences.getString("chef_ID", "");
        chef_name = preferences.getString("chef_name", "");
        chef_tel = preferences.getString("chef_tel", "");
        chef_addr = preferences.getString("chef_addr", "");
        retrieveFoodMallTask = new RetrieveFoodMallTask(Util.Servlet_URL + "CherOrderServlet", chef_ID, menu_od_ID);
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
            Type foodListType = new TypeToken<List<FoodVO>>() {
            }.getType();
            Type dishFoodListType = new TypeToken<List<DishFoodVO>>() {
            }.getType();
            favSupList = gson.fromJson(stringList.get(0), favFdSupListType);
            foodMallList = gson.fromJson(stringList.get(1), foodMallListType);
            foodSupList = gson.fromJson(stringList.get(2), foodSupListType);
            foodList = gson.fromJson(stringList.get(3), foodListType);
            dishFoodList = gson.fromJson(stringList.get(4), dishFoodListType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (foodMallList != null) {
            autoType();
            ViewPager viewPager = findViewById(R.id.viewFoodMallPager);
            viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));//頁面手勢滑動
            TabLayout tabLayout = findViewById(R.id.tabFoodMallLayout);//訂單種類滑動列表
            viewPager.isFakeDragging();
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    public void autoType() {
        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getFood_type_ID().equals("g0")) {
                foodMallListOne.add(foodMallList.get(i));
            }
            if (foodList.get(i).getFood_type_ID().equals("g1")) {
                foodMallListTwo.add(foodMallList.get(i));
            }
            if (foodList.get(i).getFood_type_ID().equals("g2")) {
                foodMallListThree.add(foodMallList.get(i));
            }
            if (foodList.get(i).getFood_type_ID().equals("g3")) {
                foodMallListFour.add(foodMallList.get(i));
            }
            if (foodList.get(i).getFood_type_ID().equals("g4")) {
                foodMallListFive.add(foodMallList.get(i));
            }
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        List<Page> pageList;

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(foodMallListFragment = new FoodMallListFragment(foodMallList), "全部"));
            pageList.add(new Page(foodMallListFragmentOne = new FoodMallListFragment(foodMallListOne), "肉類"));
            pageList.add(new Page(foodMallListFragmentTwo = new FoodMallListFragment(foodMallListTwo), "菜類"));
            pageList.add(new Page(foodMallListFragmentThree = new FoodMallListFragment(foodMallListThree), "海鮮"));
            pageList.add(new Page(foodMallListFragmentFour = new FoodMallListFragment(foodMallListFour), "五穀雜糧"));
            pageList.add(new Page(foodMallListFragmentFive = new FoodMallListFragment(foodMallListFive), "其他"));

        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foodmall_menu, menu);
        return true;
    }

    public class NewDishAdapter extends RecyclerView.Adapter<NewDishAdapter.ViewHolder> {
        private List<DishVO> newDishList;
        private LayoutInflater layoutInflater;
        private Context context;

        public NewDishAdapter(Context context, List<DishVO> newDishList) {
            this.newDishList = newDishList;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idNewDish_Name;
            CheckBox idDishCheck;

            public ViewHolder(View itemView) {
                super(itemView);
                idNewDish_Name = itemView.findViewById(R.id.idNewDish_Name);
                idDishCheck = itemView.findViewById(R.id.idDishCheck);
            }
        }

        @Override
        public NewDishAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_newdish, viewGroup, false);
            return new NewDishAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NewDishAdapter.ViewHolder viewHolder, int i) {
            DishVO dishVO = newDishList.get(i);
            viewHolder.idNewDish_Name.setText(dishVO.getDish_name());
            viewHolder.idDishCheck.setChecked(dishVO.isCheck());
            viewHolder.idDishCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = ((CheckBox) v).isChecked();
                    viewHolder.idDishCheck.setChecked(b);
                    newDishList.get(i).setCheck(b);
                }
            });


        }

        @Override
        public int getItemCount() {
            return newDishList.size();
        }


    }
}

