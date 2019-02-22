package idv.david.foodgodapp;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.david.foodgodapp.order.fragment.FestOrderFragment;
import idv.david.foodgodapp.order.fragment.FoodOrderFragment;
import idv.david.foodgodapp.order.fragment.MenuOrderFragment;


//(訂單專區)
public class OrderActivity extends AppCompatActivity {
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView textView;
    private boolean isOnClick;
    private Spinner reviewStauts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        View bottomSheet = findViewById(R.id.bottom_sheet);//下方訂單內容
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));//頁面手勢滑動

        TabLayout tabLayout = findViewById(R.id.tabLayout);//訂單種類滑動列表
        viewPager.isFakeDragging();
        tabLayout.setupWithViewPager(viewPager);

        MenuOrderDisplay();//展示下方訂單內容方法
        findViews();



    }



    private void findViews() {
        reviewStauts = findViewById(R.id.idReviewStatus);
        String[] stringStuts = {"請審核", "審核通過", "審核未通過"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, stringStuts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewStauts.setAdapter(adapter);
        reviewStauts.setSelection(0, true);
//        reviewStauts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void MenuOrderDisplay() {
        textView = findViewById(R.id.idMenu_Order_msg);
        MenuOrder menuOrder = new MenuOrder("M02091", "M1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), new Date(), 0, "", "C00001", "C00001", "M00001");
        String status = menuOrder.getMenu_od_status();
        if ("M1".equals(status)) {
            status = "審核通過";
        } else if ("M0".equals(status)) {
            status = "審核未通過";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("嚴選套餐訂單編號:" + menuOrder.getMenu_od_ID() + "\r\n")
                .append("訂單狀態:" + status + "\r\n")
                .append("下單日期:" + menuOrder.getMenu_od_start() + "\r\n")
                .append("預約日期:" + menuOrder.getMenu_od_book() + "\r\n")
                .append("完成日期:" + menuOrder.getMenu_od_end() + "\r\n")
                .append("訂單評價:" + menuOrder.getMenu_od_rate() + "\r\n")
                .append("訂單評價留言:" + menuOrder.getMenu_od_msg() + "\r\n")
                .append("顧客編號:" + menuOrder.getCust_ID() + "\r\n")
                .append("主廚編號:" + menuOrder.getChef_ID() + "\r\n")
                .append("套餐編號:" + menuOrder.getMenu_ID() + "\r\n");
        textView.setText(sb);
    }

    public void orderClick(View view) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (!isOnClick) {
            isOnClick = true;
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            isOnClick = false;
        }
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

