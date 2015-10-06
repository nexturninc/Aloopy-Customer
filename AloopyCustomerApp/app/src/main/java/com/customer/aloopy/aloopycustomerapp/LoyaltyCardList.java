package com.customer.aloopy.aloopycustomerapp;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerLoyaltyCardContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emporia-003 on 9/14/2015.
 */
public class LoyaltyCardList extends Fragment{

    public String UserID;
    public GridView gridView;
    public ArrayList<CustomerLoyaltyCardContract> cardData = new ArrayList<CustomerLoyaltyCardContract>();
    public CardSetAdapter cardSetAdapter;
    private CardSetTask mAuthTask = null;

    public LoyaltyCardList()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.card_list, container, false);

        //GET SHARED PREFERENCES
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        UserID = mSettings.getString("UserId", null);

        gridView = (GridView)rootView.findViewById(R.id.gvCardList);

        GetCards();

        //GET DATA
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), cardData.get(position).CustomerCardID, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getBaseContext(), StampDetails.class);
                intent.putExtra(getString(R.string.EXTRA_CardDetail_Id), cardData.get(position).CustomerCardID);
                startActivity(intent);

            }
        });

        return rootView;
    }

    public static LoyaltyCardList newInstance()
    {
        LoyaltyCardList fragment = new LoyaltyCardList();
        return fragment;
    }

    public void GetCards()
    {
        if(mAuthTask != null)
        {
            return;
        }
        showProgress(true);
        mAuthTask = new CardSetTask(UserID);
        mAuthTask.execute((Void) null);
    }

    public class CardSetTask extends AsyncTask<Void, Void, Boolean>
    {
        private final String CustomerId;

        CardSetTask(String customerId)
        {
            CustomerId = customerId;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Boolean loginSuccess = false;
            String userId = "";
            String userDisplay = "";
            JSONObject jsonResponse = null;

            try
            {
                JSONObject jsonParam = new JSONObject();

                Common comm = new Common();
                comm.setAPIURL(getString(R.string.AloopyAPIURL));
                jsonResponse = comm.GetAPI("/aloopy/customerloyalty?customerId=" + CustomerId);

                if(jsonResponse != null)
                {
                    String strSuccess = jsonResponse.getString("success");

                    if(strSuccess == "true")
                    {
                        JSONArray cardSetArray = jsonResponse.getJSONArray("loyaltyCards");

                        if(cardSetArray != null)
                        {
                            cardData = new ArrayList<CustomerLoyaltyCardContract>();

                            Context cont = getActivity().getBaseContext();

                            // Gets the data repository in write mode
                            AloopySQLHelper helper = AloopySQLHelper.getInstance(getActivity());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            try
                            {
                                db.delete(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.TABLE_NAME, null, null);
                            }
                            catch (Exception ex)
                            {

                            }

                            for(int ctr=0;ctr<cardSetArray.length();ctr++)
                            {
                                CustomerLoyaltyCardContract cardItem =  new CustomerLoyaltyCardContract();
                                cardItem.CustomerID = CustomerId;
                                cardItem.CustomerCardID = cardSetArray.getJSONObject(ctr).getString("id");
                                cardItem.CardImage = cardSetArray.getJSONObject(ctr).getJSONObject("loyaltyCardImage_x3").getString("filePath");
                                cardItem.CardPrice = cardSetArray.getJSONObject(ctr).getString("cardPrice");
                                cardItem.CardTitle = cardSetArray.getJSONObject(ctr).getString("title");
                                cardItem.CardVolume = cardSetArray.getJSONObject(ctr).getInt("volume");
                                cardItem.CardID = cardSetArray.getJSONObject(ctr).getString("loyaltyCardID");
                                cardItem.DateCreated = cardSetArray.getJSONObject(ctr).getString("dateCreated");
                                cardItem.DateExpiration = cardSetArray.getJSONObject(ctr).getString("dateExpiration");
                                cardItem.MerchantID = cardSetArray.getJSONObject(ctr).getString("merchantId");
                                cardItem.QRCode = cardSetArray.getJSONObject(ctr).getJSONObject("loyaltyCardQRCode_x3").getString("filePath");
                                cardItem.TotalPoints = cardSetArray.getJSONObject(ctr).getInt("totalPoints");

                                cardData.add(cardItem);

                                //SAVE TO DATABASE
                                ContentValues values = new ContentValues();
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Card_ID, cardItem.CardID);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Image, cardItem.CardImage);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Price, cardItem.CardPrice);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Title, cardItem.CardTitle);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Card_Volume, cardItem.CardVolume);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Customer_Card_ID, cardItem.CustomerCardID);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Customer_ID, cardItem.CustomerID);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Date_Created, cardItem.DateCreated);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Date_Expiration, cardItem.DateExpiration);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Merchant_ID, cardItem.MerchantID);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_QR_Code, cardItem.QRCode);
                                values.put(CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Total_Points, cardItem.TotalPoints);

                                long newRowId;
                                newRowId = db.insert(
                                        CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.TABLE_NAME,
                                        CustomerLoyaltyCardContract.CustomerLoyaltyCardInformation.COLUMN_NAME_Customer_ID,
                                        values);
                            }

                            db.close();
                        }

                        loginSuccess = true;
                    }
                }
            }catch (Exception ex) {

                String abc = ex.getMessage();

            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if(success)
            {
                cardSetAdapter = new CardSetAdapter(getActivity(), R.layout.card_set_row, cardData);
                gridView.setAdapter(cardSetAdapter);
                //finish()
            }
            else
            {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);


        }


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            this.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
    }
}
