package com.customer.aloopy.aloopycustomerapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerInfoContract;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by imbisibol on 9/8/2015.
 */
public class SignupActivity extends Activity {

    private SignupTask mSignupTask = null;

    public SignupActivity()
    {
        //ABC GGG
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_signup);

    }


    public class SignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mUsername;
        private final String mPassword;
        private final String mFirstname;
        private final String mLastname;

        SignupTask(String email, String userName, String password, String firstName, String lastName) {
            mEmail = email;
            mUsername = userName;
            mPassword = password;
            mFirstname = firstName;
            mLastname = lastName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Boolean loginSuccess = false;
            String userId = "";
            String userDisplay = "";
            JSONObject jsonResponse = null;

            try {

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("password", mPassword);
                jsonParam.put("username", mEmail);


                Common comm = new Common();
                comm.setAPIURL(getString(R.string.AloopyAPIURL));
                jsonResponse = comm.PostAPI(jsonParam, "/aloopy/customer");


                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("success");

                    if (strSuccess == "true") {

                        JSONArray customerInfo = jsonResponse.getJSONArray("customerInfo");

                        if(customerInfo != null && customerInfo.length() > 0) {
                            userId = customerInfo.getJSONObject(0).getString("id");
                            //SAVE TO SHARED PREFERENCES
                            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString(getString(R.string.SHARE_PREF_UserId), userId);
                            editor.commit();

                            userDisplay = customerInfo.getJSONObject(0).getString("firstName")  +
                                    " " +
                                    customerInfo.getJSONObject(0).getString("lastName") +
                                    " (" +
                                    customerInfo.getJSONObject(0).getString("code") +
                                    ")"
                            ;

                            // Gets the data repository in write mode
                            AloopySQLHelper helper = AloopySQLHelper.getInstance(getBaseContext());
                            SQLiteDatabase db = helper.getWritableDatabase();

                            // Create a new map of values, where column names are the keys
                            ContentValues values = new ContentValues();
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Customer_ID, customerInfo.getJSONObject(0).getString("id"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Username, customerInfo.getJSONObject(0).getString("username"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Code, customerInfo.getJSONObject(0).getString("code"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_EmailAddress, customerInfo.getJSONObject(0).getString("emailAddress"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_FirstName, customerInfo.getJSONObject(0).getString("firstName"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_LastName, customerInfo.getJSONObject(0).getString("lastName"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_DateCreated, customerInfo.getJSONObject(0).getString("dateCreated"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_DateModified, customerInfo.getJSONObject(0).getString("dateModified"));

                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_QRCodeURL, customerInfo.getJSONObject(0).getJSONObject("qrCodeImage_3x").getString("imageURL"));
                            values.put(CustomerInfoContract.CustomerInformation.COLUMN_NAME_AvatarURL, customerInfo.getJSONObject(0).getJSONObject("avatarURLImage_3x").getString("imageURL"));

                            // Insert the new row, returning the primary key value of the new row
                            long newRowId;
                            db.delete(CustomerInfoContract.CustomerInformation.TABLE_NAME, null, null);
                            newRowId = db.insert(
                                    CustomerInfoContract.CustomerInformation.TABLE_NAME,
                                    CustomerInfoContract.CustomerInformation.COLUMN_NAME_Code,
                                    values);

                            db.close();
                        }


                        loginSuccess = true;
                    }
                }

            } catch (Exception ex) {
            }

            if(loginSuccess) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra(getString(R.string.EXTRA_LoginDisplay_Name), userDisplay);
                startActivity(intent);
            }


            // TODO: register the new account here.
            return loginSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSignupTask = null;
            //showProgress(false);

            if (success) {
                finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mSignupTask = null;
            //showProgress(false);
        }
    }

}
