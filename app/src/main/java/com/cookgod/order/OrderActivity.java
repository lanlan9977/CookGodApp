package com.cookgod.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookgod.R;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//(訂單專區)
public class OrderActivity extends AppCompatActivity {
    public static List<MenuOrderVO> menuOrderVOList;
    private final static String TAG = "OrderActivity";
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView textView;
    private boolean isOnClick;
    private Spinner reviewStauts;
    private AsyncTask retrieveMenuOrderTask;


    private class RetrieveMenuOrderTask extends AsyncTask<String, String, List<MenuOrderVO>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menuOrderVOList = new ArrayList<>();
            progressDialog = new ProgressDialog(OrderActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<MenuOrderVO> doInBackground(String... params) {
            String url = params[0];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("selectMenuOrder", "menuOredrList");
            jsonIn = getRemoteData(url, jsonObject.toString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MenuOrderVO>>() {
            }.getType();

            return gson.fromJson(jsonIn, listType);
        }

        @Override
        protected void onPostExecute(List<MenuOrderVO> items) {
            menuOrderVOList = items;
            if(menuOrderVOList!=null) {
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
        if (networkConnected()) {
            retrieveMenuOrderTask = new RetrieveMenuOrderTask().execute(Util.MenuOrder_Servlet_URL);
        } else {
            Toast.makeText(OrderActivity.this, "網路連線錯誤", Toast.LENGTH_SHORT).show();
        }
        findViews();
    }

    private void findViews() {
        reviewStauts = findViewById(R.id.idReviewStatus);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    private boolean networkConnected() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void MenuOrderDisplay() {
        textView = findViewById(R.id.idMenu_Order_msg);
        MenuOrderVO menuOrderVO = new MenuOrderVO("M02091", "M1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), new Date(), 0, "", "C00001", "C00001", "M00001");
        String status = menuOrderVO.getMenu_od_status();
        if ("M1".equals(status)) {
            status = "審核通過";
        } else if ("M0".equals(status)) {
            status = "審核未通過";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("嚴選套餐訂單編號:" + menuOrderVO.getMenu_od_ID() + "\r\n")
                .append("訂單狀態:" + status + "\r\n")
                .append("下單日期:" + menuOrderVO.getMenu_od_start() + "\r\n")
                .append("預約日期:" + menuOrderVO.getMenu_od_book() + "\r\n")
                .append("完成日期:" + menuOrderVO.getMenu_od_end() + "\r\n")
                .append("訂單評價:" + menuOrderVO.getMenu_od_rate() + "\r\n")
                .append("訂單評價留言:" + menuOrderVO.getMenu_od_msg() + "\r\n")
                .append("顧客編號:" + menuOrderVO.getCust_ID() + "\r\n")
                .append("主廚編號:" + menuOrderVO.getChef_ID() + "\r\n")
                .append("套餐編號:" + menuOrderVO.getMenu_ID() + "\r\n");
        textView.setText(sb);
    }

    public void onOrderClick(View view ) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(985);
        if (!isOnClick) {
            isOnClick = true;
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            isOnClick = false;
        }
        MenuOrderDisplay();//展示下方訂單內容方法
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
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

