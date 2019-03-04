package com.cookgod.chef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.order.FestOrderVO;

import java.util.List;

public class ChefFestOrderFragment extends Fragment {
    private List<FestOrderVO> festOrderList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheffestorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idChefFestOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//設定recyclerView
        recyclerView.setAdapter(new ChefFestOrderFragment.ChefFestOrderAdapter(inflater));
        return view;
    }

    private class ChefFestOrderAdapter extends RecyclerView.Adapter<ChefFestOrderAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        public ChefFestOrderAdapter(LayoutInflater inflater) {
            this.layoutInflater = inflater;

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView idChefFestOrder_Name,idChefFestOrder_Start;


            public ViewHolder(View itemView) {
                super(itemView);
                idChefFestOrder_Name=itemView.findViewById(R.id.idChefFestOrder_Name);
                idChefFestOrder_Start=itemView.findViewById(R.id.idChefFestOrder_Start);

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_cheffestorder, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
