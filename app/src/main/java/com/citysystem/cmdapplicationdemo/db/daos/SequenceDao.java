package com.citysystem.cmdapplicationdemo.db.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.citysystem.cmdapplicationdemo.entities.Sequence;

@Dao
public interface SequenceDao {

    @Insert
    void insert(Sequence sequence);

    @Update
    void update(Sequence sequence);

    @Query("UPDATE  sequence set counter= :value WHERE type= :type")
    void updateCountSequence(long value, String type);


    @Query("SELECT * FROM sequence WHERE type=:type")
    Sequence getSequence(String type);
}
