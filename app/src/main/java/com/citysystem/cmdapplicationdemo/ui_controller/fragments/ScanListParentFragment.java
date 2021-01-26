package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;
import com.citysystem.cmdapplicationdemo.viewmodels.PartnerViewModel;
import com.google.zxing.Result;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanListParentFragment extends BaseFragment implements ZXingScannerView.ResultHandler, OnBackPressed {


    private ZXingScannerView mScannerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
        mScannerView = new ZXingScannerView(getActivity());
        Log.i("mArguments", getArguments() + " ScanListParentFragment");
        return mScannerView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new PartnerListFragment()).commit();
    }

    @Override
    public void handleResult(Result rawResult) {

        mScannerView.resumeCameraPreview(this);

        if (rawResult != null) {

            PartnerViewModel partnerViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PartnerViewModel.class);
            final Partner partnerViaScanner = partnerViewModel.getPartnerViaScanner(rawResult.getText());

            InfoPartnerFragment infoPartner = InfoPartnerFragment.infoPartnerFragmentInstance(partnerViaScanner);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    infoPartner).commit();

        }

    }

}
