package com.example.roxanagh.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Roxana on 5/2/2015.
 */
public class ShoppingCartActivity extends Activity {
    SessionManager session;
    Context mContext;
    ListView lv;
    OrderItem orderItem = null;
    EditText quantityET;
    NumberPicker numberPicker;
    TextView totalTV;
    TextView subtotalTV;
    Float total = 0f;
    int quantity = 0;
    TextView cartTV;

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionManager(getApplicationContext());
        mContext = ShoppingCartActivity.this;
        lv = (ListView) findViewById(R.id.orderListLV);
        //  quantityET = (EditText) findViewById(R.id.quantityET);
        Float x = 10f + Utils.totalOrderBuyer;


        totalTV = (TextView) findViewById(R.id.totalPriceTV);
        totalTV.setText(x.toString());
        subtotalTV = (TextView) findViewById(R.id.subTotalPriceTV);
        subtotalTV.setText(Utils.totalOrderBuyer.toString());

        if (Utils.orderList != null && Utils.orderList.size() > 0) {
            populateListViewWithProducts(Utils.orderList);
            registerClickCallBack();
        }
        continueButton = (Button) findViewById(R.id.btnContinue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShoppingCartTransportActivity.class);
                startActivity(i);
            }
        });


    }

    public void populateListViewWithProducts(ArrayList<OrderItem> products) {

        ArrayAdapter<OrderItem> adapter = new OrderListAdapter(mContext, products, totalTV, subtotalTV);
        ListView list = (ListView) findViewById(R.id.orderListLV);
        list.setVisibility(View.VISIBLE);
        list.setAdapter(adapter);
    }

    private void registerClickCallBack() {
        lv.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
