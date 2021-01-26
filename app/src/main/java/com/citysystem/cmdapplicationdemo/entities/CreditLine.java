package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "credit_line",
        foreignKeys = {
                @ForeignKey(entity = CreditNote.class,
                        parentColumns = "id", childColumns = "creditId", onDelete = CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId")
        }, indices = {@Index("creditId"), @Index("productId")})
public class CreditLine {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long creditId;
    private long productId;
    private int quantity0;
    private int quantity1;


    public CreditLine(long productId, int quantity0) {

        this.productId = productId;
        this.quantity0 = quantity0;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreditId() {
        return creditId;
    }

    public void setCreditId(long creditId) {
        this.creditId = creditId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity0() {
        return quantity0;
    }

    public void setQuantity0(int quantity0) {
        this.quantity0 = quantity0;
    }

    public int getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(int quantity1) {
        this.quantity1 = quantity1;
    }
}
