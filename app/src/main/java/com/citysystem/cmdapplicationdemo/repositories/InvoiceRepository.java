package com.citysystem.cmdapplicationdemo.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.citysystem.cmdapplicationdemo.db.daos.InvoiceDao;
import com.citysystem.cmdapplicationdemo.db.room.CmdDataBase;
import com.citysystem.cmdapplicationdemo.entities.CreditLine;
import com.citysystem.cmdapplicationdemo.entities.CreditNote;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InvoiceRepository {


    private InvoiceDao invoiceDao;
    private LiveData<List<Invoice>> allInvoices;
    private LiveData<List<CreditNote>> allCredits;

    public InvoiceRepository(Application application) {
        CmdDataBase database = CmdDataBase.getInstance(application);
        invoiceDao = database.invoiceDao();
        allInvoices = invoiceDao.getAllInvoices();
        allCredits = invoiceDao.getAllCreditNotes();
    }

    // INVOICE GETS

    public LiveData<List<Invoice>> getAllInvoices() {
        return allInvoices;
    }

    public Invoice getInvoiceById(long id) {
        try {
            return new getInvoiceByIdAsyncTask(invoiceDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Invoice>> getInvoicesByState(String state) {
        return invoiceDao.getInvoiceByState(state);
    }

    public LiveData<List<Invoice>> getInvoicesByPartnerId(long partnerId) {
        return invoiceDao.getInvoicesByPartnerId(partnerId);
    }

    public long insertInvoice(Invoice invoice, InvoiceLine invoiceLine) {

        try {
            return new InsertInvoiceAsyncTask(invoiceDao).execute(invoice, invoiceLine).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insertJustInvoice(Invoice invoice) {

        try {
            return new InsertJustInvoiceAsyncTask(invoiceDao).execute(invoice).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }


    //INVOICE CRUD

    public void updateInvoice(Invoice invoice) {
        new UpdateInvoiceAsyncTask(invoiceDao).execute(invoice);

    }

    public void deleteInvoice(Invoice invoice) {
        new DeleteInvoiceAsyncTask(invoiceDao).execute(invoice);

    }

    public void deleteInvoicesByState(String state) {
        new DeleteInvoicesByStateAsyncTask(invoiceDao).execute(state);

    }

    public void deleteInvoicesByPartnerId(long partnerId, String state) {
        new DeleteInvoicesByPartnerIdAsyncTask(invoiceDao).execute(partnerId, state);

    }

    public InvoiceLine getInvoiceLineById(long id) {
        return invoiceDao.getInvoiceLineById(id);
    }

    // INVOICE LINE GETS

    public LiveData<List<InvoiceLine>> getInvoiceLinesByInvoiceIdLiveDataRepo(long invoiceId) {
        return invoiceDao.getInvoiceLinesByInvoiceIdLiveData(invoiceId);
    }

    public List<InvoiceLine> getInvoiceLinesByInvoiceIdRepo(long invoiceId) {
        try {
            return new getInvoiceLinesByInvoiceIdRepoAsync(invoiceDao).execute(invoiceId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertInvoiceLine(InvoiceLine invoiceLine) {
        new InsertInvoiceLineAsyncTask(invoiceDao).execute(invoiceLine);
    }

    public void updateInvoiceLine(int qte, long idInvoiceLine) {
        new UpdateInvoiceLineAsyncTask(invoiceDao).execute(qte, idInvoiceLine);
    }

    //INVOICE LINE CRUD

    public void UpdateInvoiceLineViaLine(InvoiceLine invoiceLine) {
        new UpdateInvoiceLineViaLineAsyncTask(invoiceDao).execute(invoiceLine);
    }

    public void deleteInvoiceLine(InvoiceLine invoiceLine) {
        new DeleteInvoiceLineAsyncTask(invoiceDao).execute(invoiceLine);
    }

    public void deleteInvoiceLineByInvoiceId(long invoiceId) {
        new DeleteInvoiceLineByInvoiceIdAsyncTask(invoiceDao).execute(invoiceId);
    }

    public LiveData<List<CreditNote>> getAllCredits() {
        return allCredits;
    }

    public LiveData<CreditNote> getCreditNoteById(long id) {
        return invoiceDao.getCreditNoteById(id);
    }

    public LiveData<List<CreditNote>> getCreditNotesByState(String state) {
        return invoiceDao.getCreditNoteByState(state);
    }

    public LiveData<List<CreditNote>> getCreditNotesByPartnerId(long partnerId) {
        return invoiceDao.getCreditNoteByPartnerId(partnerId);
    }

    public void insertCreditNote(CreditNote creditNote, CreditLine creditLine) {
        new InsertCreditNoteAsyncTask(invoiceDao).execute(creditNote, creditLine);
    }

    public void generateCreditNoteByInvoice(Invoice invoice) {

        new GenerateCreditNoteByInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public void generateCreditNoteByCreditNote(CreditNote creditNote) {

        new GenerateCreditNoteByCreditNoteAsyncTask(invoiceDao).execute(creditNote);
    }

    //  INVOICE LINE ASYNC TASKS

    public void updateCreditNote(CreditNote creditNote) {
        new UpdateCreditNoteAsyncTask(invoiceDao).execute(creditNote);

    }

    public void deleteCreditNote(CreditNote creditNote) {
        new DeleteCreditNoteAsyncTask(invoiceDao).execute(creditNote);

    }

    public void deleteCreditNotesByState(String state) {
        new DeleteCreditNoteByStateAsyncTask(invoiceDao).execute(state);

    }

    public void deleteCreditNotesByPartnerId(long partnerId, String state) {
        new DeleteCreditNoteByPartnerIdAsyncTask(invoiceDao).execute(partnerId, state);

    }


    ///////////////////////////////////////////////////////////////////

// CREDIT NOTE GETS

    public LiveData<List<CreditLine>> getCreditLinesByCreditId(long creditId) {
        return invoiceDao.getCreditLineByCreditId(creditId);
    }

    public void insertCreditLine(CreditLine creditLine) {
        new InsertCreditLineAsyncTask(invoiceDao).execute(creditLine);
    }

    public void updateCreditLine(CreditLine creditLine) {
        new UpdateCreditLineAsyncTask(invoiceDao).execute(creditLine);
    }

    public void deleteCreditLine(CreditLine creditLine) {
        new DeleteCreditLineAsyncTask(invoiceDao).execute(creditLine);
    }

    // CREDIT NOTES CRUD

    public void deleteCreditLinesByCreditId(long invoiceId) {
        new DeleteCreditLineByCreditIdAsyncTask(invoiceDao).execute(invoiceId);
    }

    public LiveData<CreditLine> getCreditLineById(long id) {
        return invoiceDao.getCreditLineById(id);
    }

    private static class getInvoiceLinesByInvoiceIdRepoAsync extends AsyncTask<Long, Void, List<InvoiceLine>> {

        private InvoiceDao invoiceDao;

        private getInvoiceLinesByInvoiceIdRepoAsync(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected List<InvoiceLine> doInBackground(Long... longs) {
            return invoiceDao.getInvoiceLinesByInvoiceId(longs[0]);
        }
    }

    private static class getInvoiceByIdAsyncTask extends AsyncTask<Long, Void, Invoice> {


        private InvoiceDao invoiceDao;

        private getInvoiceByIdAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }


        @Override
        protected Invoice doInBackground(Long... longs) {

            return invoiceDao.getInvoiceById(longs[0]);
        }
    }

    //  INVOICE ASYNC TASKS
    private static class InsertInvoiceAsyncTask extends AsyncTask<Object, Void, Long> {

        private InvoiceDao invoiceDao;

        private InsertInvoiceAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Long doInBackground(Object... params) {

            Invoice invoice = (Invoice) params[0];
            InvoiceLine invoiceLine = (InvoiceLine) params[1];

            return invoiceDao.insertInvoiceAndInvoiceLine(invoice, invoiceLine);
        }
    }

    private static class InsertJustInvoiceAsyncTask extends AsyncTask<Invoice, Void, Long> {
        private InvoiceDao invoiceDao;

        private InsertJustInvoiceAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Long doInBackground(Invoice... invoices) {
            return invoiceDao.insertJustInvoice(invoices[0]);
        }
    }


    //  INVOICE ASYNC TASKS

    private static class UpdateInvoiceAsyncTask extends AsyncTask<Invoice, Void, Void> {

        private InvoiceDao invoiceDao;

        private UpdateInvoiceAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            invoiceDao.updateInvoice(invoices[0]);
            return null;
        }
    }

    private static class DeleteInvoiceAsyncTask extends AsyncTask<Invoice, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteInvoiceAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            invoiceDao.deleteInvoice(invoices[0]);
            return null;
        }
    }

    // CREDIT LINES GETS

    private static class DeleteInvoicesByStateAsyncTask extends AsyncTask<String, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteInvoicesByStateAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(String... params) {

            invoiceDao.deleteInvoicesByState(params[0]);
            return null;
        }
    }

    // CREDIT LINES CRUD

    private static class DeleteInvoicesByPartnerIdAsyncTask extends AsyncTask<Object, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteInvoicesByPartnerIdAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            long partnerId = (long) params[0];
            String state = (String) params[1];
            invoiceDao.deleteInvoicesByPartnerId(partnerId, state);
            return null;
        }
    }

    private static class InsertInvoiceLineAsyncTask extends AsyncTask<InvoiceLine, Void, Void> {
        private InvoiceDao invoiceDao;

        private InsertInvoiceLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(InvoiceLine... invoiceLines) {
            invoiceDao.insertInvoiceLine(invoiceLines[0]);
            return null;
        }
    }

    private static class UpdateInvoiceLineAsyncTask extends AsyncTask<Object, Void, Void> {
        private InvoiceDao invoiceDao;

        UpdateInvoiceLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... objects) {

            invoiceDao.updateInvoiceLinesByInvoiceId((int) objects[0], (long) objects[1]);
            return null;
        }
    }

    private static class UpdateInvoiceLineViaLineAsyncTask extends AsyncTask<InvoiceLine, Void, Void> {
        private InvoiceDao invoiceDao;

        UpdateInvoiceLineViaLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(InvoiceLine... invoiceLines) {
            invoiceDao.updateInvoiceLine(invoiceLines[0]);
            Log.i("Mytest", invoiceLines[0].getQuantity() + "");
            return null;
        }
    }

    private static class DeleteInvoiceLineAsyncTask extends AsyncTask<InvoiceLine, Void, Void> {
        private InvoiceDao invoiceDao;

        public DeleteInvoiceLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(InvoiceLine... invoiceLines) {
            invoiceDao.deleteInvoiceLine(invoiceLines[0]);
            return null;
        }
    }

    private static class DeleteInvoiceLineByInvoiceIdAsyncTask extends AsyncTask<Object, Void, Void> {
        private InvoiceDao invoiceDao;

        public DeleteInvoiceLineByInvoiceIdAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            long invoiceId = (long) params[0];
            invoiceDao.deleteInvoiceLinesByInvoiceId(invoiceId);
            return null;
        }
    }


    // ASYNC TASKS

    private static class InsertCreditLineAsyncTask extends AsyncTask<CreditLine, Void, Void> {
        private InvoiceDao invoiceDao;

        public InsertCreditLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditLine... creditLines) {
            invoiceDao.insertCreditLine(creditLines[0]);
            return null;
        }
    }

    private static class UpdateCreditLineAsyncTask extends AsyncTask<CreditLine, Void, Void> {
        private InvoiceDao invoiceDao;

        public UpdateCreditLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditLine... creditLines) {
            invoiceDao.updateCreditLine(creditLines[0]);
            return null;
        }
    }

    private static class DeleteCreditLineAsyncTask extends AsyncTask<CreditLine, Void, Void> {
        private InvoiceDao invoiceDao;

        public DeleteCreditLineAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditLine... creditLines) {
            invoiceDao.deleteCreditLine(creditLines[0]);
            return null;
        }
    }

    private static class DeleteCreditLineByCreditIdAsyncTask extends AsyncTask<Object, Void, Void> {
        private InvoiceDao invoiceDao;

        public DeleteCreditLineByCreditIdAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            long creditLine = (long) params[0];
            invoiceDao.deleteCreditLinesByCreditId(creditLine);
            return null;
        }
    }

    private static class InsertCreditNoteAsyncTask extends AsyncTask<Object, Void, Void> {

        private InvoiceDao invoiceDao;

        private InsertCreditNoteAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {

            CreditNote creditNote = (CreditNote) params[0];
            CreditLine creditLine = (CreditLine) params[1];
            invoiceDao.insertCreditNoteAndCreditLine(creditNote, creditLine);

            return null;
        }
    }


    private static class GenerateCreditNoteByCreditNoteAsyncTask extends AsyncTask<CreditNote, Void, Void> {

        private InvoiceDao invoiceDao;

        private GenerateCreditNoteByCreditNoteAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditNote... creditNotes) {
            invoiceDao.generateCreditNoteByCreditNote(creditNotes[0]);
            return null;
        }
    }

    private static class GenerateCreditNoteByInvoiceAsyncTask extends AsyncTask<Invoice, Void, Void> {

        private InvoiceDao invoiceDao;

        private GenerateCreditNoteByInvoiceAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            invoiceDao.generateCreditNoteByInvoice(invoices[0]);
            return null;
        }
    }

    private static class UpdateCreditNoteAsyncTask extends AsyncTask<CreditNote, Void, Void> {

        private InvoiceDao invoiceDao;

        private UpdateCreditNoteAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditNote... creditNotes) {
            invoiceDao.updateCreditNote(creditNotes[0]);
            return null;
        }
    }

    private static class DeleteCreditNoteAsyncTask extends AsyncTask<CreditNote, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteCreditNoteAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(CreditNote... creditNotes) {
            invoiceDao.deleteCreditNote(creditNotes[0]);
            return null;
        }
    }

    private static class DeleteCreditNoteByPartnerIdAsyncTask extends AsyncTask<Object, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteCreditNoteByPartnerIdAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            long partnerId = (long) params[0];
            String state = (String) params[1];
            invoiceDao.deleteCreditNoteByPartnerId(partnerId, state);
            return null;
        }
    }

    private static class DeleteCreditNoteByStateAsyncTask extends AsyncTask<Object, Void, Void> {

        private InvoiceDao invoiceDao;

        private DeleteCreditNoteByStateAsyncTask(InvoiceDao invoiceDao) {
            this.invoiceDao = invoiceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            String state = (String) params[0];
            invoiceDao.deleteCreditNoteByState(state);

            return null;
        }
    }


}
