package com.citysystem.cmdapplicationdemo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Partner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.PartnerHolder> implements Filterable {

    private List<Partner> partnersList = new ArrayList<>();
    private List<Partner> allPartnersList;
    private OnItemClickListener mListener;
    private Filter partnersFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Partner> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allPartnersList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Partner item : allPartnersList) {
                    if (item.getPartnerName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            partnersList.clear();
            partnersList.addAll((Collection<? extends Partner>) results.values);
            notifyDataSetChanged();
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PartnerAdapter.PartnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_item, parent, false);
        return new PartnerHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerAdapter.PartnerHolder holder, int position) {
        Partner currentPartner = partnersList.get(position);
        holder.imgUser.setImageResource(R.drawable.ic_user);
        holder.textViewName.setText(currentPartner.getPartnerName());
        holder.textViewExternalCode.setText(currentPartner.getExternalCode());
        holder.textViewTariff.setText(String.valueOf(currentPartner.getTariffId()));
    }

    @Override
    public int getItemCount() {
        return partnersList.size();
    }

    public void setPartnersList(List<Partner> partnersList) {
        this.partnersList = partnersList;
        this.allPartnersList = new ArrayList<>(partnersList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return partnersFilter;
    }

    public interface OnItemClickListener {

        void onItemClick(Partner selectedPartner);
    }

    class PartnerHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser;
        private TextView textViewName;
        private TextView textViewExternalCode;
        private TextView textViewTariff;

        PartnerHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_partner_id);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewExternalCode = itemView.findViewById(R.id.text_view_external_code);
            textViewTariff = itemView.findViewById(R.id.text_view_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Partner selectedPartner = partnersList.get(position);
                            listener.onItemClick(selectedPartner);
                        }
                    }
                }
            });
        }
    }
}