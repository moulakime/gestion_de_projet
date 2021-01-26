package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.adapters.PartnerAdapter;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.utils.OnBackPressed;
import com.citysystem.cmdapplicationdemo.viewmodels.PartnerViewModel;

import java.util.List;
import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.DEFAULT_INVOICE_ID;


public class InvoicePartnerListFragment extends BaseFragment implements OnBackPressed {

    PartnerViewModel partnerViewModel;
    RecyclerView recyclerView;
    PartnerAdapter adapter;
    Toolbar toolbar;
    DrawerLayout drawerLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.invoice_list_partners_fragment, container, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // TODO: 05/07/2019
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.invoice_qr_code) {

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new ScanInvoiceParentListFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.invoice_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.invoice_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("mArguments", getArguments() + " InvoicePartnerListFragment");
        recyclerView = view.findViewById(R.id.invoice_partners_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PartnerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PartnerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Partner selectedPartner) {

                ProductsFragment productsFragment = ProductsFragment.ProductsFragmentInstance(selectedPartner, false, DEFAULT_INVOICE_ID);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(
                        ((ViewGroup) Objects.requireNonNull(getView()).getParent()).getId(), productsFragment).commit();
            }
        });


        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        drawerLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setTitle(R.string.choose_partner);
        Log.i("MyOnViewCreated", "InvoicePartnerListFragment");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        partnerViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PartnerViewModel.class);
        partnerViewModel.getAllPartners().observe(getViewLifecycleOwner(), new Observer<List<Partner>>() {
            @Override
            public void onChanged(@Nullable List<Partner> partners) {
                adapter.setPartnersList(partners);
            }
        });
    }


}
