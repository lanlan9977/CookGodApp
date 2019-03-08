package com.cookgod.chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookgod.R;
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
    private String chef_ID;
    private RetrieveChefOrderDetailTask retrieveChefOrderDetailTask;
    private List<ChefOdDetailVO> chefOdDetailList;
    private List<ChefOrderVO> chefOrderList;
    private RecyclerView idChefOrderDetailRecyclerView;




    private Map<String, List<ChefOdDetailVO>> chefOdDetailMap;

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
        retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID);
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
            chefOrderList = gson.fromJson(stringList.get(0), chefOrderType);
            chefOdDetailMap = gson.fromJson(stringList.get(1), chefOdDetailType);
//            Log.e(TAG,""+chefOdDetailList.size());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        idChefOrderDetailRecyclerView.setAdapter(new ChefOrderDetailAdapter(ChefOrderDetailActivity.this, chefOrderList));

    }

    private class ChefOrderDetailAdapter extends RecyclerView.Adapter<ChefOrderDetailAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ChefOrderVO> chefOrderList;
        private Context context;
        private boolean[] teamDetailExpanded;

        public ChefOrderDetailAdapter(Context context, List<ChefOrderVO> chefOrderList) {
            this.context = context;
            this.chefOrderList = chefOrderList;
            layoutInflater = LayoutInflater.from(context);
            this.teamDetailExpanded = new boolean[chefOrderList.size()];
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idChefOrder_ID, idChefOrder_Status, idChefOrder_Name, idChefOrder_Start, idChefOrder_Tel,
                    idChefOrder_Addr, idChefOrder_Send, idChefOrder_Rcv, idChefOrder_End;

            RelativeLayout idChefOrder_RelativeLayout;

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
            chefOdDetailList = new ArrayList<>();
            ChefOrderVO chefOrderVO = chefOrderList.get(i);


                    chefOdDetailList = chefOdDetailMap.get(chefOrderVO.getChef_or_ID());
//                    Log.e(TAG, "" + key);


            Log.e(TAG, "" + chefOdDetailList.size());


            viewHolder.idChefOrder_ID.setText("主廚食材訂單編號：" + chefOrderVO.getChef_or_ID());
            viewHolder.idChefOrder_Status.setText("訂單狀態：" + chefOrderVO.getChef_or_status());
            viewHolder.idChefOrder_Name.setText("收件人姓名：" + chefOrderVO.getChef_or_name());
            viewHolder.idChefOrder_Start.setText("下單日期" + sdf.format(chefOrderVO.getChef_or_start()));
            viewHolder.idChefOrder_Tel.setText("收件人電話：" + chefOrderVO.getChef_or_tel());
            viewHolder.idChefOrder_Addr.setText("收件人地址：" + chefOrderVO.getChef_or_addr());
            viewHolder.idChefOrder_Send.setText("出貨日期：" + sdf.format(chefOrderVO.getChef_or_send()));
//            if(chefOrderVO.getChef_or_rcv()!=null){
//                viewHolder.idChefOrder_Rcv.setText(sdf.format(chefOrderVO.getChef_or_rcv()));
//            }
//            if(chefOrderVO.getChef_or_end()!=null){
//                viewHolder.idChefOrder_End.setText(sdf.format(chefOrderVO.getChef_or_end()));
//            }

            if (chefOdDetailList != null)

            {
                viewHolder.idChefOrder_RelativeLayout.setOnClickListener(new View.OnClickListener() {

                    class ChefOrderDetailListAdapter extends RecyclerView.Adapter<ChefOrderDetailListAdapter.ViewHolder> {
                        private LayoutInflater layoutInflater;
                        private List<ChefOdDetailVO> chefOdDetailList;
                        private Context context;

                        public ChefOrderDetailListAdapter(Context context, List<ChefOdDetailVO> chefOdDetailList) {
                            this.chefOdDetailList = chefOdDetailList;
                            this.context = context;
                            layoutInflater = LayoutInflater.from(context);
                        }

                        class ViewHolder extends RecyclerView.ViewHolder {
                            TextView idChefOrderDetail_ID;

                            public ViewHolder(View itemView) {
                                super(itemView);
                                idChefOrderDetail_ID = itemView.findViewById(R.id.idChefOrderDetail_ID);

                            }
                        }


                        @Override
                        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                            View itemView = layoutInflater.inflate(R.layout.card_cheforderdetaillist, viewGroup, false);
                            return new ViewHolder(itemView);
                        }

                        @Override
                        public void onBindViewHolder(ViewHolder viewHolder, int i) {

                            viewHolder.idChefOrderDetail_ID.setText(chefOdDetailList.get(i).getChef_or_ID());

                        }

                        @Override
                        public int getItemCount() {
                            return chefOdDetailList.size();
                        }
                    }

                    @Override
                    public void onClick(View v) {
                        RecyclerView idChefOrderDetailListView = v.findViewById(R.id.idChefOrderDetailListView);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(ChefOrderDetailActivity.this);
                        Log.e(TAG, "" + idChefOrderDetailListView);
                        RelativeLayout relativeLayout = v.findViewById(R.id.idChefOrder_RelativeLayout);
                        idChefOrderDetailListView.setLayoutManager(layoutManager);
                        idChefOrderDetailListView.setAdapter(new ChefOrderDetailListAdapter(ChefOrderDetailActivity.this, chefOdDetailList));
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return chefOrderList.size();
        }
    }
}
