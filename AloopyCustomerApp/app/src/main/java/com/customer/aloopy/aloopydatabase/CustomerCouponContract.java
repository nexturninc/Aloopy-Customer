package com.customer.aloopy.aloopydatabase;

import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by imbisibol on 9/15/2015.
 */
public final class CustomerCouponContract {

    public String CouponName;
    public String QRCodeImage;
    public String MainBannerImage;
    public String Claimed;
    public String StartDate;
    public String EndDate;
    public String DateCreated;
    public String DateModified;

    public CustomerCouponContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CustomerCouponInformation.TABLE_NAME + " (" +
                    CustomerCouponInformation._ID + " INTEGER PRIMARY KEY," +
                    CustomerCouponInformation.COLUMN_NAME_Customer_Coupon_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Coupon_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Coupon_Name + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Start_Date + DATE_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_End_Date + DATE_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Store_Id + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Store_Coupon_Id + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Main_Banner_Image + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_QR_Code_Image + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Claimed + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Date_Created + TEXT_TYPE + COMMA_SEP +
                    CustomerCouponInformation.COLUMN_NAME_Date_Modified + TEXT_TYPE +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CustomerCouponInformation.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class CustomerCouponInformation implements BaseColumns {
        public static final String TABLE_NAME = "CustomerCouponInformation";
        public static final String COLUMN_NAME_Customer_Coupon_ID = "CustomerCouponId";
        public static final String COLUMN_NAME_Coupon_ID = "CouponId";
        public static final String COLUMN_NAME_Coupon_Name = "CouponName";
        public static final String COLUMN_NAME_Start_Date = "StartDate";
        public static final String COLUMN_NAME_End_Date = "EndDate";
        public static final String COLUMN_NAME_Store_Id = "StoreId";
        public static final String COLUMN_NAME_Store_Coupon_Id = "StoreCouponId";
        public static final String COLUMN_NAME_Main_Banner_Image = "MainBannerImage";
        public static final String COLUMN_NAME_QR_Code_Image = "QRCodeImage";
        public static final String COLUMN_NAME_Claimed = "Claimed";
        public static final String COLUMN_NAME_Date_Created = "DateCreated";
        public static final String COLUMN_NAME_Date_Modified = "DateModified";

    }
}

