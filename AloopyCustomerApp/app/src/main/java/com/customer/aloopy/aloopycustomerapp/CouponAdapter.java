package com.customer.aloopy.aloopycustomerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.CustomerCouponContract;


import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by imbisibol on 9/15/2015.
 */
public class CouponAdapter extends ArrayAdapter<CustomerCouponContract> {

    Context context;
    int layoutResourceId;
    ArrayList<CustomerCouponContract> data = new ArrayList<CustomerCouponContract>();

    public CouponAdapter(Context context, int layoutResourceId,
                           ArrayList<CustomerCouponContract> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public CustomerCouponContract getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CouponItem holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CouponItem();
            holder.imgCoupon = (ImageView) row.findViewById(R.id.imgCoupon);
            holder.imgQRCode = (ImageView) row.findViewById(R.id.imgQRCode);
            holder.lblClaimed = (TextView) row.findViewById(R.id.lblCalimed);
            holder.lblCouponTitle = (TextView) row.findViewById(R.id.lblCouponTitle);
            holder.lblStartDate = (TextView) row.findViewById(R.id.lblStartDate);
            holder.lblEndDate = (TextView) row.findViewById(R.id.lblEndDate);

            row.setTag(holder);

        } else {
            holder = (CouponItem) row.getTag();
        }

        CustomerCouponContract item = data.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat destFormat = new SimpleDateFormat("MM-dd-yyyy");

        Date dateItem;
        holder.lblCouponTitle.setText(item.CouponName);
        try {
            dateItem = formatter.parse(item.StartDate.replace('.', '-'));
            holder.lblStartDate.setText(destFormat.format(dateItem));
        }
        catch(Exception ex){}
        try {
            dateItem = formatter.parse(item.EndDate.replace('.', '-'));
            holder.lblEndDate.setText(destFormat.format(dateItem));
        }
        catch(Exception ex){}
        if (item.MainBannerImage != null && item.MainBannerImage != "")
            Common.getImageLoader(null).displayImage(item.MainBannerImage, holder.imgCoupon);
        if (item.QRCodeImage != null && item.QRCodeImage != "")
            Common.getImageLoader(null).displayImage(item.QRCodeImage, holder.imgQRCode);

        if(item.Claimed == null || item.Claimed.equals("false"))
            holder.lblClaimed.setVisibility(View.GONE);


        return row;

    }

    static class CouponItem
    {
        public TextView lblCouponTitle;
        public TextView lblClaimed;
        public ImageView imgQRCode;
        public ImageView imgCoupon;
        public TextView lblStartDate;
        public TextView lblEndDate;
        public TextView lblDateUpdated;
    }

}
