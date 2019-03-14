package com.cookgod.order;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
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
import com.cookgod.task.RetrieveOrderQRCode;
import com.cookgod.task.RetrieveOrderTask;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//(訂單專區)
public class OrderActivity extends AppCompatActivity {
    private final static String TAG = "OrderActivity";
    private final static String PACKAGE = "com.google.zxing.client.android";
    public List<MenuOrderVO> menuOrderList;
    public List<FestOrderVO> festOrderList;
    public List<FoodOrderVO> foodOrderList;
    private RetrieveOrderTask retrieveOrderTask;
    private RetrieveOrderQRCode retrieveOrderQRCode;
    private Boolean isChef;
    private LocationManager locationManager;
    private String cust_ID, commadStr;
    private static final int MY_REQUEST_CODE = 0;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

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
    private int REQUEST_ORDER_QRCODE = 0;


    public void updataCist_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ORDER_QRCODE) {
            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                    MODE_PRIVATE);
            String menu_od_ID = data.getStringExtra("SCAN_RESULT");
            if (isChef) {
                try {
                    String chef_ID = preferences.getString("chef_ID", "");
                    retrieveOrderQRCode = new RetrieveOrderQRCode(Util.Servlet_URL + "OrderByChefQRCodeServlet", menu_od_ID, isChef, chef_ID);
                    String result = retrieveOrderQRCode.execute().get();
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setTitle(result);
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                try {
                    String cust_ID = preferences.getString("cust_ID", "");
                    retrieveOrderQRCode = new RetrieveOrderQRCode(Util.Servlet_URL + "OrderByChefQRCodeServlet", menu_od_ID, isChef, cust_ID);
                    String result = retrieveOrderQRCode.execute().get();
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setTitle(result);
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        commadStr = LocationManager.GPS_PROVIDER;
        findViews();
    }

    private void findViews() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        isChef = preferences.getBoolean("isChef", false);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            if (Util.networkConnected(this)) {

                try {
                    if (isChef) {
                        retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL + "OrderByChefServlet", preferences.getString("chef_ID", ""));
                    } else {
                        retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL + "OrderByCustServlet", preferences.getString("cust_ID", ""));
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

            if (!isChef)
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
                Toast.makeText(OrderActivity.this, cust_ID, Toast.LENGTH_SHORT).show();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
                }
                Location location = locationManager.getLastKnownLocation(commadStr);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                List<String> list = new ArrayList<>();
                list.add("location");
                list.add("C00009");
                String stringLocation = gson.toJson(location);
                list.add(stringLocation);
                String message = new Gson().toJson(list);
                Util.broadcastSocket.send(message);

//                Util.showToast(OrderActivity.this,String.valueOf(location.getLongitude()));
                break;
        }
        return false;
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }


    private void scanQRCode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        try {
            startActivityForResult(intent, 0);// 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
        } catch (ActivityNotFoundException ex) {
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

