package com.customer.aloopy.aloopycustomerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerStampSetContract;

/**
 * Created by imbisibol on 9/7/2015.
 */
public class StampDetails extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stamp_details);

        Intent intent = getIntent();
        String stampId = intent.getStringExtra(getString(R.string.EXTRA_StampDetail_Id));

        LoadStampData(stampId);
    }

    private void LoadStampData(String id)
    {
        AloopySQLHelper helper = AloopySQLHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //GET DATA FROM DB
        String[] projection = {
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Created,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Title,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_ID,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Text_Color,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_StampSet_ID,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Store_StampSet_ID,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color2,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Modified,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Id,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_H,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_V,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Name,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Reward_Image,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_QR_Code,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Count,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Icon,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Title,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Volume,
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_End_Date
        };

        Cursor c = db.query(
                CustomerStampSetContract.CustomerStampSetInformation.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_StampSet_ID + " = ?",                                // The columns for the WHERE clause
                new String[]{id},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();

        String textColor = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Text_Color));
        String bgColor2 = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color2));

        ((TextView)findViewById(R.id.txtStampDetailTitle)).setText(c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Title)));
        ((TextView)findViewById(R.id.txtStampDetailTitle)).setTextColor(Integer.parseInt(bgColor2, 16) + 0xFF000000);

        ((TextView)findViewById(R.id.lblLastUpdated)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        ((TextView)findViewById(R.id.lblLastUpdatedDate)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        String lastUpdatedDate = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Modified));
        if(lastUpdatedDate != null && !lastUpdatedDate.equals("null")) {
            ((TextView) findViewById(R.id.lblLastUpdatedDate)).setText(lastUpdatedDate);
        }

        ((TextView)findViewById(R.id.lblValidUntil)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        ((TextView)findViewById(R.id.lblValidUntilDate)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        String endDate = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_End_Date));
        if(endDate != null && !endDate.equals("null")) {
            ((TextView) findViewById(R.id.lblValidUntilDate)).setText(endDate);
        }

        Common.getImageLoader(null).displayImage(c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_V)), ((ImageView) findViewById(R.id.imgStampDetailMerchantLogo)));
        Common.getImageLoader(null).displayImage(c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_QR_Code)), ((ImageView) findViewById(R.id.imgQRCode)));
        Common.getImageLoader(null).displayImage(c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Reward_Image)), ((ImageView) findViewById(R.id.imgRewardImage)));

        (findViewById(R.id.dvStampDetailBody)).setBackgroundColor(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color)), 16) + 0xFF000000);
        (findViewById(R.id.dvStampBar)).setBackgroundColor(Integer.parseInt(bgColor2, 16) + 0xFF000000);

        ImageView stamp01 = ((ImageView)findViewById(R.id.imgStamp1));
        ImageView stamp02 = ((ImageView)findViewById(R.id.imgStamp2));
        ImageView stamp03 = ((ImageView)findViewById(R.id.imgStamp3));
        ImageView stamp04 = ((ImageView)findViewById(R.id.imgStamp4));
        ImageView stamp05 = ((ImageView)findViewById(R.id.imgStamp5));
        ImageView stamp06 = ((ImageView)findViewById(R.id.imgStamp6));
        ImageView stamp07 = ((ImageView)findViewById(R.id.imgStamp7));
        ImageView stamp08 = ((ImageView)findViewById(R.id.imgStamp8));
        ImageView stamp09 = ((ImageView)findViewById(R.id.imgStamp9));
        ImageView stamp10 = ((ImageView)findViewById(R.id.imgStamp10));

        String stampIconURL = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Icon));
        if (stampIconURL != null && stampIconURL != "") {
            Common.getImageLoader(null).displayImage(stampIconURL, stamp01);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp02);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp03);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp04);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp05);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp06);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp07);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp08);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp09);
            Common.getImageLoader(null).displayImage(stampIconURL, stamp10);
        }

        Integer stampVolume = c.getInt(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Volume));
        ((TextView)findViewById(R.id.lblStampCount)).setText(stampVolume.toString());
        ((TextView)findViewById(R.id.lblStampCount)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        ((TextView)findViewById(R.id.lblStamps)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);

        if(stampVolume <= 8) {
            stamp10.setVisibility(View.GONE);
            stamp09.setVisibility(View.GONE);
        }
        if(stampVolume <= 5) {
            stamp08.setVisibility(View.GONE);
            stamp07.setVisibility(View.GONE);
            stamp06.setVisibility(View.GONE);
        }
        if(stampVolume <= 3) {
            stamp05.setVisibility(View.GONE);
            stamp04.setVisibility(View.GONE);
        }

        Integer stampCount = c.getInt(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Count));
        ((TextView)findViewById(R.id.lblClaimsCount)).setText(stampCount.toString());
        ((TextView)findViewById(R.id.lblClaimsCount)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        ((TextView)findViewById(R.id.lblClaims)).setTextColor(Integer.parseInt(textColor, 16) + 0xFF000000);
        if(stampCount > 0)
            stamp01.setAlpha((float) 1);
        if(stampCount > 1)
            stamp02.setAlpha((float) 1);
        if(stampCount > 2)
            stamp03.setAlpha((float) 1);
        if(stampCount > 3)
            stamp04.setAlpha((float) 1);
        if(stampCount > 4)
            stamp05.setAlpha((float) 1);
        if(stampCount > 5)
            stamp06.setAlpha((float) 1);
        if(stampCount > 6)
            stamp07.setAlpha((float) 1);
        if(stampCount > 7)
            stamp08.setAlpha((float) 1);
        if(stampCount > 8)
            stamp09.setAlpha((float) 1);
        if(stampCount > 9)
            stamp10.setAlpha((float) 1);



        c.close();
    }

}
