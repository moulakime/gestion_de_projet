package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tariff")
public class Tariff {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private boolean isPromo;
    private boolean isDefault;

    public Tariff(String name, boolean isPromo, boolean isDefault) {
        this.name = name;
        this.isPromo = isPromo;
        this.isDefault = isDefault;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPromo() {
        return isPromo;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
