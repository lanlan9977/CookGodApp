package com.cookgod.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.*;

import com.cookgod.R;

public class MenuOrderFragment extends Fragment {

    private List<MenuOrderVO> menuOrderVOList;
    private LinearLayout idMenu_Order_Layout;


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
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.card_menuorder, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            idMenu_Order_Layout= itemView.findViewById(R.id.idMenu_Order_Layout);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            MenuOrderVO menuOrderVO = menuOrderVOList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH : mm ");
            viewHolder.idMenu_or_id.setText("訂單編號："+ menuOrderVO.getMenu_od_ID());
            viewHolder.idMenu_or_appt.setText("預約日期："+sdf.format(menuOrderVO.getMenu_od_book()));
            if("g0".equals(menuOrderVO.getMenu_od_status()))
            idMenu_Order_Layout.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }
        @Override
        public int getItemCount() {
            return menuOrderVOList.size();
        }


    }


}

