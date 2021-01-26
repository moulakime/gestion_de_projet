package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "product",
        foreignKeys = {
                @ForeignKey(entity = Brand.class,
                        parentColumns = "id",
                        childColumns = "brandId"),
                @ForeignKey(entity = Category.class,
                        parentColumns = "id",
                        childColumns = "categoryId")
        }, indices = {@Index("brandId"), @Index("categoryId")})
public class Product implements Parcelable {
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private long brandId;
    private long categoryId;
    private boolean promo;
    private double priceUnit;
    private String code;
    private String commercialName;
    private boolean favorite;


    public Product(String name, long brandId, long categoryId, boolean promo, double priceUnit,
                   String code, String commercialName, boolean favorite) {
        this.name = name;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.promo = promo;
        this.priceUnit = priceUnit;
        this.code = code;
        this.commercialName = commercialName;
        this.favorite = favorite;
    }

    protected Product(Parcel in) {
        id = in.readLong();
        name = in.readString();
        brandId = in.readLong();
        categoryId = in.readLong();
        promo = in.readByte() != 0;
        priceUnit = in.readDouble();
        code = in.readString();
        commercialName = in.readString();
        favorite = in.readByte() != 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getBrandId() {
        return brandId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public boolean isPromo() {
        return promo;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public String getCode() {
        return code;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public boolean isFavorite() {
        return favorite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(brandId);
        dest.writeLong(categoryId);
        dest.writeByte((byte) (promo ? 1 : 0));
        dest.writeDouble(priceUnit);
        dest.writeString(code);
        dest.writeString(commercialName);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }
}
