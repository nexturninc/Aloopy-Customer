package com.customer.aloopy.aloopycustomerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.aloopy.aloopydatabase.CustomerLoyaltyCardContract;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Emporia-003 on 9/14/2015.
 */
public class CardSetAdapter extends ArrayAdapter<CustomerLoyaltyCardContract> {

    Context context;
    int layoutResourceId;
    ArrayList<CustomerLoyaltyCardContract> data = new ArrayList<CustomerLoyaltyCardContract>();

    public CardSetAdapter(Context context, int layoutResourceId, ArrayList<CustomerLoyaltyCardContract> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public CustomerLoyaltyCardContract getItem(int position)
    {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        CardItem holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CardItem();
            holder.CardID = (TextView) row.findViewById(R.id.txtCardID);
            holder.CardTitle = (TextView) row.findViewById(R.id.txtCardTitle);
            holder.CardVolume = (TextView) row.findViewById(R.id.txtCardVolume);
            holder.DateExpiration = (TextView) row.findViewById(R.id.txtDateExpiration);
            holder.CardPrice = (TextView) row.findViewById(R.id.txtCardPrice);
            holder.MerchantID = (TextView) row.findViewById(R.id.txtMerchantID);
            holder.CardImage = (ImageView) row.findViewById(R.id.imgCard);
            holder.QRCode = (ImageView) row.findViewById(R.id.imgQRCode);
            holder.TotalPoints = (TextView) row.findViewById(R.id.txtTotalPoints);
            holder.CustomerID = (TextView) row.findViewById(R.id.txtCustomerID);
            holder.CustomerCardID = (TextView) row.findViewById(R.id.txtCustomerCardID);
            holder.DateCreated = (TextView) row.findViewById(R.id.txtDateCreated);

            row.setTag(holder);
        }
        else
        {
            holder = (CardItem) row.getTag();
        }

        CustomerLoyaltyCardContract item = data.get(position);
        holder.CardID.setText(item.CardID);
        holder.CardTitle.setText(item.CardTitle);
        holder.CardVolume.setText(String.valueOf(item.CardVolume));
        holder.DateExpiration.setText(item.DateExpiration);
        holder.CardPrice.setText(item.CardPrice);
        holder.MerchantID.setText(item.MerchantID);
        holder.TotalPoints.setText(String.valueOf(item.TotalPoints));
        holder.CustomerID.setText(item.CustomerID);
        holder.CustomerCardID.setText(item.CustomerCardID);
        holder.DateCreated.setText(item.DateCreated);

        Common.getImageLoader(null).displayImage(item.CardImage, holder.CardImage);
        Common.getImageLoader(null).displayImage(item.QRCode, holder.QRCode);

        return row;
    }

    static class CardItem
    {
        public TextView CardID;
        public TextView CardTitle;
        public TextView CardVolume;
        public TextView DateExpiration;
        public TextView CardPrice;
        public TextView MerchantID;
        public ImageView CardImage;
        public ImageView QRCode;
        public TextView TotalPoints;
        public TextView CustomerID;
        public TextView CustomerCardID;
        public TextView DateCreated;
    }
}
