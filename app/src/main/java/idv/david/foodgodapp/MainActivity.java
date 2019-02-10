package idv.david.foodgodapp;

import android.content.Intent;
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
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        //設定主畫面為activity_navigation_drawer
        findViews();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //使用自定toolbar為ActionBar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        //將此activity設定監聽器
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //將drawerLayout和toolbar整合，會出現「三」按鈕
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //利用MenuInflater建立選單
        inflater.inflate(R.menu.option_menu, menu);
        //將option_menu inflate使用（膨脹）

        MenuItem menuItem = menu.findItem(R.id.myBadge);
        provider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        provider.setOnClickListener(0, onClickListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idLogin://(登入專區)
                onItemSelectedTo(R.string.stringLoginM, LoginActivity.class);
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
                onItemSelectedTo(R.string.stringNews, NewsActivity.class);
                break;
            case R.id.itemMember://(會員專區)
                onItemSelectedTo(R.string.stringMember, MemberActivity.class);
                break;
            case R.id.itemMall://(商城專區)
                onItemSelectedTo(R.string.stringMall, MallActivity.class);
                break;
            case R.id.itemOrder://(訂單專區)
                onItemSelectedTo(R.string.stringOrder, OrderActivity.class);
                break;
            case R.id.itemForums://(論壇專區)
                onItemSelectedTo(R.string.stringForum, ForumActivity.class);
                break;
            case R.id.itemLives://(直播專區)
                onItemSelectedTo(R.string.stringLives, LivesActivity.class);
                break;
            case R.id.itemCustomerService://(客服專區)
                onItemSelectedTo(R.string.stringCustomerService, CustomerServiceActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onItemSelectedTo(int toastString, Class toClass) {
        //設定要跳轉的Activity&動畫
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, toClass);
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
        startActivity(intent);
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        provider.setIcon(R.drawable.ic_broadcast_icon);
    }
}
