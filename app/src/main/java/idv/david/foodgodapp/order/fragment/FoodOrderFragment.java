package idv.david.foodgodapp.order.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.david.foodgodapp.MenuOrder;
import idv.david.foodgodapp.R;

public class FoodOrderFragment extends Fragment {
    private List<MenuOrder> MenuOrderList;

    public FoodOrderFragment() {
//        利用建構子帶入MENU_ORDER VO
        MenuOrderList = new ArrayList<>();
        MenuOrder menuOrder = new MenuOrder("M02091",0,new Date(),new Date(),new Date(),0,"","C00001","C00001","M00001");

        MenuOrderList.add(menuOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_foodorder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.foodOrderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new FoodOrderAdapter(inflater));

        return view;
    }

    private class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.ViewHolder> {
        private LayoutInflater inflater;

        public FoodOrderAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idMenu_or_id, idMenu_or_appt;

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
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final MenuOrder MenuOrder = MenuOrderList.get(position);
            viewHolder.idMenu_or_id.setText(MenuOrder.getMenu_or_id());
            viewHolder.idMenu_or_appt.setText((MenuOrder.getMenu_or_appt()).toString());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return MenuOrderList.size();
        }

    }


}
