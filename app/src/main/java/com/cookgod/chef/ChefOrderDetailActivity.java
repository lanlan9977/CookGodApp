package com.cookgod.chef;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private Map<String, List<ChefOdDetailVO>> chefOdDetailMap;
    private Map<String, List<FoodVO>> foodMap;
    private Dialog dialog;
    private ChefOrderDetailAdapter chefOrderDetailAdapter;
    private int total ;

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
        updateChefOrder(getOne);
        idChefOrderDetailRecyclerView.setAdapter(chefOrderDetailAdapter=new ChefOrderDetailAdapter(ChefOrderDetailActivity.this, chefOrderList));
    }

    private void updateChefOrder(Boolean getOne) {
        if (getOne) {
            retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID, chef_or_ID,0,"add");
        } else {
            retrieveChefOrderDetailTask = new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", chef_ID, "",0,"add");
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
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        updateChefOrder(true);
//    }

    private class ChefOrderDetailAdapter extends RecyclerView.Adapter<ChefOrderDetailAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ChefOrderVO> chefOrderList;
        private Context context;
        private Handler handler = new Handler();

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
            Button btnOrderDetail, idPayOrder;

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
                btnOrderDetail = itemView.findViewById(R.id.btnOrderDetail);
                idPayOrder = itemView.findViewById(R.id.idPayOrder);
                idChefOrder_Rcv = itemView.findViewById(R.id.idChefOrder_Rcv);
                idChefOrder_End = itemView.findViewById(R.id.idChefOrder_End);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_cheforderdetail, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            viewHolder.chefOdDetailList = new ArrayList<>();
            viewHolder.foodList = new ArrayList<>();
            final ChefOrderVO chefOrderVO = chefOrderList.get(i);

            for (String key : chefOdDetailMap.keySet()) {
                if (chefOrderVO.getChef_or_ID().equals(key)) {
                    viewHolder.chefOdDetailList = chefOdDetailMap.get(key);
                    viewHolder.foodList = foodMap.get(key);
                }
            }
            total=0;
            for (ChefOdDetailVO chefOdDetailVO : viewHolder.chefOdDetailList) {
                total += chefOdDetailVO.getChef_od_stotal();
            }
            String status = chefOrderVO.getChef_or_status();
            String status_string = "";
            switch (status) {
                case "o0":
                    status_string = "未付款";
                    break;
                case "o1":
                    status_string = "等待食材出貨";
                    viewHolder.idChefOrder_Status.setTextColor(getResources().getColor(R.color.colorGreen));
                    break;
                case "02":
                    status_string = "已出貨";
                    break;
                case "o3":
                    status_string = "送達";
                    break;
                case "o4":
                    status_string = "訂單完成";
                    break;
            }
            viewHolder.idChefOrder_ID.setText("主廚食材訂單編號：" + chefOrderVO.getChef_or_ID());
            viewHolder.idChefOrder_Status.setText("訂單狀態：" + status_string);
            viewHolder.idChefOrder_Name.setText("收件人姓名：" + chefOrderVO.getChef_or_name());
            viewHolder.idChefOrder_Start.setText("下單日期：" + sdf.format(chefOrderVO.getChef_or_start()));
            viewHolder.idChefOrder_Tel.setText("收件人電話：" + chefOrderVO.getChef_or_tel());
            viewHolder.idChefOrder_Addr.setText("收件人地址：" + chefOrderVO.getChef_or_addr());
            viewHolder.idChefOrder_Send.setText("出貨日期：" + sdf.format(chefOrderVO.getChef_or_send()));

            viewHolder.idTotal.setText("＄" + String.valueOf(total));
            if (chefOrderVO.getChef_or_rcv() != null) {
                viewHolder.idChefOrder_Rcv.setText("到貨日期：" + sdf.format(chefOrderVO.getChef_or_rcv()));
            } else {
                viewHolder.idChefOrder_Rcv.setText("到貨日期：" + "食材尚未到貨");
                viewHolder.idChefOrder_Rcv.setTextColor(getResources().getColor(R.color.colorRed));
            }
            if (chefOrderVO.getChef_or_end() != null) {
                viewHolder.idChefOrder_End.setText("完成日期：" + sdf.format(chefOrderVO.getChef_or_end()));
            } else {
                viewHolder.idChefOrder_End.setText("完成日期：" + "訂單尚未完成");
                viewHolder.idChefOrder_End.setTextColor(getResources().getColor(R.color.colorRed));
            }
            viewHolder.idPayOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(ChefOrderDetailActivity.this,R.style.PauseDialog);
                    dialog.setTitle("確認付款");
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_cheforderpay);
                    final ImageView idCard1 = dialog.findViewById(R.id.idCard1);
                    final ImageView idCard2 = dialog.findViewById(R.id.idCard2);
                    final LinearLayout idCardLayout = dialog.findViewById(R.id.idCardLayout);
                    final LinearLayout idCardLayoutContent=dialog.findViewById(R.id.idCardLayoutContent);
                    final LinearLayout idPayPwd=dialog.findViewById(R.id.idPayPwd);
                    final  TextView idCareText=dialog.findViewById(R.id.idCareText);
                    final Button btnCardCheck=dialog.findViewById(R.id.btnCardCheck);
                    final Button btnCardNext=dialog.findViewById(R.id.btnCardNext);
                    final EditText idCardName=dialog.findViewById(R.id.idCardName);
                    EditText idCardPwd=dialog.findViewById(R.id.idCardPwd);


                    ProgressBar idPayProgress = dialog.findViewById(R.id.idPayProgress);
                    final int[] progress = {0};

                    idCard1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            idCard1.bringToFront();

                            idCardName.setText(chefOrderVO.getChef_or_name());
                            TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                    0.11f, Animation.RELATIVE_TO_SELF, 0.0f);
                            mShowAction1.setDuration(600);
                            idCard1.setAnimation(mShowAction1);
                            idCardLayout.setAnimation(AnimationUtils.loadAnimation(ChefOrderDetailActivity.this, R.anim.alpha));
                            idCareText.setVisibility(View.GONE);
                            TranslateAnimation mShowAction2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                    -2.05f, Animation.RELATIVE_TO_SELF, 0.0f);
                            mShowAction2.setDuration(600);

                            idCard2.setAnimation(mShowAction2);
                            idCardLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    idCard2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            idCard2.bringToFront();
                        }
                    });
                    btnCardCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            idCardPwd.setError(null);
                            String idPwd = idCardPwd.getText().toString().trim();
                            if (idPwd.isEmpty()) {
                                idCardPwd.setError("密碼不得為空");
                                return;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChefOrderDetailActivity.this);
                            builder.setTitle("是否確定送出付款訊息");
                            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface aDialog, int which) {
                                    idPayProgress.setVisibility(View.VISIBLE);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while (progress[0] <= 100) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        idPayProgress.setProgress(progress[0]);
                                                    }
                                                });
                                                try {
                                                    Thread.sleep(8);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                progress[0]++;
                                                if( progress[0]==100){
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                    }).start();
                                    retrieveChefOrderDetailTask = (RetrieveChefOrderDetailTask) new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", "o1", chef_or_ID, total, "update").execute();
                                    onStart();
                                    Util.showToast(ChefOrderDetailActivity.this, "付款完畢");
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Util.showToast(ChefOrderDetailActivity.this, "取消付款");
                                }
                            });

                            builder.setCancelable(true);
                            AlertDialog dialog = builder.create();
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                        }
                    });
                    btnCardNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnCardCheck.setVisibility(View.VISIBLE);
                            btnCardNext.setVisibility(View.GONE);

                            TranslateAnimation mShowAction1 = new TranslateAnimation(
                                    Animation.RELATIVE_TO_PARENT, 0.0f,
                                    Animation.RELATIVE_TO_PARENT, -1.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f);

                            TranslateAnimation mShowAction2=new TranslateAnimation(
                                    Animation.RELATIVE_TO_PARENT, +1.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f,
                                    Animation.RELATIVE_TO_PARENT, 0.0f);

                            mShowAction1.setDuration(500);
                            mShowAction2.setDuration(500);

                            idCardLayoutContent.setAnimation(mShowAction1);
                            idPayPwd.setAnimation(mShowAction2);
                            idCardLayoutContent.setVisibility(View.GONE);
                            idPayPwd.setVisibility(View.VISIBLE);

                        }
                    });
                    final Window dialogWindow = dialog.getWindow();
                    dialogWindow.setGravity(Gravity.LEFT);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = 730;
                    lp.alpha = 1.0f;
                    dialogWindow.setAttributes(lp);
                    dialog.show();
                }

            });


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
