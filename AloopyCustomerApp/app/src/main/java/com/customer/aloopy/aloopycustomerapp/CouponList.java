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
import com.customer.aloopy.aloopydatabase.CustomerCouponContract;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by imbisibol on 9/15/2015.
 */
public class CouponList extends Fragment {

    public String UserID;
    public GridView gridview;
    public ArrayList<CustomerCouponContract> resultData = new ArrayList<CustomerCouponContract>();
    public CouponAdapter couponAdapter;

    private CouponTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View mStampListBody;

    public CouponList() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.coupon_list, container, false);

        mProgressBar = ((ProgressBar)rootView.findViewById(R.id.login_progress));
        mStampListBody = (rootView.findViewById(R.id.dvCouponListBody));
        Button btnRefresh = (Button)rootView.findViewById(R.id.btnRefresh);

        //GET SHARED PREFERENCES
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        UserID = mSettings.getString(getActivity().getString(R.string.SHARE_PREF_UserId), null);

        gridview = (GridView)rootView.findViewById(R.id.gvCouponList);

        GetCoupons(false);

        //GET DATA
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //Toast.makeText(getActivity().getBaseContext(), stampData.get(position).CustomerStampSetId, Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getActivity().getBaseContext(), StampDetails.class);
                //intent.putExtra(getString(R.string.EXTRA_StampDetail_Id), stampData.get(position).CustomerStampSetId);
                //startActivity(intent);

            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCoupons(true);
            }
        });


        return rootView;
    }

    public static CouponList newInstance() {
        CouponList fragment = new CouponList();
        return fragment;
    }

    private void GetCoupons(boolean forceAPIQuery){

        if (mAuthTask != null) {
            return;
        }

        showProgress(true);

        AloopySQLHelper helper = AloopySQLHelper.getInstance(this.getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        //GET DATA FROM DB
        String[] projection = {
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Customer_Coupon_ID,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Coupon_ID,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Coupon_Name,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Start_Date,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_End_Date,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Store_Id,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Store_Coupon_Id,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Main_Banner_Image,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_QR_Code_Image,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Claimed,
                CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Date_Created
        };

        Cursor c = db.query(
                CustomerCouponContract.CustomerCouponInformation.TABLE_NAME,  // The table to query
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

                mAuthTask = new CouponTask(UserID);
                mAuthTask.execute((Void) null);

            }
            else //GET FROM DATABASE
            {

                resultData = new ArrayList<CustomerCouponContract>();

                while (c.moveToNext()) {

                    CustomerCouponContract resultItem = new CustomerCouponContract();
                    resultItem.QRCodeImage = c.getString(c.getColumnIndexOrThrow(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_QR_Code_Image));
                    resultItem.MainBannerImage = c.getString(c.getColumnIndexOrThrow(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Main_Banner_Image));

                    resultData.add(resultItem);
                }

                couponAdapter = new CouponAdapter(getActivity(), R.layout.coupon_row, resultData);
                gridview.setAdapter(couponAdapter);

                showProgress(false);
            }
        }
    }

    public class CouponTask extends AsyncTask<Void, Void, Boolean> {

        private final String CustomerId;

        CouponTask(String customerId) {
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
                jsonResponse = comm.GetAPI("/aloopy/customercoupon/?customerId=" + CustomerId + "&beaconId=00000000-0000-0000-0000-000000000000");


                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("success");

                    if (strSuccess == "true") {

                        JSONArray stampSetArray = jsonResponse.getJSONArray("couponList");

                        if(stampSetArray != null) {

                            resultData = new ArrayList<CustomerCouponContract>();

                            Context cont = getActivity().getBaseContext();

                            // Gets the data repository in write mode
                            AloopySQLHelper helper = AloopySQLHelper.getInstance(getActivity());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            try {
                                db.delete(CustomerCouponContract.CustomerCouponInformation.TABLE_NAME, null, null);
                            }
                            catch (Exception ex)
                            {

                            }

                            for(int ctr=0;ctr<stampSetArray.length();ctr++) {
                                CustomerCouponContract resultItem = new CustomerCouponContract();
                                resultItem.MainBannerImage = stampSetArray.getJSONObject(ctr).getJSONObject("mainBannerImage3x").getString("imageURL");
                                resultItem.QRCodeImage = stampSetArray.getJSONObject(ctr).getJSONObject("qrCodeImage_3x").getString("imageFileName");

                                resultData.add(resultItem);


                                //SAVE TO DATABASE
                                ContentValues values = new ContentValues();
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Customer_Coupon_ID, stampSetArray.getJSONObject(ctr).getString("customerCouponID"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Coupon_ID, stampSetArray.getJSONObject(ctr).getString("couponID"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Coupon_Name, stampSetArray.getJSONObject(ctr).getString("couponName"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Start_Date, stampSetArray.getJSONObject(ctr).getString("startDate"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_End_Date, stampSetArray.getJSONObject(ctr).getString("endDate"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Store_Id, stampSetArray.getJSONObject(ctr).getString("storeID"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Store_Coupon_Id, stampSetArray.getJSONObject(ctr).getString("storeCouponID"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Main_Banner_Image, stampSetArray.getJSONObject(ctr).getJSONObject("mainBannerImage3x").getString("filePath"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_QR_Code_Image, stampSetArray.getJSONObject(ctr).getJSONObject("qrCodeImage_3x").getString("imageFileName"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Claimed, stampSetArray.getJSONObject(ctr).getString("claimed"));
                                values.put(CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Date_Created, stampSetArray.getJSONObject(ctr).getString("dateCreated"));

                                long newRowId;
                                newRowId = db.insert(
                                        CustomerCouponContract.CustomerCouponInformation.TABLE_NAME,
                                        CustomerCouponContract.CustomerCouponInformation.COLUMN_NAME_Customer_Coupon_ID,
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

                couponAdapter = new CouponAdapter(getActivity(), R.layout.coupon_row, resultData);
                gridview.setAdapter(couponAdapter);

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
