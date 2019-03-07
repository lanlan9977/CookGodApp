package com.cookgod.order;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.main.Page;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveOrderTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


//(訂單專區)
public class OrderActivity extends AppCompatActivity {
    private final static String TAG = "OrderActivity";
    private final static String PACKAGE = "com.google.zxing.client.android";
    public List<MenuOrderVO> menuOrderList;
    public List<FestOrderVO> festOrderList;
    public List<FoodOrderVO> foodOrderList;
    private RetrieveOrderTask retrieveOrderTask;
    private Boolean isChef;
    public List<MenuOrderVO> getMenuOrderList() {
        return menuOrderList;
    }

    public List<FestOrderVO> getFestOrderList() {
        return festOrderList;
    }
    public List<FoodOrderVO> getFoodOrderList() {
        return foodOrderList;
    }
    public Boolean getIsChef() {
        return isChef;
    }
    private int REQUEST_CHER_ORDER = 1;


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CHER_ORDER) {
//            if (resultCode == RESULT_OK) {
//                Map<FoodMallVO, FoodMallVO> stringMap = (Map<FoodMallVO, FoodMallVO>) data.getSerializableExtra("FoodMallVO");
//                Map<FoodMallVO, ChefOdDetailVO> stringMapQua = (Map<FoodMallVO, ChefOdDetailVO>) data.getSerializableExtra("Chef_Od_Detail");
//                Log.e(TAG,""+stringMap);
//                Util.showToast(OrderActivity.this,"FFFFFFFFFFFF");
//            }
//            Util.showToast(OrderActivity.this,"TTTTTTTTT");
//
//        }Log.e(TAG,""+111);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findViews();
    }

    private void findViews() {
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        isChef=preferences.getBoolean("isChef",false);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            if (Util.networkConnected(this)) {

                try {
                    if(isChef){
                        retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL+"OrderByChefServlet", preferences.getString("chef_ID",""));
                    }else{
                        retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL+"OrderByCustServlet", preferences.getString("cust_ID", ""));
                    }

                    String OrderListJsonIn = retrieveOrderTask.execute().get();

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    Type orderType = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> orderList = gson.fromJson(OrderListJsonIn, orderType);

                    String menuOrderJsonIn = orderList.get(0);
                    Type menuOrderType = new TypeToken<List<MenuOrderVO>>() {
                    }.getType();
                    menuOrderList = gson.fromJson(menuOrderJsonIn, menuOrderType);

                    String festOrderJsonIn = orderList.get(0);
                    Type festOrderType = new TypeToken<List<FestOrderVO>>() {
                    }.getType();
                    festOrderList = gson.fromJson(festOrderJsonIn, festOrderType);

                    String foodOrderJsonIn = orderList.get(2);
                    Type foodOrderType = new TypeToken<List<FoodOrderVO>>() {
                    }.getType();
                    foodOrderList = gson.fromJson(foodOrderJsonIn, foodOrderType);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (menuOrderList != null) {
                    ViewPager viewPager = findViewById(R.id.viewPager);
                    viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));//頁面手勢滑動

                    TabLayout tabLayout = findViewById(R.id.tabLayout);//訂單種類滑動列表
                    viewPager.isFakeDragging();
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                    MODE_PRIVATE);
            pageList.add(new Page(new MenuOrderFragment(), "嚴選套餐訂單"));
            pageList.add(new Page(new FestOrderFragment(), "節慶主題訂單"));

            if(!isChef)
                pageList.add(new Page(new FoodOrderFragment(), "嚴選食材訂單"));
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
        // 設定menu相機icon
        MenuInflater inflater = getMenuInflater();//利用MenuInflater建立選單
        inflater.inflate(R.menu.order_menu, menu);//登記menu id=options_menu的選單
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //點選相機icon後發生事件
        switch (item.getItemId()) {
            case R.id.idCameraQRCode:
                Toast.makeText(OrderActivity.this, getResources().getText(R.string.stringCameraQRCode), Toast.LENGTH_SHORT).show();
//                MenuOrderFragment menuOrderFragment = (MenuOrderFragment) getFragmentManager().findFragmentById(R.id.example_fragment);
                scanQRCode();
                break;
            case R.id.idOrderQRCode:
                Toast.makeText(OrderActivity.this, getResources().getText(R.string.stringOrderQRCode), Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void scanQRCode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        try {
            startActivityForResult(intent, 0);
        }
        // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
        catch (ActivityNotFoundException ex) {
            showDownloadDialog();
        }
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("沒有找到QRCode掃描軟體");
        downloadDialog.setMessage("請下載並安裝掃描軟體!");
        downloadDialog.setNegativeButton("前往",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setPositiveButton("返回",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }


}

