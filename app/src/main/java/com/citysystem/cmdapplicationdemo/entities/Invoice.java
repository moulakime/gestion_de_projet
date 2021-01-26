package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "invoice",
        foreignKeys = {
                @ForeignKey(entity = Partner.class,
                        parentColumns = "id", childColumns = "partnerId")
        }, indices = @Index("partnerId"))
public class Invoice {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private Date date;
    private String state;
    private long partnerId;


    @Ignore
    public Invoice() {
    }

    public Invoice(String name, Date date, String state, long partnerId) {
        this.name = name;
        this.date = date;
        this.state = state;
        this.partnerId = partnerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
