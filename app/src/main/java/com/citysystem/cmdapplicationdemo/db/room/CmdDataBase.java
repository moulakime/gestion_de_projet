package com.citysystem.cmdapplicationdemo.db.room;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.citysystem.cmdapplicationdemo.db.daos.InvoiceDao;
import com.citysystem.cmdapplicationdemo.db.daos.PartnerDao;
import com.citysystem.cmdapplicationdemo.db.daos.ProductDao;
import com.citysystem.cmdapplicationdemo.db.daos.SequenceDao;
import com.citysystem.cmdapplicationdemo.db.daos.TariffDao;
import com.citysystem.cmdapplicationdemo.entities.Brand;
import com.citysystem.cmdapplicationdemo.entities.Category;
import com.citysystem.cmdapplicationdemo.entities.CreditLine;
import com.citysystem.cmdapplicationdemo.entities.CreditNote;
import com.citysystem.cmdapplicationdemo.entities.Inventory;
import com.citysystem.cmdapplicationdemo.entities.InventoryLine;
import com.citysystem.cmdapplicationdemo.entities.Invoice;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.entities.Product;
import com.citysystem.cmdapplicationdemo.entities.Sequence;
import com.citysystem.cmdapplicationdemo.entities.Tariff;
import com.citysystem.cmdapplicationdemo.entities.TariffLine;
import com.citysystem.cmdapplicationdemo.entities.TariffQuantities;
import com.citysystem.cmdapplicationdemo.entities.VendorOrder;
import com.citysystem.cmdapplicationdemo.entities.VendorOrderLine;
import com.citysystem.cmdapplicationdemo.utils.Converters;


@Database(entities = {Brand.class, Category.class, CreditNote.class, CreditLine.class,
        Invoice.class, InvoiceLine.class, Product.class, Partner.class, Sequence.class,
        Tariff.class, TariffLine.class, TariffQuantities.class, Inventory.class, InventoryLine.class,
        VendorOrder.class, VendorOrderLine.class}, exportSchema = false, version = 1)

@TypeConverters({Converters.class})

public abstract class CmdDataBase extends RoomDatabase {

    private static CmdDataBase instance;
    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized CmdDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CmdDataBase.class, "LocalDatabase.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;

    }

    public abstract ProductDao productDao();

    public abstract PartnerDao partnerDao();

    public abstract TariffDao tariffDao();

    public abstract InvoiceDao invoiceDao();

    public abstract SequenceDao sequenceDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {


        private PopulateDbAsyncTask(CmdDataBase db) {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            instance.tariffDao().insertTariff(new Tariff("default", false, true));
            instance.tariffDao().insertTariff(new Tariff("promo", true, false));
            instance.tariffDao().insertTariff(new Tariff("TN", false, false));


            instance.sequenceDao().insert(new Sequence("TEMPO_PARTNER", "CLTMP", 1, 6));
            instance.sequenceDao().insert(new Sequence("INVOICE", "IN", 1, 6));
            instance.sequenceDao().insert(new Sequence("CREDIT_NOT", "CRDN", 1, 6));
            instance.sequenceDao().insert(new Sequence("RETOUR", "RT", 1, 6));
            instance.productDao().insertBrand(new Brand("COCA"));
            instance.productDao().insertBrand(new Brand("YAWMI"));
            instance.productDao().insertBrand(new Brand("BIMO"));
            instance.productDao().insertBrand(new Brand("SANITAIRE"));
            instance.productDao().insertBrand(new Brand("SIDI ALI"));
            instance.productDao().insertBrand(new Brand("MARLBORO"));
            instance.productDao().insertCategory(new Category("BOISSONS"));
            instance.productDao().insertCategory(new Category("YOUGHOURT"));
            instance.productDao().insertCategory(new Category("BISCUIT"));
            instance.productDao().insertCategory(new Category("SANITAIRE"));
            instance.productDao().insertCategory(new Category("CIGARETTE"));
            return null;
        }

    }
}
