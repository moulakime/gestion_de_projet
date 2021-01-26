package com.citysystem.cmdapplicationdemo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.citysystem.cmdapplicationdemo.R;
import com.citysystem.cmdapplicationdemo.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item, viewGroup, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int i) {

        Product currentProduct = products.get(i);
        if (currentProduct.isFavorite()) {
            productHolder.imageView_add_favorite.setImageResource(R.drawable.ic_is_favorite);
        } else {
            productHolder.imageView_add_favorite.setImageResource(R.drawable.ic_is_not_favorite);
        }
        productHolder.textViewName.setText(currentProduct.getName());
        productHolder.textViewCommercialDesignation.setText(currentProduct.getCommercialName());
        productHolder.textViewPriceUnit.setText(String.valueOf(currentProduct.getPriceUnit()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setFavoriteProducts(List<Product> favoriteProducts) {
        this.products = favoriteProducts;
        notifyDataSetChanged();
    }

    public void setPromoProducts(List<Product> promoProducts) {
        this.products = promoProducts;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPriceUnit;
        private TextView textViewCommercialDesignation;
        private ImageView imageView_add_favorite;


        ProductHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_product_name);
            textViewPriceUnit = itemView.findViewById(R.id.text_view_product_price);
            textViewCommercialDesignation = itemView.findViewById(R.id.text_view_qte);
            imageView_add_favorite = itemView.findViewById(R.id.ic_add_favorite);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(products.get(position));
                    }
                }
            });
        }
    }
}
