package com.cookgod.chef;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookgod.R;

public class ChefOrderPayFragment extends Fragment {
    private final static String TAG = "ChefOrderDetailActivity";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "GGGGGGGGGGGG");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheforderpay, container, false);
        Log.e(TAG, "FFFFF");
        return view;
    }
}
