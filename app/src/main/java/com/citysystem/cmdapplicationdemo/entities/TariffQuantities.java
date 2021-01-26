package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tariff_quantities",
        foreignKeys = {
                @ForeignKey(entity = TariffLine.class,
                        parentColumns = "id",
                        childColumns = "tariffLineId"),
        }, indices = {@Index("tariffLineId")})

public class TariffQuantities {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long tariffLineId;
    private int quantityMin;
    private int quantityMax;
    private double priceUnit;
    private long gratisProductId;
    private int gratisQuantity;

    @Ignore
    public TariffQuantities(long tariffLineId, int quantityMin, int quantityMax, double priceUnit, long gratisProductId, int gratisQuantity) {
        this.tariffLineId = tariffLineId;
        this.quantityMin = quantityMin;
        this.quantityMax = quantityMax;
        this.priceUnit = priceUnit;
        this.gratisProductId = gratisProductId;
        this.gratisQuantity = gratisQuantity;
    }

    public TariffQuantities(long tariffLineId, int quantityMin, int quantityMax, double priceUnit) {
        this.tariffLineId = tariffLineId;
        this.quantityMin = quantityMin;
        this.quantityMax = quantityMax;
        this.priceUnit = priceUnit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTariffLineId() {
        return tariffLineId;
    }

    public void setTariffLineId(long tariffLineId) {
        this.tariffLineId = tariffLineId;
    }

    public int getQuantityMin() {
        return quantityMin;
    }

    public void setQuantityMin(int quantityMin) {
        this.quantityMin = quantityMin;
    }

    public int getQuantityMax() {
        return quantityMax;
    }

    public void setQuantityMax(int quantityMax) {
        this.quantityMax = quantityMax;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public long getGratisProductId() {
        return gratisProductId;
    }

    public void setGratisProductId(long gratisProductId) {
        this.gratisProductId = gratisProductId;
    }

    public int getGratisQuantity() {
        return gratisQuantity;
    }

    public void setGratisQuantity(int gratisQuantity) {
        this.gratisQuantity = gratisQuantity;
    }
}
