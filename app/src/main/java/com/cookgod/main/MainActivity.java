package com.cookgod.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cookgod.R;
import com.cookgod.cust.CustVO;
import com.cookgod.cust.LoginActivity;
import com.cookgod.order.MemberActivity;
import com.cookgod.order.OrderActivity;
import com.cookgod.other.CustomerServiceActivity;
import com.cookgod.other.ForumActivity;
import com.cookgod.other.LivesActivity;
import com.cookgod.other.MallActivity;
import com.cookgod.other.NewsActivity;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CustVO cust_account;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView idCust_name, idHeaderText;
    private BadgeActionProvider provider;
    private BadgeActionProvider.OnClickListener onClickListener = new BadgeActionProvider.OnClickListener() {
        @Override
        public void onClick(int what) {
            if (what == 0) {
                Toast.makeText(MainActivity.this, getResources().getText(R.string.stringMainBroast), Toast.LENGTH_SHORT).show();
                provider.setBadge(0);
            }
        }
    };
    private final int  REQUEST_LOGIN = 1;
    private final int  REQUEST_ORDER = 2;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if(login){
            idCust_name.setText(preferences.getString("cust_name",""));
            idHeaderText.setText("歡迎");
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        switch (resultCode) {
//            case RESULT_OK:
//                if (requestCode == REQUEST_LOGIN) {
//                    Bundle bundle = data.getExtras();
//                    if (!bundle.isEmpty()) {
//                        cust_account = (CustVO) bundle.getSerializable("cust_account");
//
//                    }else{
//                        Toast.makeText(MainActivity.this,"Fuck",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        idCust_name = (TextView) header.findViewById(R.id.idCust_name);
        idHeaderText = (TextView) header.findViewById(R.id.idHeaderText);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();//將drawerLayout和toolbar整合，會出現'三'選單
    }


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
//                if(cust_account==null){
                onItemSelectedTo(R.string.stringLoginM, LoginActivity.class,REQUEST_LOGIN);
//                }else{
//                    Toast.makeText(MainActivity.this,"您已經登入",Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //設定Navigation側滑Item轉至該Activity
        switch (item.getItemId()) {
            case R.id.itemNews://(廣告專區)
                onItemSelectedTo(R.string.stringNews, NewsActivity.class,0);
                break;
            case R.id.itemMember://(會員專區)
                onItemSelectedTo(R.string.stringMember, MemberActivity.class,0);
                break;
            case R.id.itemMall://(商城專區)
                onItemSelectedTo(R.string.stringMall, MallActivity.class,0);
                break;
            case R.id.itemOrder://(訂單專區)
                onItemSelectedTo(R.string.stringOrder, OrderActivity.class,REQUEST_ORDER);
                break;
            case R.id.itemForums://(論壇專區)
                onItemSelectedTo(R.string.stringForum, ForumActivity.class,0);
                break;
            case R.id.itemLives://(直播專區)
                onItemSelectedTo(R.string.stringLives, LivesActivity.class,0);
                break;
            case R.id.itemCustomerService://(客服專區)
                onItemSelectedTo(R.string.stringCustomerService, CustomerServiceActivity.class,0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onItemSelectedTo(int toastString, Class toClass,int requestCode) {
        Intent intent = new Intent(MainActivity.this, toClass);
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        provider.setIcon(R.drawable.ic_broadcast_icon);//推播icon
    }
}
