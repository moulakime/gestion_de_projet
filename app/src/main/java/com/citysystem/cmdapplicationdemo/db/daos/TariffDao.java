package com.citysystem.cmdapplicationdemo.db.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.citysystem.cmdapplicationdemo.entities.Tariff;
import com.citysystem.cmdapplicationdemo.entities.TariffLine;
import com.citysystem.cmdapplicationdemo.entities.TariffQuantities;


@Dao
public interface TariffDao {


    @Insert
    long insertTariff(Tariff tariff);

    @Update
    void updateTarif(Tariff tariff);

    @Delete
    void deleteTarif(Tariff tariff);

    @Query("SELECT id FROM Tariff WHERE isDefault =:isDef")
    long getDefaultTariff(boolean isDef);

    @Query("SELECT * FROM Tariff WHERE id =:Id")
    long getTariffById(long Id);

    //TARIF LINES

    @Insert
    long insertTariffLine(TariffLine tariffLine);

    @Update
    void updateTariffLine(TariffLine tariffLine);

    @Delete
    void deleteTariffLine(TariffLine tariffLine);

    @Query("SELECT id FROM  tariff_line WHERE tariffId=:tariffId and productId=:productId")
    long getTariffLineByProductId(long tariffId, long productId);

    @Query("SELECT id FROM tariff WHERE isPromo=:isPromo")
    long getTariffPromo(boolean isPromo);

    //TARIFFS QUANTITIES

    @Insert
    void insertTariffQuantities(TariffQuantities tariffQuantities);

    @Update
    void updateTariffQuantities(TariffQuantities tariffQuantities);

    @Delete
    void deleteTariffQuantities(TariffQuantities tariffQuantities);


    @Query("SELECT * FROM tariff_quantities WHERE quantityMin<=:quantity and quantityMax>:quantity and tariffLineId=:tariffLineId")
    TariffQuantities getTariffByQuantity(int quantity, long tariffLineId);


}
