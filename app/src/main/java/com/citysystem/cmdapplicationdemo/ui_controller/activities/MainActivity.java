package com.citysystem.cmdapplicationdemo.ui_controller.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.ui_controller.fragments.BaseFragment;
import com.citysystem.cmdapplicationdemo.ui_controller.fragments.InvoicePartnerListFragment;
import com.citysystem.cmdapplicationdemo.ui_controller.fragments.OrderVendorFragment;
import com.citysystem.cmdapplicationdemo.ui_controller.fragments.PartnerListFragment;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;
import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.ERROR_DIALOG_REQUEST;
import static com.citysystem.cmdapplicationdemo.utils.Constants.PERMISSIONS;
import static com.citysystem.cmdapplicationdemo.utils.Constants.PERMISSION_ALL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;

    public static boolean hasNoPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        isServicesOK();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new InvoicePartnerListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_invoice);
        }


    }

    public void hideKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (im.isAcceptingText() && v != null) {
            v.clearFocus();
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Toast.makeText(getApplicationContext(), " open closed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_invoice:
                drawer.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new InvoicePartnerListFragment()).commit();
                break;
            case R.id.nav_credit_notes:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new OrderVendorFragment()).commit();
                break;
            case R.id.nav_catalogue:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage_clients:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new PartnerListFragment()).commit();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        tellFragments();
    }

    private void tellFragments() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof BaseFragment)
                ((BaseFragment) f).onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasNoPermission(this, PERMISSIONS))
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
    }

    private void requestPermissions() {

        if (hasNoPermission(this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                    return;
                }
            }
        }
    }

    private void isServicesOK() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Toast.makeText(getApplicationContext(), "an error, but we can fix it", Toast.LENGTH_LONG).show();
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
    }


}
