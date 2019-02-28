package com.cookgod.order;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cookgod.R;
import com.cookgod.order.FestOrderVO;

public class FestOrderFragment extends Fragment {
    private List<FestOrderVO> festOrderVOList;
    private LinearLayout idFest_Order_Layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView idFest_Order_msg;
    private Boolean isOnClick = true;

    @Override
    public void onAttach(Context context) {//從OrderActivity取得物件資料
        super.onAttach(context);
        festOrderVOList = ((OrderActivity) context).getFestOrderList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.festOrderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//設定recyclerView
        recyclerView.setAdapter(new FestOrderAdapter(inflater));
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);//設定bottomSheetBehavior
        idFest_Order_msg = view.findViewById(R.id.idFest_Order_msg);//設定bottomSheetBehavior中的TextView(顯示訂單內容)
        return view;
    }

    private class FestOrderAdapter extends RecyclerView.Adapter<FestOrderAdapter.ViewHolder> {//CardView顯示訂單Id與日期
        private LayoutInflater inflater;

        public FestOrderAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView idFest_or_id, idFest_or_appt;

            public ViewHolder(View itemView) {
                super(itemView);
                idFest_or_id = itemView.findViewById(R.id.idFest_or_id);
                idFest_or_appt = itemView.findViewById(R.id.idFest_or_appt);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.card_festorder, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            idFest_Order_Layout = itemView.findViewById(R.id.idFest_Order_Layout);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final FestOrderVO festOrderVO = festOrderVOList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH : mm ");
            viewHolder.idFest_or_id.setText("訂單編號：" + festOrderVO.getFest_or_ID());
//            viewHolder.idFest_or_appt.setText("預約日期：" + sdf.format(festOrderVO.getFest_or_start()));
            if ("o0".equals(festOrderVO.getFest_or_status())) {
                idFest_Order_Layout.setBackgroundColor(getResources().getColor(R.color.colorRed));
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnClick) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setPeekHeight(985);
                        displayFestOrder(position);
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
            return festOrderVOList.size();
        }
    }

    private void displayFestOrder(int position) {
//        FestOrderVO festOrder = festOrderVOList.get(position);
//        String status = festOrder.getFest_od_status();
//        if ("g1".equals(status)) {
//            status = "審核通過";
//        } else if ("g0".equals(status)) {
//            status = "審核未通過";
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("嚴選套餐訂單編號:" + festOrder.getFest_od_ID() + "\r\n")
//                .append("訂單狀態:" + status + "\r\n")
//                .append("下單日期:" + festOrder.getFest_od_start() + "\r\n")
//                .append("預約日期:" + festOrder.getFest_od_book() + "\r\n")
//                .append("完成日期:" + festOrder.getFest_od_end() + "\r\n")
//                .append("訂單評價:" + festOrder.getFest_od_rate() + "\r\n")
//                .append("訂單評價留言:" + festOrder.getFest_od_msg() + "\r\n")
//                .append("顧客編號:" + festOrder.getCust_ID() + "\r\n")
//                .append("主廚編號:" + festOrder.getChef_ID() + "\r\n")
//                .append("套餐編號:" + festOrder.getFest_ID() + "\r\n");
//        idFest_Order_msg.setText(sb);
    }
}
