package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import static com.citysystem.cmdapplicationdemo.utils.Constants.PARTNER;
import static com.citysystem.cmdapplicationdemo.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;


public class InfoPartnerFragment extends BaseFragment implements OnBackPressed {
    Partner partner;
    PartnerViewModel partnerViewModel;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    EditText edName, edTel, edEmail, edPatent, edIce;
    String name;
    String tel;
    String email;
    String patent;
    String ice;
    ImageButton btnEditPartner;
    MenuItem editMenuItem;
    MenuItem updateMenuItem;
    LocationRequest locationRequest;
    private double mLatitude;
    private double mLongitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    public static InfoPartnerFragment infoPartnerFragmentInstance(Partner partner) {

        InfoPartnerFragment infoPartner = new InfoPartnerFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARTNER, partner);
        infoPartner.setArguments(args);
        return infoPartner;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.info_partner_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("mArguments", getArguments() + " InfoPartnerFragment");

        mFusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        findViews(view);
        createLocationCallback();
        createLocationRequest();
        fillViews();
        disableOrEnableView(true);
        btnEditPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edName.getText().toString().trim();
                tel = edTel.getText().toString().trim();
                email = edEmail.getText().toString().trim();
                patent = edPatent.getText().toString().trim();
                ice = edIce.getText().toString().trim();

                if (name.isEmpty() || tel.isEmpty() || email.isEmpty() || patent.isEmpty() || ice.isEmpty()) {
                    Toast.makeText(getContext(), "field empty", Toast.LENGTH_LONG).show();
                } else {
                    partner.setPartnerName(name);
                    partner.setPhone(tel);
                    partner.setEmail(email);
                    partner.setPatent(patent);
                    partner.setIce(ice);
                    updateMenuItem.setVisible(false);
                    editMenuItem.setVisible(true);
                    disableOrEnableView(true);
                    partnerViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PartnerViewModel.class);
                    partnerViewModel.updatePartner(partner);
                    Toast.makeText(getContext(), "Partner Updated", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isGpsEnabled();

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
        mLatitude = 0.0;
        mLongitude = 0.0;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationRequest();
    }

    private void stopLocationRequest() {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            relativeLayout.setAlpha(1f);
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
        isGpsEnabled();
        relativeLayout.setAlpha(0.1f);
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
                if (mLongitude != 0.0 && mLatitude != 0.0) {
                    partner.setLatitude(mLatitude);
                    partner.setLongitude(mLongitude);
                    Toast.makeText(getContext(), mLatitude + "" + mLongitude, Toast.LENGTH_SHORT).show();
                    stopLocationRequest();
                }

            }
        };
    }

    private void fillViews() {

        if (getArguments() != null) {

            partner = getArguments().getParcelable(PARTNER);
            if (partner == null)
                return;
//            CharSequence format = DateFormat.format("yyyy|MM|dd hh:mm:ss", partner.getDateCreation());
//            Log.i("MY_PARTNER_DATE", String.valueOf(format));
            edName.setText(partner.getPartnerName());
            edTel.setText(partner.getPhone());
            edEmail.setText(partner.getEmail());
            edPatent.setText(partner.getPatent());
            edIce.setText(partner.getIce());
        }
    }

    private void findViews(View v) {
        locationRequest = new LocationRequest();
        relativeLayout = v.findViewById(R.id.info_partner_relative_layout);
        progressBar = v.findViewById(R.id.progress_circular_info_partner);
        edName = v.findViewById(R.id.ed_nom_partner);
        edTel = v.findViewById(R.id.ed_tel_partner);
        edEmail = v.findViewById(R.id.ed_email_partner);
        edPatent = v.findViewById(R.id.ed_patent_partner);
        edIce = v.findViewById(R.id.ed_ice_partner);
        btnEditPartner = v.findViewById(R.id.btn_save_partner);
    }

    public void disableOrEnableView(boolean isVisible) {

        if (isVisible) {
            edName.setEnabled(false);
            edTel.setEnabled(false);
            edEmail.setEnabled(false);
            edPatent.setEnabled(false);
            edIce.setEnabled(false);
            btnEditPartner.setVisibility(View.INVISIBLE);
        } else {
            edName.setEnabled(true);
            edTel.setEnabled(true);
            edEmail.setEnabled(true);
            edPatent.setEnabled(true);
            edIce.setEnabled(true);
            btnEditPartner.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.info_partner_menu, menu);
        menu.getItem(1).setVisible(false);
        updateMenuItem = menu.getItem(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_partner) {
            disableOrEnableView(false);
            item.setVisible(false);
            editMenuItem = item;
            updateMenuItem.setVisible(true);
            return true;
        } else if (item.getItemId() == R.id.update_location) {

            requestLocationUpdates();
            relativeLayout.setAlpha(0.1f);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void isGpsEnabled() {

        final LocationManager manager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessage();
        }

    }

    private void buildAlertMessage() {
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
