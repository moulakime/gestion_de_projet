package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "inventory")
public class Inventory {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private Date date;
    private String state;

    public Inventory() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getState() {
        return state;
    }


}
