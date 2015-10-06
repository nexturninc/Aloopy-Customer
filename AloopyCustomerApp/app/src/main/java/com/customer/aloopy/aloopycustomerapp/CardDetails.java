package com.customer.aloopy.aloopycustomerapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;

/**
 * Created by Emporia-003 on 10/2/2015.
 */
public class CardDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_details);

        Intent intent = getIntent();
        String cardId = intent.getStringExtra(getString(R.string.EXTRA_CardDetail_Id));


    }

    private void LoadCardData(String id)
    {
        AloopySQLHelper helper = AloopySQLHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //GET DATA
    }
}
