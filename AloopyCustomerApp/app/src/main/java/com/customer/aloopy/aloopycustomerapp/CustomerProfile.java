package com.customer.aloopy.aloopycustomerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerInfoContract;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by imbisibol on 8/27/2015.
 */
public class CustomerProfile extends ActionBarActivity {

    private UpdateProfileTask mUpdateTask = null;
    private ProgressBar mProgressBar;
    private View mCustomerProfileBody;

    String CustomerId;
    TextView lblUsername;
    TextView lblCustomerCode;
    ImageView imgCustomerQRCode;
    EditText txtPassword;
    EditText txtCPassword;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmailAddress;

    Button btnUpdateProfile;


    public CustomerProfile() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        AloopySQLHelper helper = AloopySQLHelper.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //INITIALIZE CONTROLS
        mProgressBar = (ProgressBar)findViewById(R.id.login_progress);
        mCustomerProfileBody = findViewById(R.id.dvCustomerProfileBody);

        lblUsername = (TextView)findViewById(R.id.lblUsername);
        lblCustomerCode = (TextView)findViewById(R.id.lblCustomerCode);
        imgCustomerQRCode = (ImageView)findViewById(R.id.imgCustomerQRCode);

        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtEmailAddress = (EditText)findViewById(R.id.txtEmailAddress);

        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtCPassword = (EditText)findViewById(R.id.txtConfirmPassword);

        btnUpdateProfile = (Button)findViewById(R.id.btnSave);


        //GET DATA FROM DB
        String[] projection = {
                CustomerInfoContract.CustomerInformation._ID,
                CustomerInfoContract.CustomerInformation.COLUMN_NAME_Customer_ID,
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

        CustomerId = c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Customer_ID));
        lblUsername.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Username)));
        lblCustomerCode.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_Code)));
        Common.getImageLoader(null).displayImage(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_QRCodeURL)), imgCustomerQRCode);

        txtFirstName.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_FirstName)));
        txtLastName.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_LastName)));
        txtEmailAddress.setText(c.getString(c.getColumnIndexOrThrow(CustomerInfoContract.CustomerInformation.COLUMN_NAME_EmailAddress)));

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //UPDATE PROFILE
                showProgress(true);

                if (txtPassword.getText().length() > 0) {
                    if (txtCPassword.getText().length() > 0) {
                        txtCPassword.setError("Password Confirmation is required!");
                    } else if (!txtPassword.getText().toString().equals(txtCPassword.getText().toString())) {
                        txtCPassword.setError("Password Confirmation does not match!");
                    }
                }
                else if (txtEmailAddress.getText().length() > 0) {
                    txtEmailAddress.setError("Email Address is required!");
                }
                else {

                    mUpdateTask = new UpdateProfileTask(CustomerId,
                            txtEmailAddress.getText().toString(),
                            lblUsername.getText().toString(), txtPassword.getText().toString(),
                            txtFirstName.getText().toString(), txtLastName.getText().toString());
                    mUpdateTask.execute((Void) null);

                }
            }
        });


        db.close();

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            mCustomerProfileBody.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCustomerProfileBody.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCustomerProfileBody.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UpdateProfileTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserId;
        private final String mEmail;
        private final String mUsername;
        private final String mPassword;
        private final String mFirstname;
        private final String mLastname;

        UpdateProfileTask(String userId, String email, String userName, String password, String firstName, String lastName) {
            mUserId = userId;
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
                jsonParam.put("username", mUsername);
                jsonParam.put("emailAddress", mEmail);
                jsonParam.put("firstName", mFirstname);
                jsonParam.put("lastName", mLastname);


                Common comm = new Common();
                comm.setAPIURL(getString(R.string.AloopyAPIURL));
                jsonResponse = comm.PutAPI(jsonParam, "/aloopy/customer");


                if (jsonResponse != null) {

                    String strSuccess = jsonResponse.getString("success");


                    if (strSuccess == "true") {

                        JSONArray customerInfo = jsonResponse.getJSONArray("customerInfo");

                        if(customerInfo != null && customerInfo.length() > 0) {
                            userId = customerInfo.getJSONObject(0).getString("id");
                            userDisplay = customerInfo.getJSONObject(0).getString("firstName")  +
                                    " " +
                                    customerInfo.getJSONObject(0).getString("lastName") +
                                    " (" +
                                    customerInfo.getJSONObject(0).getString("code") +
                                    ")";

                            //SAVE TO SHARED PREFERENCES
                            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString(getString(R.string.SHARE_PREF_UserId), userId);
                            editor.putString(getString(R.string.SHARE_PREF_UserName), userDisplay);
                            editor.commit();


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

            }
            catch (Exception ex) {
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
            mUpdateTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mUpdateTask = null;
            showProgress(false);
        }
    }

}
