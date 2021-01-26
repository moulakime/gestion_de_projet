package com.citysystem.cmdapplicationdemo.repositories;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.citysystem.cmdapplicationdemo.db.daos.PartnerDao;
import com.citysystem.cmdapplicationdemo.db.room.CmdDataBase;
import com.citysystem.cmdapplicationdemo.entities.Partner;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PartnerRepository {


    private PartnerDao partnerDao;

    public PartnerRepository(Application application) {
        CmdDataBase database = CmdDataBase.getInstance(application);
        partnerDao = database.partnerDao();

    }

    public void updatePartner(Partner partner) {
        new UpdatePartnerAsyncTask(partnerDao).execute(partner);
    }

    public void deletePartner(Partner partner) {
        new DeletePartnerAsyncTask(partnerDao).execute(partner);
    }

    public void deletePartnerTempo() {
        new DeletePartnerTempoAsyncTask(partnerDao).execute();
    }


    public LiveData<List<Partner>> getAllPartners() {
        try {
            return new getAllPartnersAsyncTask(partnerDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long insertPartnerRepo(Partner partner) {
        try {
            return new insertPartnersAsyncTask(partnerDao).execute(partner).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public LiveData<List<Partner>> getPartnerTempo() {
        return partnerDao.getPartnerTempo(true);
    }

    public Partner getPartnerViaScanner(String external) {

        try {
            return new getPartnerViaScannerAsyncTask(partnerDao).execute(external).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class insertPartnersAsyncTask extends AsyncTask<Partner, Void, Long> {

        private PartnerDao partnerDao;

        private insertPartnersAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected Long doInBackground(Partner... partners) {
            return partnerDao.insertPartnerTempo(partners[0]);
        }
    }

    private static class getAllPartnersAsyncTask extends AsyncTask<Void, Void, LiveData<List<Partner>>> {

        private PartnerDao partnerDao;

        private getAllPartnersAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected LiveData<List<Partner>> doInBackground(Void... voids) {
            return partnerDao.getAllPartners();
        }
    }

    private static class getPartnerViaScannerAsyncTask extends AsyncTask<String, Void, Partner> {

        private PartnerDao partnerDao;

        private getPartnerViaScannerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected Partner doInBackground(String... strings) {
            return partnerDao.getPartner(strings[0]);
        }
    }


    private static class UpdatePartnerAsyncTask extends AsyncTask<Partner, Void, Void> {

        private PartnerDao partnerDao;

        private UpdatePartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected Void doInBackground(Partner... partners) {
            partnerDao.updatePartner(partners[0]);
            return null;
        }
    }

    private static class DeletePartnerAsyncTask extends AsyncTask<Partner, Void, Void> {

        private PartnerDao partnerDao;

        private DeletePartnerAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected Void doInBackground(Partner... partners) {
            partnerDao.deletePartner(partners[0]);
            return null;
        }
    }

    private static class DeletePartnerTempoAsyncTask extends AsyncTask<Void, Void, Void> {

        private PartnerDao partnerDao;

        DeletePartnerTempoAsyncTask(PartnerDao partnerDao) {
            this.partnerDao = partnerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            partnerDao.deletePartnerTempo(true);
            return null;
        }
    }


}

