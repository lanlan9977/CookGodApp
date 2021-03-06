package com.cookgod.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.broadcast.BadgeActionProvider;
import com.cookgod.broadcast.BroadcastFragment;
import com.cookgod.broadcast.BroadcastVO;
import com.cookgod.chef.ChefVO;
import com.cookgod.chef.ChefZoneActivity;
import com.cookgod.cust.CustVO;
import com.cookgod.cust.LoginActivity;
import com.cookgod.order.MemberActivity;
import com.cookgod.order.OrderActivity;
import com.cookgod.other.CustomerServiceActivity;
import com.cookgod.other.LivesActivity;
import com.cookgod.other.MallActivity;
import com.cookgod.other.NewsActivity;
import com.cookgod.task.AdImageTask;
import com.cookgod.task.CustImageTask;
import com.cookgod.task.RetrieveAdConTask;
import com.cookgod.task.RetrieveBroadcastTask;
import com.cookgod.task.RetrieveCustTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public List<BroadcastVO> broadcastList;
    private final static String TAG = "MainActivity";
    private RetrieveCustTask retrieveCustTask;
    private CustVO cust_account;
    private ChefVO chef_account;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView idCust_name, idHeaderText;
    private Boolean isChef, login;
    private BroadcastFragment broadcastFragment;
    private BadgeActionProvider provider;
    private FragmentManager manager;
    private ImageView idPicView,idCust_Pic;
    private FragmentTransaction transaction;
    private RetrieveAdConTask retrieveAdConTask;
    private Map<String,String[]> stringConMap;
    private CustImageTask custImageTask;
    private CarouselView customCarouselView;
    private AdImageTask adImageTask;
    private int adSize;
    private GifImageView gifImageView1;
    private List<String> conList,tittleList;
    private RetrieveBroadcastTask retrieveBroadcastTask;
    public List<BroadcastVO> getBroadcastList() {
        Log.e(TAG, "getBroadcastList");
        return broadcastList;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(item);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(item);
                    return true;
            }
            return false;
        }
    };

    private void showFragment(MenuItem item) {
        transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (item.getItemId()) {

            case R.id.navigation_notifications:
                if (broadcastFragment == null) {
                    Log.e(TAG, "null");
                    broadcastFragment = new BroadcastFragment();
                    transaction.add(R.id.frameLayout, broadcastFragment).setCustomAnimations(R.anim.in, R.anim.out).show(broadcastFragment);
                } else {
                    Log.e(TAG, "else");
                    transaction.setCustomAnimations(R.anim.in, R.anim.out).show(broadcastFragment);
                }
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (broadcastFragment != null) {
            fragmentTransaction.hide(broadcastFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        conList=new ArrayList<>();
        tittleList=new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        int imageSize = getResources().getDisplayMetrics().widthPixels ;
        DisplayMetrics metrics = MainActivity.this.getResources().getDisplayMetrics();
        float highSize = metrics.heightPixels;

        retrieveAdConTask = new RetrieveAdConTask(Util.Servlet_URL + "AdServlet", "selectCon");
        try {
            String jsonInCon = retrieveAdConTask.execute().get();
            Type stringMapType = new TypeToken<Map<String,String[]>>() {
            }.getType();
            stringConMap = gson.fromJson(jsonInCon, stringMapType);
            adSize = stringConMap.size();
            for(String ad_ID:stringConMap.keySet()){
                tittleList.add((stringConMap.get(ad_ID))[0]);
                conList.add((stringConMap.get(ad_ID))[1]);
            }
            Log.e(TAG, "" + stringConMap);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        login = preferences.getBoolean("login", false);
        isChef = preferences.getBoolean("isChef", false);
        if (login) {
            Util.connectServer(preferences.getString("cust_ID", ""), this);
            idCust_name.setText(preferences.getString("cust_niname", ""));
            if (highSize == 1024) {
                Log.e(TAG,"FFFFFFFFFFFFFFFFF");
                custImageTask =new CustImageTask(Util.Servlet_URL + "CustImageServlet",preferences.getString("cust_acc", ""),imageSize,idCust_Pic);
            } else {
                custImageTask =new CustImageTask(Util.Servlet_URL + "CustImageServlet",preferences.getString("cust_acc", ""),imageSize/8,idCust_Pic);

            }
            custImageTask.execute();
            retrieveCustTask = new RetrieveCustTask(Util.Servlet_URL + "CustServlet", preferences.getString("cust_acc", ""), preferences.getString("cust_pwd", ""));
            try {
                String jsonIn = retrieveCustTask.execute().get();
                Type listType = new TypeToken<Map<String, String>>() {
                }.getType();
                Type broadcastType = new TypeToken<List<BroadcastVO>>() {
                }.getType();
                Map<String, String> map = gson.fromJson(jsonIn, listType);
                List<String> list = new ArrayList<>();
                for (String key : map.keySet()) {
                    list.add(key);
                }
                cust_account = gson.fromJson(list.get(0), CustVO.class);
                broadcastList = gson.fromJson(map.get(list.get(0)), broadcastType);
                if (list.size() > 1) {
                    chef_account = gson.fromJson(map.get(list.get(1)), ChefVO.class);
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        Menu menu = navigationView.getMenu();
        if (login) {
            if (isChef) {
                idPicView.setImageDrawable(getResources().getDrawable(R.drawable.ic_iconfinder_chef));
                idHeaderText.setText("主廚");

            } else {
                idHeaderText.setText("顧客");
                menu.findItem(R.id.itemForums).setVisible(true);
            }
            menu.findItem(R.id.logout).setVisible(true);
            Toast.makeText(MainActivity.this, "登入成功", Toast.LENGTH_LONG);
        }
        customCarouselView.setViewListener(viewListener);
        customCarouselView.setSlideInterval(4000);
        customCarouselView.setPageCount(adSize);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);//設定主畫面為activity_navigation_drawer
        findViews();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //使用自定toolbar為ActionBar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);//將此activity設定監聽器
        View header = navigationView.inflateHeaderView(R.layout.nav_header_navigation_drawer);//設定Navigation上的HEADER元件
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();//將drawerLayout和toolbar整合，會出現'三'選單
        idCust_name = header.findViewById(R.id.idCust_name);
        idHeaderText = header.findViewById(R.id.idHeaderText);
        idPicView = header.findViewById(R.id.idPicView);
        idCust_Pic=header.findViewById(R.id.idCust_Pic);
        customCarouselView = findViewById(R.id.customCarouselView);
        gifImageView1=findViewById(R.id.logoGif);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.bkd_login);
            gifImageView1.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);
            TextView labelTextView = customView.findViewById(R.id.labelTextView);
            TextView labelConView=customView.findViewById(R.id.labelConView);
            ImageView imageView =customView.findViewById(R.id.fruitImageView);
            int imageSize = getResources().getDisplayMetrics().widthPixels / 2;
            adImageTask = new AdImageTask(Util.Servlet_URL + "AdServlet", imageSize, imageView, position);
            adImageTask.execute();

            labelTextView.setText(conList.get(position));
            labelConView.setText(tittleList.get(position));
            customCarouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);

            return customView;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//利用MenuInflater建立選單
        inflater.inflate(R.menu.option_menu, menu); //將option_menu inflate使用（膨脹）
        MenuItem menuItem = menu.findItem(R.id.myBadge);
        provider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idLogin://(登入專區)
                if (cust_account == null) {
                    onItemSelectedTo(R.string.stringLoginM, LoginActivity.class);
                } else {
                    Toast.makeText(MainActivity.this, "您已經登入", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.myRefresh://(重新整理)
                onRestart();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {//設定Navigation側滑Item轉至該Activity
            case R.id.itemNews://(廣告專區)
                onItemSelectedTo(R.string.stringNews, NewsActivity.class);
                break;
            case R.id.itemMember://(會員專區)
                onItemSelectedTo(R.string.stringMember, MemberActivity.class);
                break;
            case R.id.itemMall://(商城專區)
                onItemSelectedTo(R.string.stringMall, MallActivity.class);
                break;
            case R.id.itemOrder://(訂單專區)
                if (cust_account == null) {
                    onItemSelectedTo(R.string.stringLoginNo, LoginActivity.class);
                } else {
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    Toast.makeText(getApplicationContext(), R.string.stringOrder, Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isChef", isChef);
                    bundle.putString("Cust_id", cust_account.getCust_ID());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);
                }

                break;
            case R.id.itemForums://(論壇專區)
                if (isChef) {
                    Intent intent = new Intent(MainActivity.this, ChefZoneActivity.class);
                    intent.putExtra("chef_ID", chef_account.getChef_ID());
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);
                } else {
                    Util.showToast(MainActivity.this, "此為主廚專區");
                }
                break;
            case R.id.itemLives://(直播專區)
                onItemSelectedTo(R.string.stringLives, LivesActivity.class);
                break;
            case R.id.itemCustomerService://(客服專區)
                onItemSelectedTo(R.string.stringCustomerService, CustomerServiceActivity.class);
                break;
            case R.id.logout://(登出)
                GoogleSignInClient mGoogleSignInClient;

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestId()
                        .requestEmail()
                        .requestProfile()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                logout();
                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit().putBoolean("login", false).putBoolean("isChef", false).apply();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onItemSelectedTo(int toastString, Class toClas) {
        Intent intent = new Intent(MainActivity.this, toClas);
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
        startActivity(intent);
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        provider.setIcon(R.drawable.ic_broadcast_icon);//推播icon
        onProviderCount(this.broadcastList);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        if (broadcastFragment != null&&cust_account!=null) {
            retrieveBroadcastTask = new RetrieveBroadcastTask(Util.Servlet_URL + "BroadcastServlet", cust_account.getCust_ID(), "action");
            try {
                String jsonIn = retrieveBroadcastTask.execute().get();
                Type broadcastType = new TypeToken<List<BroadcastVO>>() {
                }.getType();
                broadcastList = gson.fromJson(jsonIn, broadcastType);
                broadcastFragment.onRead(broadcastList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onProviderCount(List<BroadcastVO> broadcastList) {
        int readStatus = 0;
        if (broadcastList != null && !broadcastList.isEmpty()) {
            for (int i = 0; i < broadcastList.size(); i++) {
                if (broadcastList.get(i).getBroadcast_status().equals("B0")) {
                    readStatus++;
                }
            }
        }
        provider.setBadge(readStatus);
    }

    public void logout() {
        AlertFragment alertFragment = new AlertFragment();
        FragmentManager fm = getSupportFragmentManager();
        alertFragment.show(fm, "alert");
    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.LightDialogTheme)
                    .setTitle("登出")
                    .setIcon(R.drawable.ic_login_button)
                    .setMessage(R.string.stringLogout)
                    .setPositiveButton(R.string.stringLogoutYes, this)
                    .setNegativeButton(R.string.stringLogoutNo, this)
                    .create();

            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_NEGATIVE:
                    dialogInterface.cancel();
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    Util.disconnectServer();
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}