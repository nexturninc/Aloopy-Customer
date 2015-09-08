package com.customer.aloopy.aloopydatabase;

import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by imbisibol on 9/1/2015.
 */
public class CustomerStampSetContract {

    public String CustomerId;
    public String CustomerStampSetId;
    public String StoreStampSetId;
    public String StampTitle;
    public int StampVolume;
    public String BackgroundColor;
    public String BackgroundColor2;
    public String TextColor;
    public int StampCount;
    public String MerchantId;
    public String MerchantName;
    public String MerchantLogoH;
    public String MerchantLogoV;
    public String StampIcon;
    public String RewardImage;
    public String QRCode;
    public String DateCreated;
    public String DateModified;
    public String EndDate;

    public CustomerStampSetContract()
    {

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CustomerStampSetInformation.TABLE_NAME + " (" +
                    CustomerStampSetInformation._ID + " INTEGER PRIMARY KEY," +
                    CustomerStampSetInformation.COLUMN_NAME_Customer_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Customer_StampSet_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Store_StampSet_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Stamp_Title + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Stamp_Volume + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Background_Color + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Background_Color2 + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Text_Color + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Stamp_Count + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Merchant_Id + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Merchant_Name + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_H + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_V + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Stamp_Icon + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Reward_Image + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_QR_Code + TEXT_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Date_Created + DATE_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_Date_Modified + DATE_TYPE + COMMA_SEP +
                    CustomerStampSetInformation.COLUMN_NAME_End_Date + DATE_TYPE +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CustomerStampSetInformation.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class CustomerStampSetInformation implements BaseColumns{
        public static final String TABLE_NAME = "CustomerStampSetInformation";
        public static final String COLUMN_NAME_Customer_ID = "CustomerId";
        public static final String COLUMN_NAME_Customer_StampSet_ID = "CustomerStampSetId";
        public static final String COLUMN_NAME_Store_StampSet_ID = "StoreStampSetId";
        public static final String COLUMN_NAME_Stamp_Title = "StampTitle";
        public static final String COLUMN_NAME_Stamp_Volume = "StampVolume";
        public static final String COLUMN_NAME_Background_Color = "BackgroundColor";
        public static final String COLUMN_NAME_Background_Color2 = "BackgroundColor2";
        public static final String COLUMN_NAME_Text_Color = "TextColor";
        public static final String COLUMN_NAME_Stamp_Count = "StampCount";
        public static final String COLUMN_NAME_Merchant_Id = "MerchantId";
        public static final String COLUMN_NAME_Merchant_Name = "MerchantName";
        public static final String COLUMN_NAME_Merchant_Logo_H = "MerchantLogoH";
        public static final String COLUMN_NAME_Merchant_Logo_V = "MerchantLogoV";
        public static final String COLUMN_NAME_Stamp_Icon = "StampIcon";
        public static final String COLUMN_NAME_Reward_Image = "RewardImage";
        public static final String COLUMN_NAME_QR_Code = "QRCode";
        public static final String COLUMN_NAME_Date_Created = "DateCreated";
        public static final String COLUMN_NAME_Date_Modified = "DateModified";
        public static final String COLUMN_NAME_End_Date = "EndDate";

    }

}
