package com.citysystem.cmdapplicationdemo.utils;

import android.Manifest;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final String PARTNER = "partner";
    public static final String PRODUCT_DIALOG = "product";
    public static final String SELECTED_PARTNER = "selected_partner";
    public static final String IS_SAME_INVOICE = "is_same_invoice";
    public static final String LINE = "line";
    public static final String INVOICE_ID = "invoice_id";
    public static final long DEFAULT_INVOICE_ID = -1;
    public static final String INVOICE_VALIDATE = "invoice_id_validate";
    public static final String DONE = "done";
    public static final String VENTE = "Vente";
    public static final String INVOICE = "INVOICE";
    public static final String CREDIT_NOTE = "CREDIT_NOT";
    public static final String RETOUR = "Retour";
    public static final String DRAFT = "draft";
    public static final String TEMPO_PARTNER = "TEMPO_PARTNER";

    public static final boolean IS_TRUE = true;
    public static final boolean IS_DEFAULT = true;
    public static final boolean IS_PROMO = true;
    public static final boolean IS_FAVORITE = true;


    public static final int PERMISSION_ALL = 1;
    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };


}
