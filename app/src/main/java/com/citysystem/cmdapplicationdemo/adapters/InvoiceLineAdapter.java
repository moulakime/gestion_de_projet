package com.citysystem.cmdapplicationdemo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;

import java.util.ArrayList;
import java.util.List;

public class InvoiceLineAdapter extends RecyclerView.Adapter<InvoiceLineAdapter.InvoiceLineHolder> {

    private List<InvoiceLine> invoiceLines = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public InvoiceLineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.invoice_line_item, viewGroup, false);

        return new InvoiceLineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceLineHolder invoiceLineHolder, int i) {

        InvoiceLine currentInvoiceLine = invoiceLines.get(i);
        final int qte = currentInvoiceLine.getQuantity();
        final double priceUnit = currentInvoiceLine.getPriceUnit();
        invoiceLineHolder.textViewNameProduct.setText(String.valueOf(currentInvoiceLine.getName()));
        invoiceLineHolder.textViewPriceUnit.setText(String.valueOf(priceUnit));
        invoiceLineHolder.textViewType.setText(String.valueOf(currentInvoiceLine.getType()));
        invoiceLineHolder.textViewQte.setText(String.valueOf(qte));
        invoiceLineHolder.textViewMt.setText(String.valueOf(qte * priceUnit));
    }

    @Override
    public int getItemCount() {
        return invoiceLines.size();
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
        notifyDataSetChanged();
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (InvoiceLine invoiceLine : invoiceLines) {
            totalAmount = totalAmount + (invoiceLine.getQuantity() * invoiceLine.getPriceUnit());

        }
        return totalAmount;
    }

    public InvoiceLine getInvoiceLine(int position) {
        return invoiceLines.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClickTwo(InvoiceLine line);
    }

    class InvoiceLineHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameProduct;
        private TextView textViewPriceUnit;
        private TextView textViewType;
        private TextView textViewQte;
        private TextView textViewMt;


        InvoiceLineHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameProduct = itemView.findViewById(R.id.text_view_product_name_invoice_line);
            textViewPriceUnit = itemView.findViewById(R.id.text_view_product_price_invoice_line);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewQte = itemView.findViewById(R.id.text_view_qte_invoice_line);
            textViewMt = itemView.findViewById(R.id.txt_calculate_qte_price_unit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClickTwo(invoiceLines.get(position));
                    }
                }
            });
        }
    }
}
