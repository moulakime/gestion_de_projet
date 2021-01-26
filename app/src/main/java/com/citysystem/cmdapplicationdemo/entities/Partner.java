package com.citysystem.cmdapplicationdemo.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "partner",
        foreignKeys =
        @ForeignKey(entity = Tariff.class,
                parentColumns = "id",
                childColumns = "tariffId"), indices = @Index("tariffId"))
public class Partner implements Parcelable {

    public static final Creator<Partner> CREATOR = new Creator<Partner>() {
        @Override
        public Partner createFromParcel(Parcel in) {
            return new Partner(in);
        }

        @Override
        public Partner[] newArray(int size) {
            return new Partner[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String partnerName;
    private String phone;
    private String email;
    private String patent;
    private String ice;
    private double latitude;
    private double longitude;
    private String codeQr;
    private String externalCode;
    private boolean tempo;
    private Date dateCreation;
    private long tariffId;

    public Partner(String partnerName, String phone, String email, String patent, String ice, double latitude,
                   double longitude, String codeQr) {
        this.partnerName = partnerName;
        this.phone = phone;
        this.email = email;
        this.patent = patent;
        this.ice = ice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.codeQr = codeQr;
    }

    @Ignore
    public Partner(String partnerName, String phone, String email, String patent, String ice, String codeQr) {
        this.partnerName = partnerName;
        this.phone = phone;
        this.email = email;
        this.patent = patent;
        this.ice = ice;
        this.codeQr = codeQr;
    }

    protected Partner(Parcel in) {
        id = in.readLong();
        partnerName = in.readString();
        phone = in.readString();
        email = in.readString();
        patent = in.readString();
        ice = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        codeQr = in.readString();
        externalCode = in.readString();
        tempo = in.readByte() != 0;
        dateCreation = new Date(in.readLong());
        tariffId = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatent() {
        return patent;
    }

    public void setPatent(String patent) {
        this.patent = patent;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCodeQr() {
        return codeQr;
    }

    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public boolean isTempo() {
        return tempo;
    }

    public void setTempo(boolean tempo) {
        this.tempo = tempo;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public long getTariffId() {
        return tariffId;
    }

    public void setTariffId(long tariffId) {

        this.tariffId = tariffId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(partnerName);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(patent);
        dest.writeString(ice);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(codeQr);
        dest.writeString(externalCode);
        dest.writeByte((byte) (tempo ? 1 : 0));
        dest.writeLong(dateCreation.getTime());
        dest.writeLong(tariffId);
    }
}
