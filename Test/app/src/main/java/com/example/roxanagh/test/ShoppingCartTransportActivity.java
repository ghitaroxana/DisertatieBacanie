package com.example.roxanagh.test;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Roxana on 6/1/2015.
 */
public class ShoppingCartTransportActivity extends Activity {

    SessionManager session;
    Context mContext;
    TextView totalTV;
    TextView subtotalTV;
    EditText userFullNameET;
    EditText addressET;
    EditText hourET;

    Float total = 0f;
    private Calendar startDate;
    Button dateBtn;
    static final int DATE_DIALOG_ID = 0;

    private Button activeDateDisplay;
    private Calendar activeDate;

    private int year;
    private int month;
    private int day;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionManager(getApplicationContext());
        mContext = ShoppingCartTransportActivity.this;

        Float x = 10f + Utils.totalOrderBuyer;


        totalTV = (TextView) findViewById(R.id.totalPriceTV);
        totalTV.setText(x.toString());
        subtotalTV = (TextView) findViewById(R.id.subTotalPriceTV);
        subtotalTV.setText(Utils.totalOrderBuyer.toString());
        userFullNameET = (EditText) findViewById(R.id.userFullNameET);
        addressET = (EditText) findViewById(R.id.addressET);
        hourET = (EditText) findViewById(R.id.hourET);

        user = Utils.userLogged;
        userFullNameET.setText(user.getDetail().getFirstName()+" "+user.getDetail().getLastName());
        addressET.setText(user.getDetail().getCounty().getId()==4?"":"Județul "+user.getDetail().getCounty().getDescription()+" Localitatea "+user.getDetail().getLocality().getDescription());

        dateBtn = (Button) findViewById(R.id.dateBtn);

        startDate = Calendar.getInstance();
        setCurrentDateOnView(dateBtn);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    public void setCurrentDateOnView(Button dateBtn) {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        dateBtn.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));

    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            dateBtn.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }
    };

}
