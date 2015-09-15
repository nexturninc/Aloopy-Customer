package com.customer.aloopy.aloopycustomerapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.customer.aloopy.aloopydatabase.CustomerCouponContract;



import java.util.ArrayList;

/**
 * Created by imbisibol on 9/15/2015.
 */
public class CouponList extends Fragment {

    public String UserID;
    public GridView gridview;
    public ArrayList<CustomerCouponContract> stampData = new ArrayList<CustomerCouponContract>();
    public CouponAdapter couponAdapter;

    //private StampSetTask mAuthTask = null;
    private ProgressBar mProgressBar;
    private View mStampListBody;

    public CouponList() {
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

        GetCoupons(false);

        //GET DATA
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //Toast.makeText(getActivity().getBaseContext(), stampData.get(position).CustomerStampSetId, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getBaseContext(), StampDetails.class);
                //intent.putExtra(getString(R.string.EXTRA_StampDetail_Id), stampData.get(position).CustomerStampSetId);
                startActivity(intent);

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


    private void GetCoupons(boolean forceAPIQuery){

    }

}
