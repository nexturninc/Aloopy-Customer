package com.customer.aloopy.aloopycustomerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.AloopySQLHelper;
import com.customer.aloopy.aloopydatabase.CustomerInfoContract;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by imbisibol on 9/8/2015.
 */
public class SignupActivity extends Activity {

    private SignupTask mSignupTask = null;
    private ProgressBar mProgressBar;
    private View mSignupForm;

    public SignupActivity()
    {
        //ABC GGG
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_signup);

        Button btnSignup = ((Button)findViewById(R.id.btnSignUp));
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mSignupForm = (findViewById(R.id.dvSignupForm));
        mProgressBar = ((ProgressBar)findViewById(R.id.login_progress));

    }

    public void attemptLogin() {
        if (mSignupTask != null) {
            return;
        }

        TextView txtPassword = (TextView)findViewById(R.id.txtPassword);
        TextView txtConfirmPassword = (TextView)findViewById(R.id.txtConfirmPassword);

        if(txtPassword.getText().length() == 0)
            txtPassword.setText("Password is required!");
        else {
            if (txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {

                TextView txtUserName = (TextView) findViewById(R.id.txtUsername);
                TextView txtEmailAddress = (TextView) findViewById(R.id.txtEmailAddress);
                TextView txtFirstName = (TextView) findViewById(R.id.txtFirstName);
                TextView txtLastName = (TextView) findViewById(R.id.txtLastName);

                if(txtUserName.getText().length() == 0)
                    txtUserName.setError("Username is required!");
                else if (txtEmailAddress.getText().length() == 0)
                    txtEmailAddress.setError("Email Address is required!");
                else {
                    showProgress(true);

                    mSignupTask = new SignupTask(txtEmailAddress.getText().toString(),
                            txtUserName.getText().toString(), txtPassword.getText().toString(),
                            txtFirstName.getText().toString(), txtLastName.getText().toString());
                    mSignupTask.execute((Void) null);
                }
            } else {
                txtConfirmPassword.setError("Password Confirmation does not match!");
            }
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
            mSignupForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                jsonParam.put("username", mUsername);
                jsonParam.put("emailAddress", mEmail);
                jsonParam.put("firstName", mFirstname);
                jsonParam.put("lastName", mLastname);


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
            mSignupTask = null;
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
            mSignupTask = null;
            showProgress(false);
        }
    }

}
