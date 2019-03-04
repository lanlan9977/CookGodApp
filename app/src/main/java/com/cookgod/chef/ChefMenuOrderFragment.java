package com.cookgod.chef;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.order.MenuOrderVO;

import java.util.List;

public class ChefMenuOrderFragment extends Fragment {
    private List<MenuOrderVO> menuOrderList;

    public void onAttach(Context context) {//從OrderActivity取得物件資料
        super.onAttach(context);
        menuOrderList = ((ChefZoneActivity) context).getMenuOrderList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chefmenuorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idChefMenuOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//設定recyclerView
        recyclerView.setAdapter(new ChefMenuOrderFragment.ChefMenuOrderAdapter(inflater));
        return view;
    }

    private class ChefMenuOrderAdapter extends RecyclerView.Adapter<ChefMenuOrderAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        public ChefMenuOrderAdapter(LayoutInflater inflater) {
            this.layoutInflater = inflater;

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView idChefMenuOrder_Name,idChefMenuOrder_Start;


            public ViewHolder(View itemView) {
                super(itemView);
                idChefMenuOrder_Name=itemView.findViewById(R.id.idChefMenuOrder_Name);
                idChefMenuOrder_Start=itemView.findViewById(R.id.idChefMenuOrder_Start);

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_chefmenuorder, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return menuOrderList.size();
        }
    }
}
