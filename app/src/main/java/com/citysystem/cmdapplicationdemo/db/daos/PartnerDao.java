package com.citysystem.cmdapplicationdemo.db.daos;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.entities.Sequence;

import java.util.List;

import static com.citysystem.cmdapplicationdemo.utils.Constants.TEMPO_PARTNER;

@Dao
public abstract class PartnerDao {



    @Insert
    abstract long insertPartner(Partner partner);

    @Update
    public abstract void updatePartner(Partner partner);

    @Delete
    public abstract void deletePartner(Partner partner);

    @Query("DELETE  FROM partner WHERE tempo=:tempo")
    public abstract void deletePartnerTempo(Boolean tempo);

    @Query("SELECT * FROM partner ")
    public abstract LiveData<List<Partner>> getAllPartners();

    @Query("SELECT * FROM partner WHERE tempo=:tempo")
    public abstract LiveData<List<Partner>> getPartnerTempo(Boolean tempo);

    @Query("UPDATE  sequence set counter= :value WHERE type= :type")
    public abstract void updateCounterSequence(long value, String type);

    @Query("SELECT * FROM sequence WHERE type=:type")
    public abstract Sequence getSequence(String type);

    @Query("SELECT * FROM partner WHERE externalCode=:externalCode")
    public abstract Partner getPartner(String externalCode);


    private String getNumberOfPartnerTempo(String prefix, long counter) {
        String name;

        if (counter < 10) {
            name = prefix + "00000" + counter;
        } else if (counter < 100) {
            name = prefix + "0000" + counter;
        } else if (counter < 1000) {
            name = prefix + "000" + counter;
        } else if (counter < 10000) {
            name = prefix + "00" + counter;
        } else if (counter < 100000) {
            name = prefix + "0" + counter;
        } else {
            name = prefix + counter;
        }
        return name;
    }

    //TRANSACTIONS

    @Transaction
    public long insertPartnerTempo(Partner partner) {
        Sequence sequence = getSequence(TEMPO_PARTNER);
        if (sequence == null) {
            return -1;
        }

        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String externalCode = getNumberOfPartnerTempo(prefix, counter);
        partner.setExternalCode(externalCode);
        updateCounterSequence(++counter, TEMPO_PARTNER);
        return insertPartner(partner);

    }


}