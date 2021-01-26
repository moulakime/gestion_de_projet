package com.citysystem.cmdapplicationdemo.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.citysystem.cmdapplicationdemo.entities.CreditLine;
import com.citysystem.cmdapplicationdemo.entities.CreditNote;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.TariffLine;
import com.citysystem.cmdapplicationdemo.entities.TariffQuantities;
import com.citysystem.cmdapplicationdemo.repositories.InvoiceRepository;
import com.citysystem.cmdapplicationdemo.repositories.TariffRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.citysystem.cmdapplicationdemo.utils.Constants.DONE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.DRAFT;

public class InvoiceViewModel extends AndroidViewModel {


    private static TariffRepository tariffRepository;
    private InvoiceRepository invoiceRepository;


    public InvoiceViewModel(@NonNull Application application) {
        super(application);

        tariffRepository = new TariffRepository(application);
        invoiceRepository = new InvoiceRepository(application);

    }

    // INVOICE CRUD


    public long insertInvoice(Invoice invoice, InvoiceLine invoiceLine) {
        return invoiceRepository.insertInvoice(invoice, invoiceLine);
    }


    public long insertJustInvoice(Invoice invoice) {
        return invoiceRepository.insertJustInvoice(invoice);
    }


    public void updateInvoice(Invoice invoice) {
        if (invoice.getState().equals(DRAFT)) {
            invoice.setState(DONE);
            invoiceRepository.updateInvoice(invoice);
        }
    }

    public void deleteInvoice(Invoice invoice) {
        if (invoice.getState().equals(DRAFT)) {
            invoiceRepository.deleteInvoice(invoice);
        }
    }

    public void deleteDraftInvoices() {
        invoiceRepository.deleteInvoicesByState(DRAFT);
    }

    public void deleteDraftInvoicesByPartnerId(long partnerId) {
        invoiceRepository.deleteInvoicesByPartnerId(partnerId, DRAFT);
    }

    // INVOICE GETS

    public LiveData<List<Invoice>> getAllInvoices() {
        return invoiceRepository.getAllInvoices();
    }

    public LiveData<List<Invoice>> getInvoicesByPartnerId(long partnerId) {
        return invoiceRepository.getInvoicesByPartnerId(partnerId);
    }

    public LiveData<List<Invoice>> getInvoiceByState(String state) {
        return invoiceRepository.getInvoicesByState(state);
    }

    public Invoice getInvoiceById(long id) {
        return invoiceRepository.getInvoiceById(id);
    }

    // INVOICE LINE  CRUD

    public void insertInvoiceLine(InvoiceLine invoiceLine) {

        if (invoiceLine == null) {
            Log.i("invoiceLine", "IS NULL");
            return;
        }
        long invoiceId = invoiceLine.getInvoiceId();
        if (invoiceId <= 0) {
            Log.i("invoiceLine", "invoiceId <= 0");
            return;
        }
        String state = invoiceRepository.getInvoiceById(invoiceId).getState();
        if (state.equals(DRAFT)) {
            Log.i("invoiceLine", "DRAFT");
            invoiceRepository.insertInvoiceLine(invoiceLine);
        }
        Log.i("invoiceLine", "insertInvoiceLine");
    }

    public void updateInvoiceLine(int qte, long idInvoiceLine) {

        if (idInvoiceLine <= 0 || qte <= 0) {
            return;
        }

        //  String state = invoiceRepository.getInvoiceById(idInvoice).getState();
        //  if (state.equals(DRAFT)) {
        invoiceRepository.updateInvoiceLine(qte, idInvoiceLine);
        //  }
    }

    public void UpdateInvoiceLineViaLine(InvoiceLine invoiceLine) {

        invoiceRepository.UpdateInvoiceLineViaLine(invoiceLine);

    }

