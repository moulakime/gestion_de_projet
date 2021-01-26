package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sequence")
public class Sequence {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private String prefix;
    private int counter;
    private int size;

    public Sequence(String type, String prefix, int counter, int size) {
        this.type = type;
        this.prefix = prefix;
        this.counter = counter;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getCounter() {
        return counter;
    }

    public int getSize() {
        return size;
    }
}
