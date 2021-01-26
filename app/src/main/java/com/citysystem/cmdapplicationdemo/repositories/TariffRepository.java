package com.citysystem.cmdapplicationdemo.repositories;


import android.app.Application;
import android.os.AsyncTask;

import com.citysystem.cmdapplicationdemo.db.daos.TariffDao;
import com.citysystem.cmdapplicationdemo.db.room.CmdDataBase;
import com.citysystem.cmdapplicationdemo.entities.TariffLine;
import com.citysystem.cmdapplicationdemo.entities.TariffQuantities;

import java.util.concurrent.ExecutionException;

import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_DEFAULT;
import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_PROMO;


public class TariffRepository {

    private TariffDao tariffDao;

    public TariffRepository(Application application) {
        CmdDataBase database = CmdDataBase.getInstance(application);
        tariffDao = database.tariffDao();

    }

    public long insertTariffLine(TariffLine tariffLine) {
        return tariffDao.insertTariffLine(tariffLine);
    }

    public void insertTariffQuantity(TariffQuantities tariffQuantities) {
        tariffDao.insertTariffQuantities(tariffQuantities);
    }


    public long getTariffPromo() {
        try {
            return new getTariffPromoAsyncTask(tariffDao).execute(IS_PROMO).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getDefaultTariff() {
        try {
            return new getDefaultTariffAsyncTask(tariffDao).execute(IS_DEFAULT).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public long getTarifById(long Id) {
        return tariffDao.getTariffById(Id);
    }

    public long getTariffLinesByProductId(long tariffId, long productId) {
        try {
            return new getTariffLinesByProductIdAsyncTask(tariffDao).execute(tariffId, productId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public TariffQuantities getTariffQuantitiesByLineId(int quantity, long tariffId) {
        try {
            return new getTariffQuantitiesByLineIdAsyncTask(tariffDao).execute(quantity, tariffId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getTariffLinesByProductIdAsyncTask extends AsyncTask<Long, Void, Long> {

        private TariffDao tariffDao;

        private getTariffLinesByProductIdAsyncTask(TariffDao tariffDao) {
            this.tariffDao = tariffDao;
        }

        @Override
        protected Long doInBackground(Long... longs) {
            return tariffDao.getTariffLineByProductId(longs[0], longs[1]);
        }
    }

    private static class getTariffQuantitiesByLineIdAsyncTask extends AsyncTask<Object, Void, TariffQuantities> {

        private TariffDao tariffDao;

        private getTariffQuantitiesByLineIdAsyncTask(TariffDao tariffDao) {
            this.tariffDao = tariffDao;
        }

        @Override
        protected TariffQuantities doInBackground(Object... objects) {
            return tariffDao.getTariffByQuantity((int) objects[0], (long) objects[1]);
        }
    }


    private static class getDefaultTariffAsyncTask extends AsyncTask<Boolean, Void, Long> {

        private TariffDao tariffDao;

        private getDefaultTariffAsyncTask(TariffDao tariffDao) {
            this.tariffDao = tariffDao;
        }

        @Override
        protected Long doInBackground(Boolean... booleans) {
            return tariffDao.getDefaultTariff(booleans[0]);
        }
    }

    public static class getTariffPromoAsyncTask extends AsyncTask<Boolean, Void, Long> {

        private TariffDao tariffDao;

        private getTariffPromoAsyncTask(TariffDao tariffDao) {
            this.tariffDao = tariffDao;
        }

        @Override
        protected Long doInBackground(Boolean... booleans) {
            return tariffDao.getTariffPromo(booleans[0]);
        }
    }

}
