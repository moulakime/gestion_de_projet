package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;

public class OrderVendorFragment extends BaseFragment implements OnBackPressed {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.orders_vendor_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("mArguments", getArguments() + " OrderVendorFragment");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // TODO: 05/07/2019
    }

}
