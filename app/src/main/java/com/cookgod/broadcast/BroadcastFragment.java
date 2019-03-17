package com.cookgod.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cookgod.other.DateFormatBack;
import com.cookgod.task.RetrieveBroadcastTask;

import java.util.ArrayList;
import java.util.List;


public class BroadcastFragment extends Fragment {
    private final static String TAG = "MainActivity";
    private RecyclerView broadcastView, broadcastReadView;
    private List<BroadcastVO> broadcastList;
    private List<BroadcastVO> broadcastNoReadList;
    private ScrollView idBroadcastScrollView;
    private BroadcastListAdapter broadcasNoReadtListAdapter;
    private BroadcastListAdapter broadcastListAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        broadcastList = ((MainActivity) context).getBroadcastList();
        if (broadcastList != null) {
            isRead();
        }
    }

    private void isRead() {
        broadcastNoReadList = new ArrayList<>();
        for (int i = 0; i < broadcastList.size(); i++) {
            if ("B0".equals(broadcastList.get(i).getBroadcast_status())) {
                broadcastNoReadList.add(broadcastList.get(i));
                broadcastList.remove(i);
                i--;
            }
        }
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

        return view;
    }

    private class BroadcastListAdapter extends RecyclerView.Adapter<BroadcastListAdapter.ViewHolder> {
        private Context context;
        private List<BroadcastVO> broadcastList;
        private LayoutInflater layoutInflater;


        public BroadcastListAdapter(Context context, List<BroadcastVO> broadcastList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.broadcastList = broadcastList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idBoradcast_con, idBoradcast_start;
            LinearLayout idBoradcast_Layout;

            public ViewHolder(View itemView) {
                super(itemView);
                idBoradcast_con = itemView.findViewById(R.id.idBoradcast_con);
                idBoradcast_start = itemView.findViewById(R.id.idBoradcast_start);
                idBoradcast_Layout = itemView.findViewById(R.id.idBoradcast_Layout);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.card_broadcast, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final BroadcastVO broadcastVO = broadcastList.get(i);
            viewHolder.idBoradcast_con.setText(broadcastVO.getBroadcast_con());
            viewHolder.idBoradcast_start.setText(new DateFormatBack().format(broadcastVO.getBroadcast_start().toString()));
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
            return broadcastList.size();
        }

    }

}
