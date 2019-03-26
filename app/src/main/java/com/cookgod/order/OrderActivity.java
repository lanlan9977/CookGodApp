package com.cookgod.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.main.Page;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveMenuOrderRate;
import com.cookgod.task.RetrieveOrderQRCode;
import com.cookgod.task.RetrieveOrderTask;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final int MY_REQUEST_CODE = 0;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private int REQUEST_ORDER_QRCODE = 0;
    public List<MenuOrderVO> menuOrderList;
    public List<FestOrderVO> festOrderList;
    public List<FoodOrderVO> foodOrderList;
    private RetrieveOrderTask retrieveOrderTask;
    private RetrieveOrderQRCode retrieveOrderQRCode;
    private Boolean isChef;
    private String cust_ID,chef_ID,cust_name,cust_id_location;
    private MenuOrderFragment menuOrderFragment;
    private Dialog dialog;
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
    public String getCust_name(){
        return cust_name;
    }
    private  RetrieveMenuOrderRate retrieveMenuOrderRate;
    private LocationCallback locationCallback;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location location;
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
                    Log.e(TAG,result);
                    if(result.equals("訂單已完成")){
                        List<String> list=new ArrayList<>();
                        list.add("menu_order_finsh");
                        list.add(menu_od_ID);
                        Log.e(TAG,"FFFFFFFFFFFFF");
                        Util.broadcastSocket.send(new Gson().toJson(list));
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                        builder.setTitle("是否要快速給評");
                        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogOK, int which) {
                                dialog = new Dialog(OrderActivity.this);
                                dialog.setTitle("評價留言");
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.dialog_foodorderrate);
                                final Window dialogWindow = dialog.getWindow();
                                dialogWindow.setGravity(Gravity.CENTER);
                                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                lp.width = 500;
                                lp.alpha = 1.0f;
                                dialogWindow.setAttributes(lp);
                                Button btnOrder_Rate_Ok = dialog.findViewById(R.id.btnOrder_Rate_Ok);
                                Button btnOrder_Rate_Back = dialog.findViewById(R.id.btnOrder_Rate_Back);
                                final EditText idOrder_Msg = dialog.findViewById(R.id.idOrder_Msg);
                                RatingBar idOrder_Rate = dialog.findViewById(R.id.idOrder_Rate);
                                final float[] menu_od_rate = {0};
                                idOrder_Rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        menu_od_rate[0] = rating;
                                    }
                                });
                                btnOrder_Rate_Ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String msg = idOrder_Msg.getText().toString().trim();
                                        retrieveMenuOrderRate = new RetrieveMenuOrderRate(Util.Servlet_URL + "MenuOrderRateServlet", String.valueOf(menu_od_rate[0]),menu_od_ID, msg);
                                        retrieveMenuOrderRate.execute();
                                        Util.showToast(OrderActivity.this, "評價成功");
                                        dialog.dismiss();

                                    }
                                });
                                btnOrder_Rate_Back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                                dialogOK.dismiss();

                            }
                        });
                        AlertDialog dialogQuestion = builder.create();
                        dialogQuestion.setCanceledOnTouchOutside(true);
                        dialogQuestion.show();

                    }else{
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
                    }

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        Log.e(TAG,"onCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");
        askPermissions();
        startLocationUpdates();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        isChef = preferences.getBoolean("isChef", false);
        chef_ID=preferences.getString("chef_ID", "");
        cust_ID= preferences.getString("cust_ID", "");
        cust_name=preferences.getString("cust_name", "");
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            if (Util.networkConnected(this)) {


                getData();

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

    public void getData() {
        try {
            if (isChef) {
                retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL + "OrderByChefServlet", chef_ID);
            } else {
                retrieveOrderTask = new RetrieveOrderTask(Util.Servlet_URL + "OrderByCustServlet",cust_ID);
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

            if(orderList.size()>1) {
                String festOrderJsonIn = orderList.get(1);
                Type festOrderType = new TypeToken<List<FestOrderVO>>() {
                }.getType();
                festOrderList = gson.fromJson(festOrderJsonIn, festOrderType);
            }
            if(orderList.size()>2) {
                String foodOrderJsonIn = orderList.get(2);
                Type foodOrderType = new TypeToken<List<FoodOrderVO>>() {
                }.getType();
                foodOrderList = gson.fromJson(foodOrderJsonIn, foodOrderType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(menuOrderFragment = new MenuOrderFragment(), "嚴選套餐訂單"));
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
            case R.id.idOrderRestart:
                onStart();
                break;
            case R.id.idCameraQRCode:
                Toast.makeText(OrderActivity.this, getResources().getText(R.string.stringCameraQRCode), Toast.LENGTH_SHORT).show();
                scanQRCode();
                break;
            case R.id.idOrderLocation:
                cust_id_location = menuOrderFragment.setData();
                Toast.makeText(OrderActivity.this, cust_id_location, Toast.LENGTH_SHORT).show();
                askPermissions();

                ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(OrderActivity.this,
                        R.array.stringTravelMode,
                        android.R.layout.simple_spinner_dropdown_item);
                final Spinner sp = new Spinner(OrderActivity.this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                sp.setAdapter(lunchList);

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                final int[] positionGet = {0};
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        positionGet[0] = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder.setView(sp);
                builder.setTitle("是否發送位置給顧客");
                builder.setPositiveButton("發送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
                        }

                        List<String> list = new ArrayList<>();
                        list.add("location");
                        list.add(cust_id_location);
//                        DecimalFormat df=new DecimalFormat("#.########");
                        list.add(String.valueOf(location.getLatitude()));
                        list.add(String.valueOf(location.getLongitude()));
                        list.add(String.valueOf(positionGet[0]));
                        String message = new Gson().toJson(list);
                        Util.broadcastSocket.send(message);
                        dialog.dismiss();
                        Util.showToast(OrderActivity.this, "發送完畢");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;}
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
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }
    private void createLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
                Log.e(TAG, "" + location);
//                if (location != null)
//                    updateLocationInfo();

            }
        };
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        // 10秒要一次位置資料 (但不一定, 有可能不到10秒, 也有可能超過10秒才要一次)
        locationRequest.setInterval(10000);
        // 若有其他app也使用了LocationServices, 就會以此時間為取得位置資料的依據
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.e(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(OrderActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(OrderActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
    public List<MenuOrderVO> setData(){
        return menuOrderList;
    }

}

