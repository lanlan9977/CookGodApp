package com.cookgod.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.chef.ChefOrderDetailActivity;
import com.cookgod.foodsup.FoodMallActivity;
import com.cookgod.main.Util;
import com.cookgod.other.Contents;
import com.cookgod.other.QRCodeEncoder;
import com.cookgod.task.RetrieveChefOrderDetailTask;
import com.cookgod.task.RetrieveMenuOrderRate;
import com.cookgod.task.RetrieveMenuOrderStatus;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class MenuOrderFragment extends Fragment {
    private final static String TAG = "OrderActivity";
    private List<MenuOrderVO> menuOrderList;
    private TextView idMenu_Order_ID, idMenu_Order_Status, idMenu_Order_Start, idMenu_Order_Appt, idMenu_Order_End, idMenu_Order_Rate, idMenu_Order_Msg;
    private Boolean isOnClick = true;
    private Button btnMenuOrder, btnMenu_od_rate, btnMenu_od_Food_Order, btnCheckChefFoodOrder, idMenu_od_status, btnMenu_od_pay;
    private RatingBar idMenu_od_ratinggbar;
    private String menu_ID, cust_ID,cust_name,cust_id_location;
    private ImageView ivCode;
    private Boolean isChef;
    private BottomSheetBehavior bottomSheetBehavior;
    private Dialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MenuOrderAdapter menuOrderAdapter;
    private RetrieveChefOrderDetailTask retrieveChefOrderDetailTask;


    @Override
    public void onAttach(Context context) {//從OrderActivity取得物件資料
        super.onAttach(context);
        menuOrderList = ((OrderActivity) context).getMenuOrderList();
        isChef = ((OrderActivity) context).getIsChef();
   cust_name=((OrderActivity) context).getCust_name();
    }

    public String setData() {
        return cust_id_location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menuorder, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlack, R.color.colorWhite,
                R.color.colorBlack, R.color.colorWhite);

        RecyclerView recyclerView = view.findViewById(R.id.menuOrderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//設定recyclerView
        recyclerView.setAdapter(menuOrderAdapter = new MenuOrderAdapter(inflater));
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        btnMenuOrder = view.findViewById(R.id.idMenuOrderButton); //設定bottomSheetBehavior中的TextView(顯示訂單內容)
        btnMenu_od_rate = view.findViewById(R.id.btnMenu_od_rate);
        btnMenu_od_Food_Order = view.findViewById(R.id.btnMenu_od_Food_Order);
        if (isChef) {
            btnMenu_od_rate.setVisibility(View.GONE);
        } else {
            btnMenu_od_Food_Order.setVisibility(View.GONE);
        }
        ivCode = view.findViewById(R.id.ivCode);
        idMenu_Order_ID = view.findViewById(R.id.idMenu_Order_id);
        idMenu_Order_Status = view.findViewById(R.id.idMenu_Order_status);
        idMenu_Order_Start = view.findViewById(R.id.idMenu_Order_start);
        idMenu_Order_Appt = view.findViewById(R.id.idMenu_Order_appt);
        idMenu_Order_End = view.findViewById(R.id.idMenu_Order_end);
        idMenu_Order_Rate = view.findViewById(R.id.idMenu_Order_rate);
        idMenu_Order_Msg = view.findViewById(R.id.idMenu_Order_msg);
        idMenu_od_ratinggbar = view.findViewById(R.id.idMenu_od_ratinggbar);

        btnCheckChefFoodOrder = view.findViewById(R.id.btnCheckChefFoodOrder);
        idMenu_od_status = view.findViewById(R.id.idMenu_od_status);
        btnMenu_od_pay = view.findViewById(R.id.btnMenu_od_pay);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((OrderActivity) getActivity()).getData();
                menuOrderList = ((OrderActivity) getActivity()).setData();
                menuOrderAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.ViewHolder> {//CardView顯示訂單Id與日期
        private LayoutInflater inflater;

        public MenuOrderAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView idMenu_or_id, idMenu_or_appt, idMenu_or_status;
            private ImageView idMenu_or_icon;
            private RatingBar idMenu_or_rate_up;


            public ViewHolder(View itemView) {
                super(itemView);
                idMenu_or_id = itemView.findViewById(R.id.idMenu_or_id);
                idMenu_or_appt = itemView.findViewById(R.id.idMenu_or_appt);
                idMenu_or_status = itemView.findViewById(R.id.idMenu_or_status);
                idMenu_or_icon = itemView.findViewById(R.id.idMenu_or_icon);
                idMenu_or_rate_up = itemView.findViewById(R.id.idMenu_or_rate_up);
                //設定bottomSheetBehavior

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.card_menuorder, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (!menuOrderList.isEmpty()) {
                MenuOrderVO menuOrderVO = menuOrderList.get(position);
                cust_ID = menuOrderVO.getCust_ID();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH : mm ");
                viewHolder.idMenu_or_id.setText("訂單編號：" + menuOrderVO.getMenu_od_ID());
                viewHolder.idMenu_or_appt.setText("預約日期：" + sdf.format(menuOrderVO.getMenu_od_book()));
                viewHolder.idMenu_or_rate_up.setRating(menuOrderVO.getMenu_od_rate());

                switch (menuOrderVO.getMenu_od_status()) {
                    case "g0":
                        viewHolder.idMenu_or_status.setText("未審核訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_question);
                        break;
                    case "g1":
                        viewHolder.idMenu_or_status.setText("未通過訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_error);
                        break;
                    case "g2":
                        viewHolder.idMenu_or_status.setText("訂單待付款");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_question);
                        break;
                    case "g3":
                        viewHolder.idMenu_or_status.setText("進行中訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_wait);
                        break;
                    case "g4":
                        viewHolder.idMenu_or_status.setText("進行中訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_home);
                        break;
                    case "g5":
                        viewHolder.idMenu_or_status.setText("已完成訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_checked);
                        break;
                }

            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayMenuOrder(position);
                    menu_ID = menuOrderList.get(position).getMenu_ID();
                    cust_id_location= menuOrderList.get(position).getCust_ID();


                    DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
                    float highSize = metrics.heightPixels;
                    Log.e(TAG, "" + highSize);
                    if (isOnClick) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        if (highSize == 1024) {
                            bottomSheetBehavior.setPeekHeight(290);
                        } else {
                            bottomSheetBehavior.setPeekHeight(580);
                        }

                        isOnClick = false;

                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        isOnClick = true;

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return menuOrderList.size();
        }

    }

    private void displayMenuOrder(int position) {
        Handler handler = new Handler();
        String qrCodeText = menuOrderList.get(position).getMenu_od_ID();
        int smallerDimension = getDimension();
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ivCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        final MenuOrderVO menuOrder = menuOrderList.get(position);
        String status = menuOrder.getMenu_od_status();
        if ("g0".equals(status)) {
            status = "尚未審核";
            if (isChef) {
                idMenu_od_status.setVisibility(View.VISIBLE);
                btnMenu_od_Food_Order.setVisibility(View.GONE);
                btnCheckChefFoodOrder.setVisibility(View.GONE);
            }else{
                btnMenu_od_pay.setVisibility(View.GONE);
            }
        } else if ("g1".equals(status)) {
            status = "審核未通過";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
                idMenu_od_ratinggbar.setVisibility(View.GONE);
                btnMenu_od_rate.setVisibility(View.GONE);
                btnMenu_od_Food_Order.setVisibility(View.GONE);
                btnCheckChefFoodOrder.setVisibility(View.GONE);
            }else{
                btnMenu_od_pay.setVisibility(View.GONE);
            }
        } else if ("g2".equals(status)) {
            status = "審核通過";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
                idMenu_od_ratinggbar.setVisibility(View.GONE);
                btnMenu_od_rate.setVisibility(View.GONE);
                btnMenu_od_Food_Order.setVisibility(View.GONE);
                btnCheckChefFoodOrder.setVisibility(View.GONE);

            }else{
                btnMenu_od_pay.setVisibility(View.VISIBLE);
            }
        } else if ("g3".equals(status)) {
            status = "審核通過";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
                btnMenu_od_Food_Order.setVisibility(View.VISIBLE);
                btnCheckChefFoodOrder.setVisibility(View.VISIBLE);

            }else{
                btnMenu_od_pay.setVisibility(View.GONE);
            }
        } else if ("g4".equals(status)) {
            status = "主廚到府";
            if (isChef) {
                btnMenu_od_Food_Order.setVisibility(View.VISIBLE);
                btnCheckChefFoodOrder.setVisibility(View.VISIBLE);
                idMenu_od_status.setVisibility(View.GONE);
            }else{
                btnMenu_od_pay.setVisibility(View.GONE);
            }
        } else if ("g5".equals(status)) {
            status = "訂單完成";
            if (!isChef) {
                if ((menuOrder.getMenu_od_rate() == null)) {
                    idMenu_od_ratinggbar.setVisibility(View.VISIBLE);
                    btnMenu_od_rate.setVisibility(View.VISIBLE);
                    btnMenu_od_pay.setVisibility(View.GONE);
                } else {
                    idMenu_od_ratinggbar.setVisibility(View.VISIBLE);
                    btnMenu_od_rate.setVisibility(View.VISIBLE);
                    btnMenu_od_pay.setVisibility(View.GONE);
                }
            } else {
                idMenu_od_status.setVisibility(View.GONE);
                btnMenu_od_Food_Order.setVisibility(View.GONE);
                btnCheckChefFoodOrder.setVisibility(View.GONE);

            }
        }
        DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String endDate = "";
        if (menuOrder.getMenu_od_end() == null) {
            idMenu_Order_End.setTextColor(getResources().getColor(R.color.colorRed));
            endDate = "訂單尚未完成";
        } else {
            endDate = sdf.format(menuOrder.getMenu_od_end());
        }
        String rateMsg = "";
        if (menuOrder.getMenu_od_msg() == null && menuOrder.getMenu_od_rate() <= 0) {
            rateMsg = "尚未給評";
            idMenu_Order_Rate.setText("訂單評價：" + rateMsg);
            idMenu_Order_Rate.setTextColor(getResources().getColor(R.color.colorRed));
            idMenu_Order_Msg.setTextColor(getResources().getColor(R.color.colorRed));
        } else if (menuOrder.getMenu_od_msg() == null) {
            idMenu_Order_Rate.setText("訂單評價：" + menuOrder.getMenu_od_rate() + "顆星");
            rateMsg = "沒有評價留言";
        } else {
            rateMsg = menuOrder.getMenu_od_msg();
            idMenu_Order_Rate.setText("訂單評價：" + menuOrder.getMenu_od_rate() + "顆星");
        }


        idMenu_Order_ID.setText("嚴選套餐訂單編號：" + menuOrder.getMenu_od_ID());
        idMenu_Order_Status.setText("訂單狀態：" + status);
        idMenu_Order_Start.setText("下單日期：" + sdf.format(menuOrder.getMenu_od_start()));
        idMenu_Order_Appt.setText("預約日期：" + sdf.format(menuOrder.getMenu_od_book()));
        idMenu_Order_End.setText("完成日期：" + endDate);

        idMenu_Order_Msg.setText("訂單評價留言：" + rateMsg);
        btnMenuOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuOrderDetailActivity.class);
                intent.putExtra("menu_ID", menu_ID);
                startActivity(intent);
            }
        });
        btnMenu_od_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final float[] menu_od_rate = {0};
//        Log.e(TAG, String.valueOf(menu_od_rate));

        idMenu_od_ratinggbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean b) {
                menu_od_rate[0] = rate;
            }
        });


        btnMenu_od_rate.setOnClickListener(new View.OnClickListener() {
            RetrieveMenuOrderRate retrieveMenuOrderRate;

            @Override
            public void onClick(View v) {
                DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
                float highSize = metrics.heightPixels;
                dialog = new Dialog(getContext());
                dialog.setTitle("評價留言");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_foodorderrate);
                final Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                if (highSize == 1024) {
                    lp.width = 500;
                } else {
                    lp.width = 700;
                }
                lp.alpha = 1.0f;
                dialogWindow.setAttributes(lp);
                Button btnOrder_Rate_Ok = dialog.findViewById(R.id.btnOrder_Rate_Ok);
                Button btnOrder_Rate_Back = dialog.findViewById(R.id.btnOrder_Rate_Back);
                final EditText idOrder_Msg = dialog.findViewById(R.id.idOrder_Msg);
                RatingBar idOrder_Rate = dialog.findViewById(R.id.idOrder_Rate);
                idOrder_Rate.setRating(menu_od_rate[0]);
                btnOrder_Rate_Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = idOrder_Msg.getText().toString().trim();
                        retrieveMenuOrderRate = new RetrieveMenuOrderRate(Util.Servlet_URL + "MenuOrderRateServlet", String.valueOf(menu_od_rate[0]), menuOrder.getMenu_od_ID(), msg);
                        retrieveMenuOrderRate.execute();
                        Util.showToast(getActivity(), "評價成功");
                        dialog.dismiss();

                    }
                });
                btnOrder_Rate_Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        btnMenu_od_Food_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FoodMallActivity.class);
                intent.putExtra("menu_od_ID", menuOrder.getMenu_od_ID());
                startActivity(intent);
            }
        });
        btnCheckChefFoodOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getContext(), ChefOrderDetailActivity.class);
                final SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                        getActivity().MODE_PRIVATE);
                final String check = preferences.getString(menuOrder.getMenu_od_ID(), "");
                if (check != null && !check.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("是否要前往該套餐之食材訂單？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(intent);
                        }
                    });
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent.putExtra("chef_or_ID", check);
                            intent.putExtra("getOne", true);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    startActivity(intent);
                }

            }
        });
        idMenu_od_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final MenuOrderVO menuOrderVO = new MenuOrderVO();
                menuOrderVO.setCust_ID(menuOrder.getCust_ID());
                menuOrderVO.setMenu_od_ID(menuOrder.getMenu_od_ID());
                builder.setTitle("請審核訂單");
                final List<String> list = new ArrayList<>();
                builder.setPositiveButton("審核通過", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RetrieveMenuOrderStatus retrieveMenuOrderStatus;
                        retrieveMenuOrderStatus = new RetrieveMenuOrderStatus(Util.Servlet_URL + "MenuOrderServlet", "g2", menuOrder.getMenu_od_ID());
                        retrieveMenuOrderStatus.execute();
                        menuOrderVO.setMenu_od_status("g2");
                        String menu_order_json = new Gson().toJson(menuOrderVO);
                        list.add("menu_order");
                        list.add(menu_order_json);
                        String message = new Gson().toJson(list);
                        Util.broadcastSocket.send(message);
                        Util.showToast(getActivity(), "審核完畢");
                    }
                });
                builder.setNegativeButton("審核不通過", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RetrieveMenuOrderStatus retrieveMenuOrderStatus;
                        retrieveMenuOrderStatus = new RetrieveMenuOrderStatus(Util.Servlet_URL + "MenuOrderServlet", "g1", menuOrder.getMenu_od_ID());
                        retrieveMenuOrderStatus.execute();
                        menuOrderVO.setMenu_od_status("g1");
                        String menu_order_json = new Gson().toJson(menuOrderVO);
                        list.add("menu_order");
                        list.add(menu_order_json);
                        String message = new Gson().toJson(list);
                        Util.broadcastSocket.send(message);
                        Util.showToast(getActivity(), "審核完畢");
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }

        });
        btnMenu_od_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext(),R.style.PauseDialog);
                dialog.setTitle("確認付款");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_menuorderpay);
                final ImageView idCard1 = dialog.findViewById(R.id.idCard1);
                final ImageView idCard2 = dialog.findViewById(R.id.idCard2);
                final ImageView idCard3 = dialog.findViewById(R.id.idCard3);
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
                idCard2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        idCard2.bringToFront();
                        idCardName.setText(cust_name);
                        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.6f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        mShowAction1.setDuration(600);
                        idCard1.setAnimation(mShowAction1);
                        idCardLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha));
                        idCareText.setText("");
                        TranslateAnimation mShowAction2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, -0.6f, Animation.RELATIVE_TO_SELF,
                                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        mShowAction2.setDuration(600);

                        idCard3.setAnimation(mShowAction2);
                        idCard1.setVisibility(View.GONE);
                        idCard3.setVisibility(View.GONE);
                        idCardLayout.setVisibility(View.VISIBLE);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                retrieveChefOrderDetailTask = (RetrieveChefOrderDetailTask) new RetrieveChefOrderDetailTask(Util.Servlet_URL + "ChefOdDetailByChefServlet", "g3", menuOrder.getMenu_od_ID(),0, "updateCust").execute();
                                onStart();
                                Util.showToast(getContext(), "付款完畢");
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Util.showToast(getContext(), "取消付款");
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
                lp.width = 750;
                lp.alpha = 1.0f;
                dialogWindow.setAttributes(lp);
                dialog.show();
            }

        });
    }

    private int getDimension() {
        WindowManager manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }


}

