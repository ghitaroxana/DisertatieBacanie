package com.example.roxanagh.test;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roxana on 5/17/2015.
 */
public class CategorySettings extends ListActivity {

    private ArrayList<SettingCheckBox> settingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_menu);
        setTitle("Alegeți categoriile dorite");
        settingList = (ArrayList<SettingCheckBox>) getIntent().getSerializableExtra(ProducerMainMenu.SETTING_CHECK_BOX);
        if (savedInstanceState != null) {
            settingList = (ArrayList<SettingCheckBox>) savedInstanceState.getSerializable(ProducerMainMenu.SETTING_CHECK_BOX);
        }
        setListAdapter(new MyActivity_Settings_Adapter(this, R.layout.sub_menu_item, settingList));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProducerMainMenu.SETTING_CHECK_BOX, settingList);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, new Intent().putExtra(ProducerMainMenu.SETTING_CHECK_BOX, settingList));
        super.finish();
    }

}

class MyActivity_Settings_Adapter extends ArrayAdapter<SettingCheckBox> {

    private final LayoutInflater layoutInflater;
    private final int itemResourceId;

    // Holder pattern (used instead of findViewById for better performance)
    static class ViewHolder {
        public TextView title;
        public CheckBox checkBox;
    }

    // Constructor
    public MyActivity_Settings_Adapter(ListActivity context, int itemResourceId, List<SettingCheckBox> options) {
        super(context, itemResourceId, options);
        layoutInflater = context.getLayoutInflater();
        this.itemResourceId = itemResourceId;
    }

    // Method called by the list view every time to display a row
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare and initialize the row view
        View rowView = convertView;
        // Declare the row view holder
        ViewHolder viewHolder;
        // Check if an inflated view is provided
        if (rowView == null) {
            // A new view must be inflated
            rowView = layoutInflater.inflate(itemResourceId, null);
            // Declare and initialize a view holder
            viewHolder = new ViewHolder();
            // Retrieve a reference to the row title
            viewHolder.title = (TextView) rowView.findViewById(R.id.option_title);
            // Retrieve a reference to the row check box
            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.option_checkbox);
            // Store the view holder as tag
            rowView.setTag(viewHolder);
        } // End if
        else
            // An inflated view is already provided, retrieve the stored view holder
            viewHolder = (ViewHolder) rowView.getTag();

        // Set the option title
        viewHolder.title.setText(getItem(position).getDescription());
        // Set the option check box state
        viewHolder.checkBox.setChecked(getItem(position).getChecked());
        // Assign a click listener to the checkbox
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View checkBox) {
                // Retrieve the stored view holder
                ViewHolder viewHolder = (ViewHolder) ((View) checkBox.getParent()).getTag();
                // Update the option state
                getItem(position).setChecked(!getItem(position).getChecked());
                // Display the new option state
                viewHolder.checkBox.setChecked(getItem(position).getChecked());
            }
        });

        // Return the row view for display
        return rowView;
    } // End of getView

}