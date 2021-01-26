package com.citysystem.cmdapplicationdemo.ui_controller.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;


public class PartnerListFragment extends BaseFragment implements OnBackPressed {


    Toolbar toolbar;
    PartnerViewModel partnerViewModel;
    FloatingActionButton btnAddPartner;
    RecyclerView recyclerView;
    PartnerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();

        }
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_partners_fragment, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.partners_list_qr_code) {

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    new ScanListParentFragment()).commit();
            return true;
        } else if (item.getItemId() == R.id.import_partners) {
            importPartnersFromCsvFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.partners_list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.partners_list_action_search);
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
    public void onBackPressed() {
        super.onBackPressed();

        // TODO: 05/07/2019
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("mArguments", getArguments() + " PartnerListFragment");
        btnAddPartner = view.findViewById(R.id.open_add_partner_fragment);
        recyclerView = view.findViewById(R.id.partners_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PartnerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PartnerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Partner selectedPartner) {
                InfoPartnerFragment infoPartner = InfoPartnerFragment.infoPartnerFragmentInstance(selectedPartner);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        infoPartner).commit();
            }
        });

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


        btnAddPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().
                        beginTransaction().replace(R.id.container, new AddPartnerFragment()).commit();
            }
        });

        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.partners);
    }

    private void importPartnersFromCsvFile() {

        InputStream is = getResources().openRawResource(R.raw.listclients);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] partner = line.split(";");
                partnerViewModel.insertPartnerViewModel(new Partner(partner[1], partner[2], partner[3], partner[4], partner[5], Double.valueOf(partner[6]),
                        Double.valueOf(partner[7]), partner[8]));
            }
            buildAlertMessageSuccess();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildAlertMessageSuccess() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Success")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
