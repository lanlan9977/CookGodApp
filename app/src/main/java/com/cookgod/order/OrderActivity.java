package com.cookgod.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.cookgod.cust.CustVO;
import com.cookgod.main.Page;
import com.cookgod.main.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//(訂單專區)
public class OrderActivity extends AppCompatActivity {
    private CustVO cust_account;
    public List<MenuOrderVO> menuOrderList;
    public List<FoodOrderVO> foodOrderList;
    private final static String TAG = "OrderActivity";
    private AsyncTask retrieveMenuOrderTask;

    public List<MenuOrderVO> getMenuOrderList() {
        return menuOrderList;
    }

    public List<FoodOrderVO> getFoodOrderList() {
        return foodOrderList;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            if (Util.networkConnected(this)) {
                retrieveMenuOrderTask = new RetrieveMenuOrderTask().execute(Util.MenuOrder_Servlet_URL, preferences.getString("cust_ID", ""));
            } else {

            }
        }
    }

    private class RetrieveMenuOrderTask extends AsyncTask<String, String, List<MenuOrderVO>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menuOrderList = new ArrayList<>();
            progressDialog = new ProgressDialog(OrderActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<MenuOrderVO> doInBackground(String... params) {
            String url = params[0];
            String cust_ID = params[1];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("selectMenuOrder", cust_ID);
            jsonIn = getRemoteData(url, jsonObject.toString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MenuOrderVO>>() {
            }.getType();

            return gson.fromJson(jsonIn, listType);
        }

        @Override
        protected void onPostExecute(List<MenuOrderVO> items) {
            menuOrderList = items;
            if (menuOrderList != null) {
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));//頁面手勢滑動

                TabLayout tabLayout = findViewById(R.id.tabLayout);//訂單種類滑動列表
                viewPager.isFakeDragging();
                tabLayout.setupWithViewPager(viewPager);
            }
            progressDialog.cancel();
        }


    }

    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findViews();
    }

    private void findViews() {
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MenuOrderFragment(), "嚴選套餐訂單"));
            pageList.add(new Page(new FestOrderFragment(), "節慶主題訂單"));
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
                break;
        }
        return false;
    }


}

