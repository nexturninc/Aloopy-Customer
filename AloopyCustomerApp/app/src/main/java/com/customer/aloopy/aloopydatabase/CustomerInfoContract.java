package com.customer.aloopy.aloopydatabase;

import android.provider.BaseColumns;

/**
 * Created by imbisibol on 8/27/2015.
 */
public final class CustomerInfoContract {

    public CustomerInfoContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CustomerInformation.TABLE_NAME + " (" +
                    CustomerInformation._ID + " INTEGER PRIMARY KEY," +
                    CustomerInformation.COLUMN_NAME_Customer_ID + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_Username + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_EmailAddress + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_FirstName + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_LastName + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_Code + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_QRCodeURL + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_AvatarURL + TEXT_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_DateCreated + DATE_TYPE + COMMA_SEP +
                    CustomerInformation.COLUMN_NAME_DateModified + DATE_TYPE +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CustomerInformation.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static abstract class CustomerInformation implements BaseColumns {
        public static final String TABLE_NAME = "CustomerInformation";
        public static final String COLUMN_NAME_Customer_ID = "CustomerId";
        public static final String COLUMN_NAME_Username = "Username";
        public static final String COLUMN_NAME_EmailAddress = "EmailAddress";
        public static final String COLUMN_NAME_FirstName = "FirstName";
        public static final String COLUMN_NAME_LastName = "LastName";
        public static final String COLUMN_NAME_Code = "Code";
        public static final String COLUMN_NAME_DateCreated = "DateCreated";
        public static final String COLUMN_NAME_DateModified = "DateModified";
        public static final String COLUMN_NAME_QRCodeURL = "QRCodeURL";
        public static final String COLUMN_NAME_AvatarURL = "AvatarURL";

    }
}
