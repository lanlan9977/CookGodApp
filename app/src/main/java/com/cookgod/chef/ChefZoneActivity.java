package com.cookgod.chef;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cookgod.R;
import com.cookgod.main.Page;
import com.cookgod.main.Util;
import com.cookgod.order.MenuOrderVO;
import com.cookgod.task.RetrieveOrdeByChefTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//(論壇專區)
public class ChefZoneActivity extends AppCompatActivity {
    private final static String TAG = "ChefZoneActivity";
    private Boolean isChef, login;
    private RetrieveOrdeByChefTask retrieveOrdeByChefTask;
    private List<MenuOrderVO> menuOrderList;

    public List<MenuOrderVO> getMenuOrderList() {
        return menuOrderList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefzone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        String chef_ID=preferences.getString("chef_ID","");

        retrieveOrdeByChefTask =new RetrieveOrdeByChefTask(Util.Servlet_URL+"OrderByChefServlet",chef_ID);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn= retrieveOrdeByChefTask.execute().get();
            Type listType=new TypeToken<List<String>>() {
            }.getType();
            List<String> stringList=gson.fromJson(jsonIn,listType);
            Type menuOrderType = new TypeToken<List<MenuOrderVO>>() {
            }.getType();
            menuOrderList=gson.fromJson(stringList.get(0),menuOrderType);

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }













        ViewPager viewPager = findViewById(R.id.idChefViewPager);
        viewPager.setAdapter(new ChefPagerAdapter(getSupportFragmentManager()));//頁面手勢滑動
        TabLayout tabLayout = findViewById(R.id.idChefTabLayout);//訂單種類滑動列表
        viewPager.isFakeDragging();
        tabLayout.setupWithViewPager(viewPager);
    }
    private class ChefPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public ChefPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new ChefMenuOrderFragment(), "嚴選套餐訂單"));
            pageList.add(new Page(new ChefFestOrderFragment(), "節慶主題訂單"));
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
}

