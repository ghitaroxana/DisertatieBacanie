package com.example.roxanagh.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by roxanagh on 3/31/2015.
 */
public class CountySpinAdapter extends ArrayAdapter<County> {

    private Context context;
    // Your custom values for the spinner
    private County[] values;

    public CountySpinAdapter(Context context, int textViewResourceId,
                       County[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public County getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return values[position].getId();
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        //label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].getDescription());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
  //      label.setTextColor(Color.BLACK);
        label.setText(values[position].getDescription());

        return label;
    }
}