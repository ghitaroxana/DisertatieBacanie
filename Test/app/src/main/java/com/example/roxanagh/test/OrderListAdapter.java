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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roxana on 5/2/2015.
 */
public class OrderListAdapter extends ArrayAdapter<OrderItem> {
    Context context;
    ArrayList<OrderItem> list;
    Float total = 0f;
    OrderItem current;
    TextView totalTV;
    TextView subtotalTV;

    public OrderListAdapter(Context context, ArrayList list, TextView totalTV,TextView subtotalTV) {
        super(context, R.layout.category_item_view, list);
        this.context = context;
        this.list = list;
        this.totalTV = totalTV;
        this.subtotalTV = subtotalTV;
    }


    public View getView(int position, final View convertView, ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemView = inflater.inflate(R.layout.order_prod_view, parent, false);
        }
        TextView tvName = (TextView) itemView.findViewById(R.id.prod_name);
        TextView tvUnitPrice = (TextView) itemView.findViewById(R.id.unitPriceTV);
        TextView tvValuePrice = (TextView) itemView.findViewById(R.id.valuePriceTV);
        NumberPicker np = (NumberPicker) itemView.findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(false);
        final TextView tvValueOrderTotal = (TextView) itemView.findViewById(R.id.valueOrderTotalTV);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.prod_icon);

        final OrderItem currentItem = list.get(position);
        this.current = list.get(position);
        tvName.setText(currentItem.getProduct().getName());
        tvUnitPrice.setText("/per " + currentItem.getProduct().getPer().toString());
        tvValuePrice.setText(currentItem.getProduct().getPrice().toString() + " RON");
        np.setValue(this.current.getQuantity());
        total = currentItem.getProduct().getPrice() * currentItem.getQuantity();
        tvValueOrderTotal.setText(total.toString() + " RON");

        if (currentItem.getProduct().bytePhotos == null || currentItem.getProduct().bytePhotos.length == 0) {
            imageView.setImageResource(R.drawable.bio1);
        } else {
            byte[] b = Base64.decode(currentItem.getProduct().bytePhotos[0], Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bm);
        }
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub

                total = current.getProduct().getPrice() * newVal;
                current.setQuantity(newVal);
                tvValueOrderTotal.setText(total.toString() + " RON");
                Utils.totalOrderBuyer+= current.getProduct().getPrice();
                Float x = 10f+Utils.totalOrderBuyer;
                totalTV.setText(x.toString());
                subtotalTV.setText(Utils.totalOrderBuyer.toString());
                Utils.nr++;
            }
        });
        return itemView;
    }

}
