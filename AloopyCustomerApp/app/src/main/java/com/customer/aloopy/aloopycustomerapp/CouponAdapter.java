package com.customer.aloopy.aloopycustomerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.customer.aloopy.aloopydatabase.CustomerCouponContract;



import java.util.ArrayList;

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

            row.setTag(holder);
        } else {
            holder = (CouponItem) row.getTag();
        }

        CustomerCouponContract item = data.get(position);

        if (item.MainBannerImage != null && item.MainBannerImage != "")
            Common.getImageLoader(null).displayImage(item.MainBannerImage, holder.imgCoupon);
        if (item.QRCodeImage != null && item.QRCodeImage != "")
            Common.getImageLoader(null).displayImage(item.QRCodeImage, holder.imgQRCode);



        return row;

    }

    static class CouponItem
    {
        public ImageView imgQRCode;
        public ImageView imgCoupon;
    }

}
