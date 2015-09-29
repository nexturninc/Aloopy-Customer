package com.customer.aloopy.aloopydatabase;

import android.provider.BaseColumns;

/**
 * Created by Emporia-003 on 9/14/2015.
 */
public class CustomerLoyaltyCardContract {

    public String CardID;
    public String CardTitle;
    public int CardVolume;
    public String DateExpiration;
    public String CardPrice;
    public String MerchantID;
    public String CardImage;
    public String QRCode;
    public int TotalPoints;
    public String CustomerID;
    public String CustomerCardID;
    public String DateCreated;

    public CustomerLoyaltyCardContract()
    {

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CustomerLoyaltyCardInformation.TABLE_NAME + " (" +
                    CustomerLoyaltyCardInformation._ID + " INTEGER PRIMARY KEY," +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Card_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Title + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Volume + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Date_Expiration + DATE_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Price + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Merchant_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Image + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_QR_Code + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Total_Points + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Customer_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Customer_Card_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerLoyaltyCardInformation.COLUMN_NAME_Date_Created + DATE_TYPE +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CustomerLoyaltyCardInformation.TABLE_NAME;

    public static abstract class CustomerLoyaltyCardInformation implements BaseColumns
    {
        public static final String TABLE_NAME = "CustomerLoyaltyCardInformation";
        public static final String COLUMN_NAME_Card_ID = "CardID";
        public static final String COLUMN_NAME_Card_Title = "CardTitle";
        public static final String COLUMN_NAME_Card_Volume = "CardVolume";
        public static final String COLUMN_NAME_Date_Expiration = "DateExpiration";
        public static final String COLUMN_NAME_Card_Price = "CardPrice";
        public static final String COLUMN_NAME_Merchant_ID = "MerchantID";
        public static final String COLUMN_NAME_Card_Image = "CardImage";
        public static final String COLUMN_NAME_QR_Code = "QRCode";
        public static final String COLUMN_NAME_Total_Points = "TotalPoints";
        public static final String COLUMN_NAME_Customer_ID = "CustomerID";
        public static final String COLUMN_NAME_Customer_Card_ID = "CustomerCardID";
        public static final String COLUMN_NAME_Date_Created = "DateCreated";
    }
}
