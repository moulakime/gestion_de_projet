package com.citysystem.cmdapplicationdemo.ui_controller.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;

import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.LINE;


public class InvoiceLineDialog extends AppCompatDialogFragment {


    InvoiceLine line;
    Button positiveBtn;
    Button negativeBtn;
    EditText edQte;
    TextView txtProductName, txtMt;
    onDialogClickListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("mArguments", getArguments() + " InvoiceLineDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.invoice_line_dialog, null);
        findViews(view);
        fillViews();
        builder.setView(view);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String trimQte = edQte.getText().toString().trim();
                if (trimQte.isEmpty()) {
                    Toast.makeText(getContext(), " isEmpty", Toast.LENGTH_SHORT).show();
                    return;
                }
                int qte = Integer.valueOf(trimQte);
                line.setQuantity(qte);
                listener.onClick(line);
                getDialog().dismiss();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        edQte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    private void fillViews() {
        if (getArguments() != null) {
            line = getArguments().getParcelable(LINE);
        }
    }

    private void findViews(@NonNull View v) {
        edQte = v.findViewById(R.id.ed_quantity_invoice_dialog_line);
        positiveBtn = v.findViewById(R.id.btn_validate_invoice_dialog_line);
        negativeBtn = v.findViewById(R.id.btn_cancel_invoice_dialog_line);
        txtProductName = v.findViewById(R.id.txt_product_invoice_dialog_line);
        txtMt = v.findViewById(R.id.txt_mt_invoice_dialog_line);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (onDialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            Toast.makeText(context, context.toString() +
                    "must implement onDialogClickListener", Toast.LENGTH_LONG).show();
        }
    }

    public interface onDialogClickListener {
        void onClick(InvoiceLine invoiceLine);

    }

}
