package com.cookgod.order;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.other.Contents;
import com.cookgod.other.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class MenuOrderFragment extends Fragment {
    private final static String TAG = "OrderActivity";
    private List<MenuOrderVO> menuOrderList;
    private LinearLayout idMenu_Order_Layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView idMenu_Order_msg;
    private Boolean isOnClick = true;
    private Button btnMenuOrder;
    private String menu_ID;
    private ImageView ivCode;



    @Override
    public void onAttach(Context context) {//從OrderActivity取得物件資料
        super.onAttach(context);
        menuOrderList = ((OrderActivity) context).getMenuOrderList();
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
        idMenu_Order_msg = view.findViewById(R.id.idMenu_Order_msg);//設定bottomSheetBehavior中的TextView(顯示訂單內容)
        btnMenuOrder = view.findViewById(R.id.idMenuOrderButton);
        ivCode = view.findViewById(R.id.ivCode);

        return view;
    }

    private class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.ViewHolder> {//CardView顯示訂單Id與日期
        private LayoutInflater inflater;

        public MenuOrderAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView idMenu_or_id, idMenu_or_appt;

            public ViewHolder(View itemView) {
                super(itemView);
                idMenu_or_id = itemView.findViewById(R.id.idMenu_or_id);
                idMenu_or_appt = itemView.findViewById(R.id.idMenu_or_appt);
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
                if ("g0".equals(menuOrderVO.getMenu_od_status())) {
                    idMenu_Order_Layout.setBackgroundColor(getResources().getColor(R.color.colorRed));
                }
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayMenuOrder(position);
                    menu_ID = menuOrderList.get(position).getMenu_ID();
                    if (isOnClick) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setPeekHeight(815);
                        isOnClick = false;
                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        isOnClick = true;
                    }
                }
            });
            String qrCodeText=menuOrderList.get(position).getMenu_od_ID();
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
        MenuOrderVO menuOrder = menuOrderList.get(position);
        String status = menuOrder.getMenu_od_status();
        if ("g1".equals(status)) {
            status = "審核通過";
        } else if ("g0".equals(status)) {
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
                ;
        idMenu_Order_msg.setText(sb);
        btnMenuOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuOrderDetailActivity.class);
                intent.putExtra("menu_ID", menu_ID);
                startActivity(intent);
            }
        });
    }
    private int getDimension() {
        WindowManager manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }



}

