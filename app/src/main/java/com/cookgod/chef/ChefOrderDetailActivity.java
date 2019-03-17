package com.cookgod.chef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.foodsup.FoodVO;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveChefOrderDetailTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChefOrderDetailActivity extends AppCompatActivity {
    private final static String TAG = "ChefOrderDetailActivity";
    private String chef_ID, chef_or_ID;
    private RetrieveChefOrderDetailTask retrieveChefOrderDetailTask;

    private List<ChefOrderVO> chefOrderList;
    private RecyclerView idChefOrderDetailRecyclerView;


    private Map<String, List<ChefOdDetailVO>> chefOdDetailMap;
    private Map<String, List<FoodVO>> foodMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheforderdetail);
        idChefOrderDetailRecyclerView = findViewById(R.id.idChefOrderDetailRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChefOrderDetailActivity.this);
        idChefOrderDetailRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        chef_ID = preferences.getString("chef_ID", "");
        Intent intent = getIntent();
        chef_or_ID = intent.getStringExtra("chef_or_ID");
        Boolean getOne = intent.getBooleanExtra("getOne", false);

        if (getOne) {
            retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID, chef_or_ID);
        } else {
            retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID, "");
        }
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String jsonIn = retrieveChefOrderDetailTask.execute().get();
            Type stringListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> stringList = gson.fromJson(jsonIn, stringListType);
            Type chefOrderType = new TypeToken<List<ChefOrderVO>>() {
            }.getType();
            Type chefOdDetailType = new TypeToken<Map<String, List<ChefOdDetailVO>>>() {
            }.getType();
            Type foodType = new TypeToken<Map<String, List<FoodVO>>>() {
            }.getType();
            chefOrderList = gson.fromJson(stringList.get(0), chefOrderType);
            chefOdDetailMap = gson.fromJson(stringList.get(1), chefOdDetailType);
            foodMap = gson.fromJson(stringList.get(2), foodType);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        idChefOrderDetailRecyclerView.setAdapter(new ChefOrderDetailAdapter(ChefOrderDetailActivity.this, chefOrderList));
    }

    private class ChefOrderDetailAdapter extends RecyclerView.Adapter<ChefOrderDetailAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ChefOrderVO> chefOrderList;
        private Context context;

        public ChefOrderDetailAdapter(Context context, List<ChefOrderVO> chefOrderList) {
            this.context = context;
            this.chefOrderList = chefOrderList;
            layoutInflater = LayoutInflater.from(context);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idChefOrder_ID, idChefOrder_Status, idChefOrder_Name, idChefOrder_Start, idChefOrder_Tel,
                    idChefOrder_Addr, idChefOrder_Send, idChefOrder_Rcv, idChefOrder_End, idTotal;
            RelativeLayout idChefOrder_RelativeLayout;
            List<ChefOdDetailVO> chefOdDetailList;
            List<FoodVO> foodList;
            LinearLayout foodHeaderLayout, foodBottomLayout;
            Button btnOrderDetail,idPayOrder;

            public ViewHolder(View itemView) {
                super(itemView);
                idChefOrder_ID = itemView.findViewById(R.id.idChefOrder_ID);
                idChefOrder_Status = itemView.findViewById(R.id.idChefOrder_Status);
                idChefOrder_Name = itemView.findViewById(R.id.idChefOrder_Name);
                idChefOrder_Start = itemView.findViewById(R.id.idChefOrder_Start);
                idChefOrder_Tel = itemView.findViewById(R.id.idChefOrder_Tel);
                idChefOrder_Addr = itemView.findViewById(R.id.idChefOrder_Addr);
                idChefOrder_Send = itemView.findViewById(R.id.idChefOrder_Send);
                idChefOrder_RelativeLayout = itemView.findViewById(R.id.idChefOrder_RelativeLayout);
                foodBottomLayout = itemView.findViewById(R.id.foodBottomLayout);
                foodHeaderLayout = itemView.findViewById(R.id.foodHeaderLayout);
                idTotal = itemView.findViewById(R.id.idTotal);
                btnOrderDetail=itemView.findViewById(R.id.btnOrderDetail);
                idPayOrder=itemView.findViewById(R.id.idPayOrder);
//                idChefOrder_Rcv=itemView.findViewById(R.id.idChefOrder_Rcv);
//                idChefOrder_End=itemView.findViewById(R.id.idChefOrder_End);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_cheforderdetail, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

            viewHolder.chefOdDetailList = new ArrayList<>();
            viewHolder.foodList = new ArrayList<>();
            ChefOrderVO chefOrderVO = chefOrderList.get(i);

            for (String key : chefOdDetailMap.keySet()) {
                if (chefOrderVO.getChef_or_ID().equals(key)) {
                    viewHolder.chefOdDetailList = chefOdDetailMap.get(key);
                    viewHolder.foodList = foodMap.get(key);
                }
            }
            int total = 0;
            for (ChefOdDetailVO chefOdDetailVO : viewHolder.chefOdDetailList) {
                total += chefOdDetailVO.getChef_od_stotal();
            }

            String status = chefOrderVO.getChef_or_status();
            String status_string="";
            switch (status) {
                case "o0":
                    status_string="未付款";
                    break;
                case "o1":
                    status_string="未出貨";
                    break;
                case "02":
                    status_string="已出貨";
                    break;
                case "o3":
                    status_string="送達";
                    break;
                case "o4":
                    status_string="訂單完成";
                    break;
            }

            viewHolder.idChefOrder_ID.setText("主廚食材訂單編號：" + chefOrderVO.getChef_or_ID());
            viewHolder.idChefOrder_Status.setText("訂單狀態：" +status_string);
            viewHolder.idChefOrder_Name.setText("收件人姓名：" + chefOrderVO.getChef_or_name());
            viewHolder.idChefOrder_Start.setText("下單日期：" + sdf.format(chefOrderVO.getChef_or_start()));
            viewHolder.idChefOrder_Tel.setText("收件人電話：" + chefOrderVO.getChef_or_tel());
            viewHolder.idChefOrder_Addr.setText("收件人地址：" + chefOrderVO.getChef_or_addr());
            viewHolder.idChefOrder_Send.setText("出貨日期：" + sdf.format(chefOrderVO.getChef_or_send()));
            viewHolder.idTotal.setText("＄" + String.valueOf(total));
//            if(chefOrderVO.getChef_or_rcv()!=null){
//                viewHolder.idChefOrder_Rcv.setText(sdf.format(chefOrderVO.getChef_or_rcv()));
//            }
//            if(chefOrderVO.getChef_or_end()!=null){
//                viewHolder.idChefOrder_End.setText(sdf.format(chefOrderVO.getChef_or_end()));
//            }

            viewHolder.btnOrderDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btnOrderDetail.setVisibility(View.GONE);
                    RecyclerView idChefOrderDetailListView = findViewById(R.id.idChefOrderDetailListView);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(ChefOrderDetailActivity.this);
                    Log.e(TAG, String.valueOf("" + viewHolder.chefOdDetailList == null));
                    if (viewHolder.chefOdDetailList != null && viewHolder.chefOdDetailList.size() > 0) {
                        viewHolder.foodHeaderLayout.setVisibility(View.VISIBLE);
                        viewHolder.foodBottomLayout.setVisibility(View.VISIBLE);
                        idChefOrderDetailListView.setLayoutManager(layoutManager);
                        idChefOrderDetailListView.setAdapter(new ChefOrderDetailListAdapter(ChefOrderDetailActivity.this, viewHolder.chefOdDetailList, viewHolder.foodList));
//                        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(ChefOrderDetailActivity.this, R.anim.set_up);
//                        idChefOrderDetailListView.setAnimation(animAnticipateOvershoot);
//                        viewHolder.foodHeaderLayout.setAnimation(animAnticipateOvershoot);
//                        viewHolder.foodBottomLayout.setAnimation(animAnticipateOvershoot);
                    }
                }

                class ChefOrderDetailListAdapter extends RecyclerView.Adapter<ChefOrderDetailListAdapter.ViewHolder> {
                    private LayoutInflater layoutInflater;
                    private List<ChefOdDetailVO> chefOdDetailList;
                    private Context context;
                    private List<FoodVO> foodList;

                    public ChefOrderDetailListAdapter(Context context, List<ChefOdDetailVO> chefOdDetailList, List<FoodVO> foodList) {
                        this.chefOdDetailList = chefOdDetailList;
                        this.context = context;
                        this.foodList = foodList;
                        layoutInflater = LayoutInflater.from(context);
                    }

                    class ViewHolder extends RecyclerView.ViewHolder {
                        TextView idFood_Name, idChefOrderDetail_Qty, idChefOrderDetail_Stotal;

                        public ViewHolder(View itemView) {
                            super(itemView);
                            idFood_Name = itemView.findViewById(R.id.idFood_Name);
                            idChefOrderDetail_Qty = itemView.findViewById(R.id.idChefOrderDetail_Qty);
                            idChefOrderDetail_Stotal = itemView.findViewById(R.id.idChefOrderDetail_Stotal);


                        }
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                        View itemView = layoutInflater.inflate(R.layout.card_cheforderdetaillist, viewGroup, false);
                        return new ViewHolder(itemView);
                    }

                    @Override
                    public void onBindViewHolder(ViewHolder viewHolder, int i) {
                        FoodVO foodVO = foodList.get(i);
                        ChefOdDetailVO chefOdDetailVO = chefOdDetailList.get(i);
                        viewHolder.idFood_Name.setText(foodVO.getFood_name());
                        viewHolder.idChefOrderDetail_Qty.setText(chefOdDetailVO.getChef_od_qty().toString());
                        viewHolder.idChefOrderDetail_Stotal.setText("＄" + chefOdDetailVO.getChef_od_stotal().toString());

                    }

                    @Override
                    public int getItemCount() {
                        return chefOdDetailList.size();
                    }
                }

            });


        }

        @Override
        public int getItemCount() {
            return chefOrderList.size();
        }
    }
}
