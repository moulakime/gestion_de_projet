package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tariff_line",
        foreignKeys = {
                @ForeignKey(entity = Tariff.class,
                        parentColumns = "id",
                        childColumns = "tariffId"),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId")
        }, indices = {@Index("tariffId"), @Index("productId")})
public class TariffLine {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long tariffId;
    private long productId;

    public TariffLine(long tariffId, long productId) {
        this.tariffId = tariffId;
        this.productId = productId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTariffId() {
        return tariffId;
    }

    public long getProductId() {
        return productId;
    }
}
