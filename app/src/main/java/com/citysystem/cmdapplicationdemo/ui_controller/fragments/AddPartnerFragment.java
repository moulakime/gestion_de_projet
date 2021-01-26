package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;
import com.citysystem.cmdapplicationdemo.viewmodels.PartnerViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.Objects;

import static android.os.Looper.getMainLooper;
import static com.citysystem.cmdapplicationdemo.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class AddPartnerFragment extends BaseFragment implements OnBackPressed {

    DrawerLayout drawerLayout;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    PartnerViewModel partnerViewModel;
    EditText edName, edTel, edEmail, edPatent, edIce;
    ImageButton btnAddPartner;
    LocationRequest locationRequest;
    String nomClient;
    String telClient;
    String emailClient;
    String patClient;
    String ICEClient;
    private double mLatitude;
    private double mLongitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_partner_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Log.i("mArguments", getArguments() + " AddPartnerFragment");
        mFusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        findViews(view);
        createLocationCallback();
        createLocationRequest();
        btnAddPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                insertData();
            }
        });

        coordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        if (drawerLayout != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Objects.requireNonNull(toolbar.getNavigationIcon()).setAlpha(0);

            // TODO: 12/07/2019
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = getActivity().getCurrentFocus();
        if (im.isAcceptingText() && v != null) {
            v.clearFocus();
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Toast.makeText(getContext(), " open closed", Toast.LENGTH_LONG).show();
        }
    }

    private void findViews(View v) {
        drawerLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer_layout);
        coordinatorLayout = v.findViewById(R.id.coordinator_add_partner);
        progressBar = v.findViewById(R.id.progress_circular_add_partner);
        progressBar.setVisibility(View.GONE);
        edName = v.findViewById(R.id.ed_add_nom_partner);
        edTel = v.findViewById(R.id.ed_add_tel_partner);
        edEmail = v.findViewById(R.id.ed_add_email_partner);
        edPatent = v.findViewById(R.id.ed_add_patent_partner);
        edIce = v.findViewById(R.id.ed_add_ice_partner);
        btnAddPartner = v.findViewById(R.id.btn_add_partner);
        locationRequest = new LocationRequest();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(((ViewGroup)
                Objects.requireNonNull(getView()).getParent()).getId(), new PartnerListFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        isGpsEnabled();
        mLatitude = 0.0;
        mLongitude = 0.0;
        Log.i("MyLocation", String.valueOf(mLatitude));
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationRequest();
    }

    private void stopLocationRequest() {

        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            coordinatorLayout.setAlpha(1f);
            progressBar.setVisibility(View.GONE);
            mLatitude = 0.0;
            mLongitude = 0.0;

        }
    }

    private void requestLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, getMainLooper());
        coordinatorLayout.setAlpha(0.5f);
        progressBar.setVisibility(View.VISIBLE);
    }


    private void createLocationRequest() {

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLatitude = locationResult.getLastLocation().getLatitude();
                mLongitude = locationResult.getLastLocation().getLongitude();
                if (mLongitude != 0.0) {
                    Log.i("MyLocation", String.valueOf(mLatitude));
                    stopLocationRequest();
                }

            }
        };
    }

    private void insertData() {
        isGpsEnabled();
        requestLocationUpdates();
        partnerViewModel = ViewModelProviders.of(this).get(PartnerViewModel.class);

        nomClient = edName.getText().toString();
        telClient = edTel.getText().toString();
        emailClient = edEmail.getText().toString();
        patClient = edPatent.getText().toString();
        ICEClient = edIce.getText().toString();

        if (nomClient.trim().equals("") ||
                telClient.trim().equals("") || patClient.trim().equals("")
                || ICEClient.trim().equals("")) {
            Toast.makeText(getContext(), "field empty", Toast.LENGTH_LONG).show();
            return;
        }


        partnerViewModel.insertPartnerViewModel(new Partner(nomClient, telClient, emailClient, patClient, ICEClient, mLatitude, mLongitude,
                null));

        returnToListClient();
    }


    private void returnToListClient() {
        android.support.v7.app.AlertDialog.Builder mBuilder;
        android.support.v7.app.AlertDialog alert;
        mBuilder = new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mBuilder.setTitle("Info");
        mBuilder.setMessage("Client Added");
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new PartnerListFragment()).commit();
            }
        });
        mBuilder.setCancelable(false);
        alert = mBuilder.create();
        alert.show();

    }

    private void isGpsEnabled() {

        final LocationManager manager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
