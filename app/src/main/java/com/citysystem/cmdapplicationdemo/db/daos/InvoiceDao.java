package com.citysystem.cmdapplicationdemo.db.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.util.Log;

import com.citysystem.cmdapplicationdemo.entities.CreditLine;
import com.citysystem.cmdapplicationdemo.entities.CreditNote;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.Sequence;

import java.util.Date;
import java.util.List;

import static com.citysystem.cmdapplicationdemo.utils.Constants.CREDIT_NOTE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.DRAFT;
import static com.citysystem.cmdapplicationdemo.utils.Constants.INVOICE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.RETOUR;

@Dao
public abstract class InvoiceDao {


    @Query("UPDATE  sequence set counter= :value WHERE type= :type")
    abstract void updateCounterSequence(long value, String type);

    @Query("SELECT * FROM sequence WHERE type=:type")
    public abstract Sequence getSequence(String type);

    private String getNumberOfInvoice(String prefix, long counter) {
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

    @Insert
    public abstract long insertInvoice(Invoice invoice);

    @Update
    public abstract void updateInvoice(Invoice invoice);

    @Transaction
    @Delete
    public abstract void deleteInvoice(Invoice invoice);

    @Transaction
    @Query("DELETE  FROM  invoice WHERE partnerId=:partnerId and state=:state")
    public abstract void deleteInvoicesByPartnerId(long partnerId, String state);

    @Transaction
    @Query("DELETE  FROM  invoice WHERE state=:state")
    public abstract void deleteInvoicesByState(String state);

    @Query("SELECT * FROM  invoice WHERE id=:Id")
    public abstract Invoice getInvoiceById(long Id);

    @Query("SELECT * FROM  invoice")
    public abstract LiveData<List<Invoice>> getAllInvoices();

    @Query("SELECT * FROM  invoice WHERE partnerId=:partnerId")
    public abstract LiveData<List<Invoice>> getInvoicesByPartnerId(long partnerId);

    @Query("SELECT * FROM  invoice WHERE state=:state")
    public abstract LiveData<List<Invoice>> getInvoiceByState(String state);


    @Insert
    public abstract void insertInvoiceLine(InvoiceLine invoice);

    @Update
    public abstract void updateInvoiceLine(InvoiceLine invoiceLine);

    @Delete
    public abstract void deleteInvoiceLine(InvoiceLine invoice);

    @Transaction
    @Query("DELETE  FROM  invoice_line WHERE invoiceId=:invoiceId")
    public abstract void deleteInvoiceLinesByInvoiceId(long invoiceId);

    @Query("SELECT * FROM  invoice_line WHERE id=:Id")
    public abstract InvoiceLine getInvoiceLineById(long Id);

    @Query("SELECT invoice_line.id,invoice_line.productId,invoice_line.invoiceId,product.name,invoice_line.priceUnit,invoice_line.quantity,invoice_line.type FROM  invoice_line,product WHERE invoice_line.productId = product.id and invoiceId=:invoiceId")
    public abstract LiveData<List<InvoiceLine>> getInvoiceLinesByInvoiceIdLiveData(long invoiceId);

    @Query("SELECT * FROM  invoice_line WHERE invoiceId=:invoiceId")
    public abstract List<InvoiceLine> getInvoiceLinesByInvoiceId(long invoiceId);

    @Query("UPDATE invoice_line set quantity=:qteLine WHERE id=:idLine ")
    public abstract void updateInvoiceLinesByInvoiceId(int qteLine, long idLine);


    //TRANSACTION

    @Transaction
    public long insertJustInvoice(Invoice invoice) {
        Sequence sequence = getSequence(INVOICE);
        if (sequence == null) {
            return -1;
        }
        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String name = getNumberOfInvoice(prefix, counter);
        invoice.setDate(new Date());
        invoice.setName(name);
        invoice.setState(DRAFT);
        updateCounterSequence(++counter, INVOICE);
        return insertInvoice(invoice);

    }


    @Transaction
    public long insertInvoiceAndInvoiceLine(Invoice invoice, InvoiceLine invoiceLine) {

        Sequence sequence = getSequence(INVOICE);
        if (sequence == null) {
            return -1;
        }
        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String name = getNumberOfInvoice(prefix, counter);
        invoice.setDate(new Date());
        invoice.setName(name);
        invoice.setState(DRAFT);

        updateCounterSequence(++counter, INVOICE);
        long idInvoice = insertInvoice(invoice);
        Log.i("InvoiceIdTransaction", idInvoice + "");
        invoiceLine.setInvoiceId(idInvoice);
        insertInvoiceLine(invoiceLine);
        return idInvoice;
    }


    @Insert
    abstract long insertCreditNote(CreditNote invoice);

    @Update
    public abstract void updateCreditNote(CreditNote invoice);

    @Transaction
    @Delete
    public abstract void deleteCreditNote(CreditNote invoice);

    @Transaction
    @Query("DELETE  FROM  credit_note WHERE state=:state")
    public abstract void deleteCreditNoteByState(String state);

    @Transaction
    @Query("DELETE FROM  credit_note WHERE partnerId=:partnerId and state=:state")
    public abstract void deleteCreditNoteByPartnerId(long partnerId, String state);

    @Query("SELECT * FROM  credit_note")
    public abstract LiveData<List<CreditNote>> getAllCreditNotes();

    @Query("SELECT * FROM  credit_note WHERE state=:state")
    public abstract LiveData<List<CreditNote>> getCreditNoteByState(String state);

    @Query("SELECT * FROM  credit_note WHERE partnerId=:partnerId")
    public abstract LiveData<List<CreditNote>> getCreditNoteByPartnerId(long partnerId);

    @Query("SELECT * FROM  credit_note WHERE id=:id")
    public abstract LiveData<CreditNote> getCreditNoteById(long id);

    // CREDIT LINES

    @Insert
    public abstract void insertCreditLine(CreditLine creditLine);

    @Update
    public abstract void updateCreditLine(CreditLine creditLine);

    @Delete
    public abstract void deleteCreditLine(CreditLine creditLine);

    @Transaction
    @Query("DELETE  FROM  credit_line WHERE creditId=:creditId")
    public abstract void deleteCreditLinesByCreditId(long creditId);

    @Query("SELECT *  FROM  credit_line WHERE id=:id")
    public abstract LiveData<CreditLine> getCreditLineById(long id);

    @Query("SELECT * FROM  credit_line WHERE creditId=:creditId")
    public abstract LiveData<List<CreditLine>> getCreditLineByCreditId(long creditId);

    private boolean returnExists(long invoiceId) {

        List<InvoiceLine> lines = getInvoiceLinesByInvoiceId(invoiceId);
        if (lines == null)
            return false;
        for (InvoiceLine line : lines) {
            if (line.getType().equals(RETOUR)) {
                return true;
            }
        }
        return false;
    }
    //TRANSACTION OF INSERT CREDIT NOTE

    @Transaction
    public void insertCreditNoteAndCreditLine(CreditNote creditNote, CreditLine invoiceLine) {

        Sequence sequence = getSequence(CREDIT_NOTE);
        if (sequence == null) {
            return;
        }
        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String name = getNumberOfInvoice(prefix, counter);
        creditNote.setState(DRAFT);
        creditNote.setName(name);
        creditNote.setDate(new Date());

        updateCounterSequence(counter, CREDIT_NOTE);
        insertCreditNote(creditNote);
        insertCreditLine(invoiceLine);
    }

    @Transaction
    public void generateCreditNoteByInvoice(Invoice invoice) {

        if (!returnExists(invoice.getId())) {
            return;
        }
        CreditNote creditNote = new CreditNote();
        Sequence sequence = getSequence(CREDIT_NOTE);
        if (sequence == null) {
            return;
        }
        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String name = getNumberOfInvoice(prefix, counter);
        updateCounterSequence(counter, CREDIT_NOTE);

        creditNote.setState(DRAFT);
        creditNote.setName(name);
        creditNote.setDate(new Date());
        creditNote.setPartnerId(invoice.getPartnerId());

        final long creditId = insertCreditNote(creditNote);
        long invoiceId = invoice.getId();
        List<InvoiceLine> lines = getInvoiceLinesByInvoiceId(invoiceId);
        for (InvoiceLine line : lines) {
            if (line.getType().equals(RETOUR)) {
                int quantity = line.getQuantity();
                long productId = line.getProductId();
                CreditLine creditLine = new CreditLine(productId, quantity);
                creditLine.setCreditId(creditId);
                insertCreditLine(creditLine);
            }
        }

    }

    @Transaction
    public void generateCreditNoteByCreditNote(CreditNote oldCreditNote) {

        CreditNote newCreditNote = new CreditNote();
        Sequence sequence = getSequence(CREDIT_NOTE);
        if (sequence == null) {
            return;
        }
        String prefix = sequence.getPrefix();
        long counter = sequence.getCounter();
        String name = getNumberOfInvoice(prefix, counter);


        newCreditNote.setState(DRAFT);
        newCreditNote.setName(name);
        newCreditNote.setDate(new Date());
        updateCounterSequence(counter, CREDIT_NOTE);
        insertCreditNote(newCreditNote);
        final long creditId = newCreditNote.getId();
        long oldCreditId = oldCreditNote.getId();

        @SuppressWarnings("unchecked")
        List<CreditLine> lines = (List<CreditLine>) getCreditLineByCreditId(oldCreditId);

        for (CreditLine line : lines) {
            if (line.getQuantity1() < line.getQuantity0()) {
                int quantity = line.getQuantity0() - line.getQuantity1();
                long productId = line.getProductId();
                CreditLine creditLine = new CreditLine(productId, quantity);
                creditLine.setCreditId(creditId);
                insertCreditLine(creditLine);
            }
        }

    }


}
