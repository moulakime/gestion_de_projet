package com.citysystem.cmdapplicationdemo.ui_controller.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.viewmodels.InvoiceViewModel;

import java.util.List;
import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.INVOICE_VALIDATE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.VENTE;


public class InvoiceValidateDialog extends AppCompatDialogFragment {


    RadioGroup radioGroup;
    RadioButton defaultCheckedRadioButton;
    Button positiveBtn;
    Button negativeBtn;
    TextView txtMt;
    Invoice invoice;
    InvoiceViewModel invoiceViewModel;
    List<InvoiceLine> invoiceLinesByInvoiceId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("mArguments", getArguments() + " InvoiceValidateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.invoice_validate_dialog, null);
        findViews(view);
        fillViews();
        getTotalAmount();
        builder.setView(view);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndUpdateInvoice();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new InvoicePartnerListFragment()).commit();
                getDialog().dismiss();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for (int i = 0; i < group.getChildCount(); i++) {

                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    childAt.setTextColor(Objects.requireNonNull(getActivity()).getResources()
                            .getColor(R.color.colorBlack, getActivity().getTheme()));
                    childAt.setTypeface(null, Typeface.NORMAL);
                    childAt.setTextSize(18f);
                    childAt.setBackgroundColor(Color.parseColor("#E9E9E9"));

                }
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                checkedRadioButton.setBackgroundColor(Color.parseColor("#969696"));
                checkedRadioButton.setTypeface(null, Typeface.BOLD);
                checkedRadioButton.setTextSize(20f);

            }
        });


        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    private void findViews(@NonNull View v) {
        radioGroup = v.findViewById(R.id.radio_group_validate_invoice_dialog);
        negativeBtn = v.findViewById(R.id.btn_cancel_invoice_validate_dialog);
        positiveBtn = v.findViewById(R.id.btn_validate_invoice_validate_dialog);
        txtMt = v.findViewById(R.id.txt_mt_invoice_validate_dialog);
        defaultCheckedRadioButton = v.findViewById(R.id.sale_radio_validate_invoice_dialog);
    }

    private void fillViews() {

        defaultCheckedRadioButton.setChecked(true);
        defaultCheckedRadioButton.setBackgroundColor(Color.parseColor("#969696"));
        defaultCheckedRadioButton.setTypeface(null, Typeface.BOLD);
        defaultCheckedRadioButton.setTextSize(20f);
    }


    private void getAndUpdateInvoice() {

        if (getArguments() != null) {
            getArguments().clear();
            invoiceViewModel.updateInvoice(invoice);
            invoiceViewModel.generateCreditNoteByInvoice(invoice);
        }

    }

    private void getTotalAmount() {

        double TotalAmount = 0;
        double mt;

        if (getArguments() == null) {
            Toast.makeText(getContext(), "getArguments" + getArguments(), Toast.LENGTH_SHORT).show();
            return;
        }
        long idInvoice = getArguments().getLong(INVOICE_VALIDATE);
        invoiceViewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);
        invoice = invoiceViewModel.getInvoiceById(idInvoice);
        invoiceLinesByInvoiceId = invoiceViewModel.getInvoiceLinesByInvoiceIdVM(idInvoice);

        if (invoiceLinesByInvoiceId.size() == 0) {
            Toast.makeText(getContext(), "List Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        for (InvoiceLine invoiceLine : invoiceLinesByInvoiceId) {
            if (invoiceLine.getType().equals(VENTE)) {
                mt = invoiceLine.getPriceUnit() * invoiceLine.getQuantity();
                TotalAmount = TotalAmount + mt;
            }
        }
        txtMt.setText(String.valueOf(TotalAmount));
    }


}
