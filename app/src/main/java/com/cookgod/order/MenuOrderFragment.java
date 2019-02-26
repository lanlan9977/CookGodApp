package com.cookgod.order;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cookgod.R;

public class MenuOrderFragment extends Fragment {

    private List<MenuOrderVO> menuOrderVOList;
    private LinearLayout idMenu_Order_Layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private Spinner reviewStauts;
    private TextView textView;
    private Boolean isOnClick = true;


    public MenuOrderFragment() {
        menuOrderVOList = OrderActivity.menuOrderVOList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menuorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.menuOrderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MenuOrderAdapter(inflater));

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        textView = view.findViewById(R.id.idMenu_Order_msg);
        reviewStauts = view.findViewById(R.id.idReviewStatus);
        return view;
    }


    private class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.ViewHolder> {
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
                CardView cardView = itemView.findViewById(R.id.idMenu_Order_CardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOnClick) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            bottomSheetBehavior.setPeekHeight(985);
                            MenuOrderDisplay();
                            isOnClick = false;
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            isOnClick = true;
                        }

                    }
                });

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
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            MenuOrderVO menuOrderVO = menuOrderVOList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH : mm ");
            viewHolder.idMenu_or_id.setText("訂單編號：" + menuOrderVO.getMenu_od_ID());
            viewHolder.idMenu_or_appt.setText("預約日期：" + sdf.format(menuOrderVO.getMenu_od_book()));
            if ("g0".equals(menuOrderVO.getMenu_od_status()))
                idMenu_Order_Layout.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }

        @Override
        public int getItemCount() {
            return menuOrderVOList.size();
        }


    }

    private void MenuOrderDisplay() {

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


}

