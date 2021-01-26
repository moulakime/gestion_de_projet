package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "invoice_line",
        foreignKeys = {
                @ForeignKey(entity = Invoice.class,
                        parentColumns = "id",
                        childColumns = "invoiceId",
                        onDelete = CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId"
                )
        }, indices = {@Index("invoiceId"), @Index("productId")})
public class InvoiceLine implements Parcelable {
    public static final Creator<InvoiceLine> CREATOR = new Creator<InvoiceLine>() {
        @Override
        public InvoiceLine createFromParcel(Parcel in) {
            return new InvoiceLine(in);
        }

        @Override
        public InvoiceLine[] newArray(int size) {
            return new InvoiceLine[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long invoiceId;
    private long productId;
    private double priceUnit;
    private int quantity;
    private String type;
    private String name;
    @Ignore
    private long gratisProductId;
    @Ignore
    private int gratisQuantity;

    @Ignore
    public InvoiceLine(long productId, double priceUnit, int quantity, String type, long gratisProductId, int gratisQuantity) {
        this.productId = productId;
        this.priceUnit = priceUnit;
        this.quantity = quantity;
        this.type = type;
        this.gratisProductId = gratisProductId;
        this.gratisQuantity = gratisQuantity;
    }

    public InvoiceLine(long productId, double priceUnit, int quantity, String type) {
        this.productId = productId;
        this.priceUnit = priceUnit;
        this.quantity = quantity;
        this.type = type;
    }

    private InvoiceLine(Parcel in) {
        invoiceId = in.readLong();
        productId = in.readLong();
        name = in.readString();
        priceUnit = in.readDouble();
        quantity = in.readInt();
        type = in.readString();
        gratisProductId = in.readLong();
        gratisQuantity = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getProductId() {
        return productId;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public long getGratisProductId() {
        return gratisProductId;
    }

    public void setGratisProductId(long gratisProductId) {
        this.gratisProductId = gratisProductId;
    }

    public int getGratisQuantity() {
        return gratisQuantity;
    }

    public void setGratisQuantity(int gratisQuantity) {
        this.gratisQuantity = gratisQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(invoiceId);
        dest.writeLong(productId);
        dest.writeDouble(priceUnit);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(type);
        dest.writeLong(gratisProductId);
        dest.writeInt(gratisQuantity);
    }
}
