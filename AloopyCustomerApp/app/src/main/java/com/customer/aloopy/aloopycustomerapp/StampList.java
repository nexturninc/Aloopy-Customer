package com.customer.aloopy.aloopycustomerapp;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerStampSetContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by imbisibol on 8/25/2015.
 */
public class StampList extends Fragment {

    public String UserID;
    public GridView gridview;
    public ArrayList<CustomerStampSetContract> stampData = new ArrayList<CustomerStampSetContract>();
    public StampSetAdapter stampSetAdapter;

    private StampSetTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View mStampListBody;

    public StampList() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.stamp_list, container, false);

        mProgressBar = ((ProgressBar)rootView.findViewById(R.id.login_progress));
        mStampListBody = (rootView.findViewById(R.id.dvStampListBody));
        Button btnRefresh = (Button)rootView.findViewById(R.id.btnRefresh);

        //GET SHARED PREFERENCES
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        UserID = mSettings.getString(getActivity().getString(R.string.SHARE_PREF_UserId), null);

        gridview = (GridView)rootView.findViewById(R.id.gvStampList);

        GetStamps(false);

        //GET DATA
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), stampData.get(position).CustomerStampSetId, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getBaseContext(), StampDetails.class);
                intent.putExtra(getString(R.string.EXTRA_StampDetail_Id), stampData.get(position).CustomerStampSetId);
                startActivity(intent);

            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetStamps(true);
            }
        });


        return rootView;
    }

    public static StampList newInstance() {
        StampList fragment = new StampList();
        return fragment;
    }

    public void GetStamps(boolean forceAPIQuery) {

        if (mAuthTask != null) {
            return;
        }

        showProgress(true);

        AloopySQLHelper helper = AloopySQLHelper.getInstance(this.getActivity());
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
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(forceAPIQuery &&
                !Common.GetInternetConnectivity((ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))) {
            showProgress(false);
            Toast.makeText(getActivity().getBaseContext(), getString(R.string.message_Internet_Required), Toast.LENGTH_SHORT).show();
        }
        else {
            if (forceAPIQuery
                    || (Common.GetInternetConnectivity((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)))
                        && c.getCount() == 0) {

                //GET FROM API

                mAuthTask = new StampSetTask(UserID);
                mAuthTask.execute((Void) null);

            }
            else //GET FROM DATABASE
            {

                stampData = new ArrayList<CustomerStampSetContract>();

                while (c.moveToNext()) {

                    CustomerStampSetContract stampItem = new CustomerStampSetContract();
                    stampItem.CustomerId = UserID;
                    stampItem.CustomerStampSetId = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_StampSet_ID));
                    stampItem.StampTitle = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Title));
                    stampItem.StoreStampSetId = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Store_StampSet_ID));
                    stampItem.StampVolume = c.getInt(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Volume));
                    stampItem.BackgroundColor = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color));
                    stampItem.BackgroundColor2 = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color2));
                    stampItem.TextColor = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Text_Color));
                    stampItem.StampCount = c.getInt(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Count));
                    stampItem.MerchantId = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Id));
                    stampItem.MerchantName = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Name));
                    stampItem.MerchantLogoH = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_H));
                    stampItem.MerchantLogoV = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_V));
                    stampItem.StampIcon = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Icon));
                    stampItem.RewardImage = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Reward_Image));
                    stampItem.QRCode = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_QR_Code));
                    stampItem.DateCreated = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Created));
                    stampItem.DateModified = c.getString(c.getColumnIndexOrThrow(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Modified));

                    stampData.add(stampItem);
                }

                stampSetAdapter = new StampSetAdapter(getActivity(), R.layout.stamp_set_row, stampData);
                gridview.setAdapter(stampSetAdapter);

                showProgress(false);
            }
        }
    }

    public class StampSetTask extends AsyncTask<Void, Void, Boolean> {

        private final String CustomerId;

        StampSetTask(String customerId) {
            CustomerId = customerId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean loginSuccess = false;
            String userId = "";
            String userDisplay = "";
            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getString(R.string.AloopyAPIURL));
                jsonResponse = comm.GetAPI("/aloopy/customerstampset/" + CustomerId);


                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("success");

                    if (strSuccess == "true") {

                        JSONArray stampSetArray = jsonResponse.getJSONArray("stampSet");

                        if(stampSetArray != null) {

                            stampData = new ArrayList<CustomerStampSetContract>();

                            Context cont = getActivity().getBaseContext();

                            // Gets the data repository in write mode
                            AloopySQLHelper helper = AloopySQLHelper.getInstance(getActivity());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            try {
                                db.delete(CustomerStampSetContract.CustomerStampSetInformation.TABLE_NAME, null, null);
                            }
                            catch (Exception ex)
                            {

                            }

                            for(int ctr=0;ctr<stampSetArray.length();ctr++) {
                                CustomerStampSetContract stampItem = new CustomerStampSetContract();
                                stampItem.CustomerId = CustomerId;
                                stampItem.CustomerStampSetId = stampSetArray.getJSONObject(ctr).getString("customerStampSetID");
                                stampItem.StampTitle = stampSetArray.getJSONObject(ctr).getString("stampName");
                                stampItem.StoreStampSetId = stampSetArray.getJSONObject(ctr).getString("storeStampSetID");
                                stampItem.StampVolume = stampSetArray.getJSONObject(ctr).getInt("stampVolume");
                                stampItem.BackgroundColor = stampSetArray.getJSONObject(ctr).getString("backgroundColor");
                                stampItem.BackgroundColor2 = stampSetArray.getJSONObject(ctr).getString("backgroundColor2");
                                stampItem.TextColor = stampSetArray.getJSONObject(ctr).getString("textColor");
                                stampItem.StampCount = stampSetArray.getJSONObject(ctr).getInt("stampCount");
                                stampItem.MerchantId = stampSetArray.getJSONObject(ctr).getString("merchantID");
                                stampItem.MerchantName = stampSetArray.getJSONObject(ctr).getString("merchantName");
                                stampItem.MerchantLogoH = stampSetArray.getJSONObject(ctr).getJSONObject("horizontalMerchantLogo3x").getString("filePath");
                                stampItem.MerchantLogoV = stampSetArray.getJSONObject(ctr).getJSONObject("verticalMerchantLogo3x").getString("filePath");
                                stampItem.StampIcon = stampSetArray.getJSONObject(ctr).getJSONObject("stampIcon3x").getString("filePath");
                                stampItem.RewardImage = stampSetArray.getJSONObject(ctr).getJSONObject("rewardImage3x").getString("filePath");
                                stampItem.QRCode = stampSetArray.getJSONObject(ctr).getJSONObject("qrCodeImage3x").getString("filePath");
                                stampItem.DateCreated = stampSetArray.getJSONObject(ctr).getString("dateCreated");
                                stampItem.DateModified = stampSetArray.getJSONObject(ctr).getString("dateModified");

                                stampData.add(stampItem);


                                //SAVE TO DATABASE
                                ContentValues values = new ContentValues();
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_ID, stampItem.CustomerId);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_StampSet_ID, stampItem.CustomerStampSetId);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Title, stampItem.StampTitle);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Store_StampSet_ID, stampItem.StoreStampSetId);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Volume, stampItem.StampVolume);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color, stampItem.BackgroundColor);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Background_Color2, stampItem.BackgroundColor2);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Text_Color, stampItem.TextColor);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Count, stampItem.StampCount);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Id, stampItem.MerchantId);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Name, stampItem.MerchantName);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_H, stampItem.MerchantLogoH);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Merchant_Logo_V, stampItem.MerchantLogoV);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Stamp_Icon, stampItem.StampIcon);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Reward_Image, stampItem.RewardImage);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_QR_Code, stampItem.QRCode);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Created, stampItem.DateCreated);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Date_Modified, stampItem.DateModified);
                                values.put(CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_End_Date, stampItem.EndDate);

                                long newRowId;
                                newRowId = db.insert(
                                        CustomerStampSetContract.CustomerStampSetInformation.TABLE_NAME,
                                        CustomerStampSetContract.CustomerStampSetInformation.COLUMN_NAME_Customer_ID,
                                        values);
                            }


                            db.close();

                        }


                        loginSuccess = true;
                    }
                }

            } catch (Exception ex) {

                String abc = ex.getMessage();

            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                stampSetAdapter = new StampSetAdapter(getActivity(), R.layout.stamp_set_row, stampData);
                gridview.setAdapter(stampSetAdapter);

                //finish();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getBaseContext());
                dialog.setTitle("Message Alert");
                dialog.setMessage("Failed tor retrieve Stamp List!");
                dialog.show();
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            mStampListBody.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mStampListBody.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mStampListBody.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
