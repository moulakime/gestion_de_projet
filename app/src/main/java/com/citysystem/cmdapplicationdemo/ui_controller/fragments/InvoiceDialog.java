package com.citysystem.cmdapplicationdemo.ui_controller.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Product;

import java.util.Objects;

import static com.citysystem.cmdapplicationdemo.utils.Constants.PRODUCT_DIALOG;


public class InvoiceDialog extends AppCompatDialogFragment {


    String totalAmount;
    Product product;
    RadioGroup radioGroup;
    RadioButton defaultCheckedRadioButton;
    Button positiveBtn;
    Button negativeBtn;
    EditText edQte;
    TextView txtProductName, txtMt;
    onDialogClickListener listener;
    String type;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("mArguments", getArguments() + " InvoiceDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.invoice_dialog, null);
        findViews(view);
        fillViews();
        builder.setView(view);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null || edQte.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), " is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == null) {
                    Toast.makeText(getContext(), "choose an operation", Toast.LENGTH_SHORT).show();
                    return;
                }

                int qte = Integer.valueOf(edQte.getText().toString().trim());
                double priceUnit = product.getPriceUnit();
                boolean isPromo = product.isPromo();
                listener.onClick(product.getId(), qte, type, priceUnit, isPromo);
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
                Log.i("MyTextWatcher", s.toString());
                if (product == null || s.toString().isEmpty()) {
                    txtMt.setText(R.string.default_price_unit);
                    return;
                }
                Log.i("MyTextWatcherAfter", s.toString());
                totalAmount = String.valueOf(product.getPriceUnit() * Double.valueOf(s.toString()));
                txtMt.setText(totalAmount);
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
                type = checkedRadioButton.getText().toString();
            }
        });


        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    private void fillViews() {

        defaultCheckedRadioButton.setChecked(true);
        defaultCheckedRadioButton.setBackgroundColor(Color.parseColor("#969696"));
        defaultCheckedRadioButton.setTypeface(null, Typeface.BOLD);
        defaultCheckedRadioButton.setTextSize(20f);
        type = defaultCheckedRadioButton.getText().toString();
        if (getArguments() != null) {
            product = getArguments().getParcelable(PRODUCT_DIALOG);
            if (product == null)
                return;
            txtProductName.setText(product.getName());
            txtMt.setText(R.string.default_price_unit);
        }
    }

    private void findViews(@NonNull View v) {
        radioGroup = v.findViewById(R.id.radio_group_invoice_dialog);
        edQte = v.findViewById(R.id.ed_quantity_invoice_dialog);
        positiveBtn = v.findViewById(R.id.btn_validate_invoice_dialog);
        negativeBtn = v.findViewById(R.id.btn_cancel_invoice_dialog);
        txtProductName = v.findViewById(R.id.txt_product_invoice_dialog);
        txtMt = v.findViewById(R.id.txt_mt_invoice_dialog);
        defaultCheckedRadioButton = v.findViewById(R.id.sale_radio_invoice_dialog);
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
        void onClick(long idProduct, int qte, String type, double priceUnit, boolean isPromo);
    }

}
