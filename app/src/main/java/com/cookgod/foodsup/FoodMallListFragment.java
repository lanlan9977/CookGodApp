package com.cookgod.foodsup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.chef.ChefOdDetailVO;
import com.cookgod.main.Util;
import com.cookgod.task.FoodMallImageTask;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FoodMallListFragment extends Fragment {
    private final static String TAG = "FoodMallActivity";
    private Dialog foodDialog;
    private RecyclerView idChefOrederRecyclerView;
    private FoodMallImageTask foodMallImageTask;
    private List<FavFdSupVO> favSupList;
    private List<FoodMallVO> foodMallList;
    private List<FoodSupVO> foodSupList;
    private List<FoodVO> foodList;
    private Map<FoodMallVO, ChefOdDetailVO> foodMallMap;
    private FoodMallListAdapter foodMallListAdapter;

    public FoodMallListFragment() {
    }

    @SuppressLint("ValidFragment")
    public FoodMallListFragment(List<FoodMallVO> foodMallList) {
        this.foodMallList = foodMallList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        favSupList = ((FoodMallActivity) context).getFavSupList();
//        foodMallList=((FoodMallActivity)context).getFoodMallList();
        foodSupList = ((FoodMallActivity) context).getFoodSupList();
        foodList = ((FoodMallActivity) context).getFoodList();
        foodMallMap = ((FoodMallActivity) context).getFoodMallMap();
    }

    public void reMapData() {
        foodMallMap = new LinkedHashMap<>();
        if (foodMallListAdapter != null) {
            foodMallListAdapter.reData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodmall, container, false);
        idChefOrederRecyclerView = view.findViewById(R.id.idChefOrederRecyclerView);
        idChefOrederRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        idChefOrederRecyclerView.setAdapter(foodMallListAdapter = new FoodMallListAdapter(inflater, foodMallList));
        return view;
    }

    private class FoodMallListAdapter extends RecyclerView.Adapter<FoodMallListAdapter.ViewHolder> {
        private List<FoodMallVO> foodMallList;
        private LayoutInflater inflater;

        public FoodMallListAdapter(LayoutInflater inflater, List<FoodMallVO> foodMallList) {
            this.inflater = inflater;
            this.foodMallList = foodMallList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idFood_M_Name, idFood_M_Place, idFood_M_Price, idFood_M_Unit, idCheckQua;
            ImageView idFood_M_Pic, idFav_Sup;
            RatingBar idFood_M_Rate;
            Spinner idFood_Mall_Qty;

            public ViewHolder(View itemView) {
                super(itemView);
                idFood_M_Name = itemView.findViewById(R.id.idFood_M_Name);
                idFood_M_Place = itemView.findViewById(R.id.idFood_M_Place);
                idFood_M_Price = itemView.findViewById(R.id.idFood_M_Price);
                idFood_M_Pic = itemView.findViewById(R.id.idFood_M_Pic);
                idFood_M_Rate = itemView.findViewById(R.id.idFood_M_Rate);
                idFood_M_Unit = itemView.findViewById(R.id.idFood_M_Unit);
                idFav_Sup = itemView.findViewById(R.id.idFav_Sup);
                idCheckQua = itemView.findViewById(R.id.idCheckQua);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = inflater.inflate(R.layout.card_cheforder, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            final FoodMallVO foodMallVO = foodMallList.get(i);
            foodMallImageTask = new FoodMallImageTask(Util.Servlet_URL + "FoodMallServlet", foodMallVO.getFood_ID(), imageSize, viewHolder.idFood_M_Pic);
            foodMallImageTask.execute();
            viewHolder.idFood_M_Name.setText(foodMallVO.getFood_m_name());
            viewHolder.idFood_M_Place.setText(foodMallVO.getFood_m_place());
            viewHolder.idFood_M_Price.setText("$" + foodMallVO.getFood_m_price().toString());
            viewHolder.idFood_M_Rate.setRating(foodMallVO.getFood_m_rate());
            viewHolder.idFood_M_Unit.setText("單位：" + foodMallVO.getFood_m_unit());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodDialog = new Dialog(getContext());
                    foodDialog.setTitle("確認訂單食材");
                    foodDialog.setCancelable(true);
                    foodDialog.setContentView(R.layout.dialog_fooddetail);
                    final Window dialogWindow = foodDialog.getWindow();
                    dialogWindow.setGravity(Gravity.CENTER);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = 1000;
                    lp.alpha = 1.0f;
                    dialogWindow.setAttributes(lp);
                    TextView idfood_M_Resume = foodDialog.findViewById(R.id.idfood_M_Resume);
                    Button btnfood_M_Resume_Back = foodDialog.findViewById(R.id.btnfood_M_Resume_Back);
                    btnfood_M_Resume_Back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            foodDialog.cancel();
                        }
                    });
                    idfood_M_Resume.setText(foodMallVO.getFood_m_resume());
                    foodDialog.show();
                }
            });
            for (int j = 0; j < favSupList.size(); j++) {
                FavFdSupVO favFdSupVO = favSupList.get(j);
                if (favFdSupVO.getFood_sup_ID().equals(foodMallVO.getFood_sup_ID())) {
                    viewHolder.idFav_Sup.setVisibility(View.VISIBLE);
                    break;
                }
            }
            viewHolder.idFood_Mall_Qty = viewHolder.itemView.findViewById(R.id.idFood_Mall_Qty);
            ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(getContext(),
                    R.array.stringFoodMallArray,
                    android.R.layout.simple_spinner_dropdown_item);
            viewHolder.idFood_Mall_Qty.setAdapter(lunchList);
            if (foodMallMap.get(foodMallVO) != null && foodMallMap.size() > 0) {

                viewHolder.idFood_Mall_Qty.setSelection(foodMallMap.get(foodMallVO).getChef_od_qty());
            }
            viewHolder.idFood_Mall_Qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Integer item = Integer.parseInt(parent.getSelectedItem().toString());
                    ChefOdDetailVO chefOdDetailVO = new ChefOdDetailVO();
                    chefOdDetailVO.setChef_od_qty(item);
                    if (position > 0) {
                        foodMallMap.put(foodMallVO, chefOdDetailVO);
                        viewHolder.idCheckQua.setVisibility(View.VISIBLE);

                    } else {
                        viewHolder.idCheckQua.setVisibility(View.GONE);
                        foodMallMap.remove(foodMallVO);
                    }
                    Log.e(TAG,""+foodMallMap.size());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return foodMallList.size();
        }

        public void reData() {

            notifyDataSetChanged();
        }
    }
}