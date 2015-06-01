package com.example.roxanagh.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roxana on 4/19/2015.
 */
public class ProductListAdapter extends ArrayAdapter<ProductItem> {
    Context context;
    ArrayList<ProductItem> list;

    public ProductListAdapter(Context context, ArrayList list) {
        super(context, R.layout.category_item_view, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String descr;

        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemView = inflater.inflate(R.layout.product_view, parent, false);
        }
        TextView tvName = (TextView) itemView.findViewById(R.id.item_name);
        TextView tvDescription= (TextView) itemView.findViewById(R.id.item_description);
        TextView tvDateFromTo= (TextView) itemView.findViewById(R.id.dateFromToTV);
        TextView tvPrice= (TextView)itemView.findViewById(R.id.priceTV);
        TextView tvLocality= (TextView)itemView.findViewById(R.id.localityTV);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);

        ProductItem currentItem = list.get(position);
        tvName.setText(currentItem.getName());
        tvDescription.setText(currentItem.getDescription());
        tvDateFromTo.setText(currentItem.getDateFrom().toString().substring(0,10)+" - "+currentItem.getDateTo().toString().substring(0,10));
        tvPrice.setText(currentItem.getPrice().toString()+" RON / per " + currentItem.getPer().toString());
        tvLocality.setText(currentItem.getUser().getDetail().getLocality().getDescription());

        if (currentItem.bytePhotos == null || currentItem.bytePhotos.length == 0) {
            imageView.setImageResource(R.drawable.bio1);
        } else {
            byte[] b = Base64.decode(currentItem.bytePhotos[0], Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bm);
        }
        return itemView;
    }
}
