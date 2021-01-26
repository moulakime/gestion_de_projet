package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "vendor_order_line",
        foreignKeys = {
                @ForeignKey(entity = VendorOrder.class,
                        parentColumns = "id",
                        childColumns = "orderId", onDelete = CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId")},
        indices = {@Index("orderId"), @Index("productId")})
public class VendorOrderLine {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long orderId;
    private long productId;
    private int quantity;

    public VendorOrderLine(long orderId, long productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

}