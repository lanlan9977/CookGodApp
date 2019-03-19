package com.cookgod.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.cookgod.task.RetrieveAdTask;
import com.cookgod.task.RetrieveCustTask;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public List<BroadcastVO> broadcastList;
    private final static String TAG = "MainActivity";
    private RetrieveCustTask retrieveCustTask;
    private RetrieveAdTask retrieveAdTask;
    private CustVO cust_account;
    private ChefVO chef_account;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView idCust_name, idHeaderText, mTextMessage;
    private Boolean isChef, login;
    private BroadcastFragment broadcastFragment;
    private BadgeActionProvider provider;
    private FragmentManager manager;
    private ImageView idPicView;
    private FragmentTransaction transaction;
    private BadgeActionProvider.OnClickListener onClickListener = new BadgeActionProvider.OnClickListener() {
        @Override
        public void onClick(int what) {
            if (what == 0) {
                Toast.makeText(MainActivity.this, getResources().getText(R.string.stringMainBroast), Toast.LENGTH_SHORT).show();
                provider.setBadge(0);
            }
        }
    };
    private AdImageTask adImageTask;
    private CarouselView carouselView;
    private int adSize;

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
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
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
       retrieveAdTask=new RetrieveAdTask(Util.Servlet_URL + "AdServlet");
        try {
            String stringSize=retrieveAdTask.execute().get();
            adSize = Integer.valueOf(stringSize);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        login = preferences.getBoolean("login", false);
        isChef = preferences.getBoolean("isChef", false);
        if (login) {
            Util.connectServer(preferences.getString("cust_ID", ""), this);
            idCust_name.setText(preferences.getString("cust_name", ""));
            retrieveCustTask = new RetrieveCustTask(Util.Servlet_URL + "CustServlet", preferences.getString("cust_acc", ""), preferences.getString("cust_pwd", ""));
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
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
        carouselView.setPageCount(adSize);
        carouselView.setImageListener(imageListener);
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        if (broadcastFragment != null) {
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.remove(broadcastFragment).commitAllowingStateLoss();
            broadcastFragment = null;
        }
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
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
        mTextMessage = findViewById(R.id.message);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();//將drawerLayout和toolbar整合，會出現'三'選單
        idCust_name = header.findViewById(R.id.idCust_name);
        idHeaderText = header.findViewById(R.id.idHeaderText);
        idPicView = header.findViewById(R.id.idPicView);
        carouselView = findViewById(R.id.carouselView);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            int imageSize = getResources().getDisplayMetrics().widthPixels/4 ;
            adImageTask = new AdImageTask(Util.Servlet_URL + "AdServlet", imageSize, imageView, position);
            adImageTask.execute();

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//利用MenuInflater建立選單
        inflater.inflate(R.menu.option_menu, menu); //將option_menu inflate使用（膨脹）
        MenuItem menuItem = menu.findItem(R.id.myBadge);
        provider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        provider.setOnClickListener(0, onClickListener);
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
//                    onItemSelectedTo(R.string.stringOrder, OrderActivity.class);
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
                Intent intent = new Intent(MainActivity.this, ChefZoneActivity.class);
                intent.putExtra("chef_ID",chef_account.getChef_ID());
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.itemLives://(直播專區)
                onItemSelectedTo(R.string.stringLives, LivesActivity.class);
                break;
            case R.id.itemCustomerService://(客服專區)
                onItemSelectedTo(R.string.stringCustomerService, CustomerServiceActivity.class);
                break;
            case R.id.logout://(登出)
                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit().putBoolean("login", false).putBoolean("isChef", false).apply();
                logout();
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
        int readStatus = 0;
        if (broadcastList != null && !broadcastList.isEmpty()) {
            for (int i = 0; i < broadcastList.size(); i++) {
                if (broadcastList.get(i).getBroadcast_status().equals("B0")) {
                    readStatus++;
                }
            }
        }
        provider.setBadge(readStatus);
        Menu menu = navigationView.getMenu();
        if (login) {
            if (isChef) {
                idPicView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chef_icon));
                idHeaderText.setText("主廚");

            } else {
                idHeaderText.setText("顧客");
                menu.findItem(R.id.itemForums).setVisible(true);
            }
            menu.findItem(R.id.logout).setVisible(true);
        }
    }

    public void logout() {
        AlertFragment alertFragment = new AlertFragment();
        FragmentManager fm = getSupportFragmentManager();
        alertFragment.show(fm, "alert");
    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
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