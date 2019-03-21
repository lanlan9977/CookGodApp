package com.cookgod.broadcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cookgod.R;
import com.cookgod.main.MainActivity;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveBroadcastTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BroadcastFragment extends Fragment {
    private final static String TAG = "MainActivity";
    private RecyclerView broadcastView, broadcastReadView;
    private List<BroadcastVO> broadcastList,broadcastNoReadList;
    private ScrollView idBroadcastScrollView;
    private BroadcastListAdapter broadcasNoReadtListAdapter;
    private BroadcastListAdapter broadcastListAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        broadcastList = ((MainActivity) context).getBroadcastList();
        if (broadcastList != null) {
            isRead(broadcastList);
        }
    }

    private void isRead(List<BroadcastVO> broadcastList) {
        this.broadcastList=broadcastList;
        this.broadcastNoReadList = new ArrayList<>();
        for (int i = 0; i < this.broadcastList.size(); i++) {
            if ("B0".equals(this.broadcastList.get(i).getBroadcast_status())) {
                broadcastNoReadList.add(this.broadcastList.get(i));
                this.broadcastList.remove(i);
                i--;
            }
        }
    }
//
    public void onRead(List<BroadcastVO> broadcastList){
        isRead(broadcastList);
        Log.e(TAG,"HHHHHHHHHHHHHHHH");
        broadcastListAdapter.notifyAll();
        broadcasNoReadtListAdapter.notifyAll();
    }
    public  String getFriendlytime(long d){
        long delta = (System.currentTimeMillis()-d)/1000;
//        if(delta<=0)return d.toLocaleString();
        if(delta/(60*60*24*365) > 0) return delta/(60*60*24*365) +"年前";
        if(delta/(60*60*24*30) > 0) return delta/(60*60*24*30) +"個月前";
        if(delta/(60*60*24*7) > 0)return delta/(60*60*24*7) +"週前";
        if(delta/(60*60*24) > 0) return delta/(60*60*24) +"天前";
        if(delta/(60*60) > 0)return delta/(60*60) +"小時前";
        if(delta/(60) > 0)return delta/(60) +"分鍾前";
        return "剛剛";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_broadcast, container, false);
        broadcasNoReadtListAdapter = new BroadcastListAdapter(getActivity(), broadcastNoReadList);
        broadcastListAdapter = new BroadcastListAdapter(getActivity(), broadcastList);
        idBroadcastScrollView = view.findViewById(R.id.idBroadcastScrollView);
        idBroadcastScrollView.setFillViewport(true);
        broadcastView = view.findViewById(R.id.idBroadcastView);
        broadcastView.setLayoutManager(new LinearLayoutManager(getActivity()));
        broadcastView.setNestedScrollingEnabled(false);


        broadcastReadView = view.findViewById(R.id.idBroadcastReadView);
        broadcastReadView.setLayoutManager(new LinearLayoutManager(getActivity()));
        broadcastReadView.setNestedScrollingEnabled(false);
        if (broadcastList != null) {
            broadcastReadView.setAdapter(broadcastListAdapter);
            broadcastView.setAdapter(broadcasNoReadtListAdapter);
        }
        idBroadcastScrollView.post(new Runnable() {
            @Override
            public void run() {
                idBroadcastScrollView.fullScroll(view.FOCUS_DOWN);
            }
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(broadcasNoReadtListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(broadcastView);
        return view;
    }

    private class BroadcastListAdapter extends RecyclerView.Adapter<BroadcastListAdapter.ViewHolder>  implements ItemTouchHelperAdapter {
        private Context context;
        private List<BroadcastVO> broadcastList1;
        private LayoutInflater layoutInflater;


        public BroadcastListAdapter(Context context, List<BroadcastVO> broadcastList1) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.broadcastList1 = broadcastList1;

        }

        class ViewHolder extends RecyclerView.ViewHolder  implements ItemTouchHelperViewHolder {
            TextView idBoradcast_con, idBoradcast_start;
            LinearLayout idBoradcast_Layout;
            public float defaultZ;


            @SuppressLint("NewApi")
            public ViewHolder(View view) {
                super(view);
                idBoradcast_con = view.findViewById(R.id.idBoradcast_con);
                idBoradcast_start = view.findViewById(R.id.idBoradcast_start);
                idBoradcast_Layout = view.findViewById(R.id.idBoradcast_Layout);
                defaultZ = itemView.getTranslationZ();
            }
            @SuppressLint("NewApi")
            @Override
            public void onItemSelected() {
                itemView.setTranslationZ(15.0f);
            }
            @SuppressLint("NewApi")
            @Override
            public void onItemClear() {
                itemView.setTranslationZ(defaultZ);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_broadcast, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final BroadcastVO broadcastVO = broadcastList1.get(i);
            viewHolder.idBoradcast_con.setText(broadcastVO.getBroadcast_con());
//            viewHolder.idBoradcast_start.setText(new DateFormatBack().format(broadcastVO.getBroadcast_start().toString()));
            viewHolder.idBoradcast_start.setText(getFriendlytime(broadcastVO.getBroadcast_start().getTime()));
            Log.e(TAG,""+Calendar.getInstance());
            viewHolder.idBoradcast_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        new RetrieveBroadcastTask(Util.Servlet_URL + "BroadcastServlet", broadcastVO.getBroadcast_ID()).execute();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    Intent intent = new Intent(getContext(), BroadcastDetailActivity.class);
                    intent.putExtra("msg", broadcastVO.getBroadcast_con());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return broadcastList1.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
//            // 將集合裡的資料進行交換
//            Collections.swap(broadcastList1, fromPosition, toPosition);
//            notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onItemDismiss(int i) {
            final BroadcastVO broadcastVO = broadcastList1.get(i);
            try {
                new RetrieveBroadcastTask(Util.Servlet_URL + "BroadcastServlet", broadcastVO.getBroadcast_ID()).execute();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            broadcastList1.remove(i);
            broadcastList.add(broadcastVO);
            ((MainActivity)getActivity()).onProviderCount(broadcastList1);
            notifyItemRemoved(i);
        }



    }
    private class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
        private ItemTouchHelperAdapter adapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            this.adapter = adapter;
        }

        // 預設即為true (寫出來提醒一下)
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        // 預設即為true (寫出來提醒一下)
        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            // 也可以設ItemTouchHelper.RIGHT(向右滑)，或是 ItemTouchHelper.START | ItemTouchHelper.END (左右滑都可以)
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float width = (float) viewHolder.itemView.getWidth();
                float alpha = 1.0f - Math.abs(dX) / width;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            // We only want the active item
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemViewHolder =
                            (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemClear();
                viewHolder.itemView.setTranslationX(0);
            }
        }
    }

}
