package com.customer.aloopy.aloopycustomerapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerInfoContract;

/**
 * Created by imbisibol on 8/27/2015.
 */
public class CustomerProfile extends Fragment {

    TextView txtFirstName;
    TextView txtLastName;

    public CustomerProfile() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customer_profile, container, false);

        AloopySQLHelper helper = AloopySQLHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        //INITIALIZE CONTROLS
        txtFirstName = (TextView)rootView.findViewById(R.id.txtFirstName);
        txtLastName = (TextView)rootView.findViewById(R.id.txtLastName);

        //GET DATA FROM DB
        String[] projection = {
                CustomerInfoContract.CustomerInformation._ID,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_Code,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_Username,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_FirstName,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_LastName,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_EmailAddress,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_AvatarURL,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_QRCodeURL,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_DateModified
        };

        Cursor c = db.query(
                CustomerInfoContract.CustomerInformation.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToNext();

        txtFirstName.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_FirstName)));
        txtLastName.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_LastName)));

        db.close();

        return rootView;
    }

    public static CustomerProfile newInstance() {
        CustomerProfile fragment = new CustomerProfile();

        return fragment;
    }
}
