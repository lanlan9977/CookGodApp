package com.cookgod.foodsup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookgod.R;

public class ChefFoodFragment extends DialogFragment {


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cheffood, container, false);
        return view;
    }
}
