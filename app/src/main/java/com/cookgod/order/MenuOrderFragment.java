package com.cookgod.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.foodsup.FoodMallActivity;
import com.cookgod.main.Util;
import com.cookgod.other.Contents;
import com.cookgod.other.QRCodeEncoder;
import com.cookgod.task.RetrieveMenuOrderRate;
import com.cookgod.task.RetrieveMenuOrderStatus;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class MenuOrderFragment extends Fragment {
    private final static String TAG = "OrderActivity";
    private List<MenuOrderVO> menuOrderList;
    private RelativeLayout idMenu_Order_Layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView idMenu_Order_ID, idMenu_Order_Status, idMenu_Order_Start, idMenu_Order_Appt, idMenu_Order_End, idMenu_Order_Rate, idMenu_Order_Msg;
    private Boolean isOnClick = true;
    private Button btnMenuOrder, btnMenu_od_rate, btnMenu_od_Food_Order, btnCheckChefFoodOrder, idMenu_od_status;
    private RatingBar idMenu_od_ratinggbar;
    private String menu_ID;
    private ImageView ivCode;
    private Boolean isChef;

    private int REQUEST_CHER_ORDER = 1;

    @Override
    public void onAttach(Context context) {//從OrderActivity取得物件資料
        super.onAttach(context);
        menuOrderList = ((OrderActivity) context).getMenuOrderList();
        isChef = ((OrderActivity) context).getIsChef();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menuorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.menuOrderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//設定recyclerView
        recyclerView.setAdapter(new MenuOrderAdapter(inflater));
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);//設定bottomSheetBehavior
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
                idMenu_or_rate_up=itemView.findViewById(R.id.idMenu_or_rate_up);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.card_menuorder, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            idMenu_Order_Layout = itemView.findViewById(R.id.idMenu_Order_Layout);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (!menuOrderList.isEmpty()) {
                MenuOrderVO menuOrderVO = menuOrderList.get(position);
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
                        viewHolder.idMenu_or_status.setText("進行中訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_wait);
                        break;
                    case "g2":
                        viewHolder.idMenu_or_status.setText("未通過訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_error);
                        break;
                    case "g3":
                        viewHolder.idMenu_or_status.setText("進行中訂單");
                        viewHolder.idMenu_or_icon.setImageResource(R.drawable.ic_wait);
                        break;
                    case "g4":
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
                    if (isOnClick) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setPeekHeight(755);
                        isOnClick = false;
                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        isOnClick = true;
                    }
                }
            });
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
        }

        @Override
        public int getItemCount() {
            return menuOrderList.size();
        }
    }

    private void displayMenuOrder(int position) {
        final MenuOrderVO menuOrder = menuOrderList.get(position);
        String status = menuOrder.getMenu_od_status();
        if ("g0".equals(status)) {
            status = "尚未審核";
            if (isChef) {
                idMenu_od_status.setVisibility(View.VISIBLE);
            }
        } else if ("g2".equals(status)) {
            status = "審核未通過";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
                idMenu_od_ratinggbar.setVisibility(View.GONE);
                btnMenu_od_rate.setVisibility(View.GONE);
            }
        } else if ("g1".equals(status)) {
            status = "審核通過";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
                btnMenu_od_Food_Order.setVisibility(View.VISIBLE);
                btnCheckChefFoodOrder.setVisibility(View.VISIBLE);
            }
        } else if ("g3".equals(status)) {
            status = "主廚到府";
            if (isChef) {
                idMenu_od_status.setVisibility(View.GONE);
            }
        } else if ("g4".equals(status)) {
            status = "訂單完成";
            if (!isChef) {
                if ((menuOrder.getMenu_od_rate() == 0)) {
                    idMenu_od_ratinggbar.setVisibility(View.VISIBLE);
                    btnMenu_od_rate.setVisibility(View.VISIBLE);
                } else {
                    idMenu_od_ratinggbar.setVisibility(View.VISIBLE);
                    idMenu_od_ratinggbar.isIndicator();
                    btnMenu_od_rate.setVisibility(View.GONE);
                }
            } else {
                idMenu_od_status.setVisibility(View.GONE);
            }
        }
        String endDate = "";
        if (menuOrder.getMenu_od_end() == null) {
            endDate = "訂單尚未完成";
        }

        DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        idMenu_Order_ID.setText("嚴選套餐訂單編號:" + menuOrder.getMenu_od_ID());
        idMenu_Order_Status.setText("訂單狀態:" + status);
        idMenu_Order_Start.setText("下單日期:" + sdf.format(menuOrder.getMenu_od_start()));
        idMenu_Order_Appt.setText("預約日期:" + sdf.format(menuOrder.getMenu_od_book()));
        idMenu_Order_End.setText("完成日期" + endDate);
        idMenu_Order_Rate.setText("訂單評價:" + menuOrder.getMenu_od_rate());
        idMenu_Order_Msg.setText("訂單評價留言:" + menuOrder.getMenu_od_msg());
        btnMenuOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuOrderDetailActivity.class);
                intent.putExtra("menu_ID", menu_ID);
                startActivity(intent);
            }
        });
        btnMenu_od_rate.setOnClickListener(new View.OnClickListener() {
            RetrieveMenuOrderRate retrieveMenuOrderRate;

            @Override
            public void onClick(View v) {
                float menu_od_rate = idMenu_od_ratinggbar.getRating();
                retrieveMenuOrderRate = new RetrieveMenuOrderRate(Util.Servlet_URL + "MenuOrderRateServlet", String.valueOf(menu_od_rate), menuOrder.getMenu_od_ID());
                retrieveMenuOrderRate.execute();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("訂單評價已送出");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        btnMenu_od_Food_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FoodMallActivity.class);
                startActivityForResult(intent, REQUEST_CHER_ORDER);
            }
        });
        btnCheckChefFoodOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getContext(),)
            }
        });
        idMenu_od_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("請審核訂單");
                builder.setPositiveButton("審核通過", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RetrieveMenuOrderStatus retrieveMenuOrderStatus;
                        retrieveMenuOrderStatus = new RetrieveMenuOrderStatus(Util.Servlet_URL + "MenuOrderServlet", "g1", menuOrder.getMenu_od_ID());
                        retrieveMenuOrderStatus.execute();
                    }
                });
                builder.setNegativeButton("審核不通過", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RetrieveMenuOrderStatus retrieveMenuOrderStatus;
                        retrieveMenuOrderStatus = new RetrieveMenuOrderStatus(Util.Servlet_URL + "MenuOrderServlet", "g2", menuOrder.getMenu_od_ID());
                        retrieveMenuOrderStatus.execute();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
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

