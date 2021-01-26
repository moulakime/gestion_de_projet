package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//@Entity(tableName = "brand", indices = arrayOf(Index("name")))

@Entity(tableName = "brand")
public class Brand {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Brand(String name) {
        this.name = name;
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
}