    public void deleteInvoiceLine(InvoiceLine invoiceLine) {
        if (invoiceLine == null) {
            return;
        }
        long invoiceId = invoiceLine.getInvoiceId();
        if (invoiceId <= 0) {
            return;
        }
        String state = invoiceRepository.getInvoiceById(invoiceId).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.deleteInvoiceLine(invoiceLine);
        }
    }


    public void deleteInvoiceLineByInvoiceId(long invoiceId) {

        String state = invoiceRepository.getInvoiceById(invoiceId).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.deleteInvoiceLineByInvoiceId(invoiceId);
        }
    }

    // INVOICE LINE  GETS

    public InvoiceLine getInvoiceLineById(long id) {
        return invoiceRepository.getInvoiceLineById(id);

    }

    public LiveData<List<InvoiceLine>> getInvoiceLinesByInvoiceIdLiveDataVM(long invoiceId) {
        return invoiceRepository.getInvoiceLinesByInvoiceIdLiveDataRepo(invoiceId);

    }

    public List<InvoiceLine> getInvoiceLinesByInvoiceIdVM(long invoiceId) {

        return invoiceRepository.getInvoiceLinesByInvoiceIdRepo(invoiceId);
    }

    // TARIFFS


    public long getTariffPromo() {
        return tariffRepository.getTariffPromo();

    }

    public long getTariffLineByTariffId(long tariffId, long productId) {
        return tariffRepository.getTariffLinesByProductId(tariffId, productId);

    }

    public TariffQuantities getTariffQuantitiesByLineId(int quantity, long lineId) {

        return tariffRepository.getTariffQuantitiesByLineId(quantity, lineId);

    }

    public void insertCreditNote(CreditNote creditNote, CreditLine creditLine) {
        invoiceRepository.insertCreditNote(creditNote, creditLine);

    }

    // CREDIT NOTE   CRUD

    public void generateCreditNoteByInvoice(Invoice invoice) {
        invoiceRepository.generateCreditNoteByInvoice(invoice);
    }

    public void generateCreditNoteByCreditNote(CreditNote creditNote) {
        invoiceRepository.generateCreditNoteByCreditNote(creditNote);
    }

    public void updateCreditNote(CreditNote creditNote) {

        if (creditNote.getState().equals(DRAFT)) {
            invoiceRepository.updateCreditNote(creditNote);
        }
    }

    public void deleteCreditNote(CreditNote creditNote) {
        if (creditNote.getState().equals(DRAFT)) {
            invoiceRepository.deleteCreditNote(creditNote);
        }
    }

    public void deleteDraftCreditNotes() {
        invoiceRepository.deleteCreditNotesByState(DRAFT);
    }

    public void deleteDraftCreditNoteByPartnerId(long partnerId) {

        invoiceRepository.deleteCreditNotesByPartnerId(partnerId, DRAFT);
    }

    public LiveData<List<CreditNote>> getCreditNotesByPartnerId(long partnerId) {
        return invoiceRepository.getCreditNotesByPartnerId(partnerId);
    }

    // CREDITNOTE   GETS

    public LiveData<List<CreditNote>> getCreditNotesByState(String state) {
        return invoiceRepository.getCreditNotesByState(state);
    }

    public LiveData<CreditNote> getCreditNoteById(long id) {
        return invoiceRepository.getCreditNoteById(id);
    }

    public void insertCreditLine(CreditLine creditLine) {

        if (creditLine == null) {
            return;
        }
        long creditId = creditLine.getCreditId();
        if (creditId <= 0) {
            return;
        }
        String state = Objects.requireNonNull(invoiceRepository.getCreditNoteById(creditId).getValue()).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.insertCreditLine(creditLine);
        }
    }

    // CREDIT  LINE   CRUD

    public void updateCreditLine(CreditLine creditLine) {

        if (creditLine == null) {
            return;
        }
        long creditId = creditLine.getCreditId();
        if (creditId <= 0) {
            return;
        }
        String state = Objects.requireNonNull(invoiceRepository.getCreditNoteById(creditId).getValue()).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.updateCreditLine(creditLine);
        }
    }

    public void deleteCreditLine(CreditLine creditLine) {

        if (creditLine == null) {
            return;
        }
        long creditId = creditLine.getCreditId();
        if (creditId <= 0) {
            return;
        }

        String state = Objects.requireNonNull(invoiceRepository.getCreditNoteById(creditId).getValue()).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.deleteCreditLine(creditLine);
        }
    }

    public void deleteCreditLinesByCreditId(long creditId) {

        String state = Objects.requireNonNull(invoiceRepository.getCreditNoteById(creditId).getValue()).getState();
        if (state.equals(DRAFT)) {
            invoiceRepository.deleteCreditLinesByCreditId(creditId);
        }

    }

    public LiveData<CreditLine> getCreditLineById(long id) {
        return invoiceRepository.getCreditLineById(id);

    }

    // CREDIT  LINE  GETS

    public LiveData<List<CreditLine>> getCreditLinesByCreditId(long invoiceId) {
        return invoiceRepository.getCreditLinesByCreditId(invoiceId);
    }

    public long insertTariffLine(TariffLine tariffLine) {
        try {
            return new insertTariffLineAsync().execute(tariffLine).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertTariffQuantity(TariffQuantities tariffQuantities) {
        new insertTariffQuantityAsync().execute(tariffQuantities);
    }

    private static class insertTariffLineAsync extends AsyncTask<TariffLine, Void, Long> {

        @Override
        protected Long doInBackground(TariffLine... tariffLines) {
            return tariffRepository.insertTariffLine(tariffLines[0]);
        }
    }

    private static class insertTariffQuantityAsync extends AsyncTask<TariffQuantities, Void, Void> {


        @Override
        protected Void doInBackground(TariffQuantities... tariffQuantities) {
            tariffRepository.insertTariffQuantity(tariffQuantities[0]);
            return null;
        }
    }
}
