package com.example.roxanagh.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Roxana on 4/19/2015.
 */
public class BuyerMainMenu extends Activity {
    SessionManager session;
    Button vegetables, fruits, meat, eggs, milk, bread, others;
    ImageView selectedImageView;
    Menu menuItem;
    //   public static final String ORDER_LIST = "ORDER_LIST";
//    private ArrayList<SettingCheckBox> settingList;

    // int nr = 0;
    TextView cartTV;
    ImageView cartImage;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionManager(getApplicationContext());

        vegetables = (Button) findViewById(R.id.vegetables_button);
        fruits = (Button) findViewById(R.id.fruits_button);
        meat = (Button) findViewById(R.id.meat_button);
        eggs = (Button) findViewById(R.id.eggs_button);
        milk = (Button) findViewById(R.id.milk_button);
        bread = (Button) findViewById(R.id.bread_button);
        others = (Button) findViewById(R.id.others_button);

        final Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trebuie vazut cum trimite, daca trimite int sau string
                intent.putExtra("productCategory", ProductCategory.VEGETABLES.getIntValue());
                startActivity(intent);
            }
        });

        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.FRUITS.getIntValue());
                //   intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.MILK.getIntValue());
                // intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.MEAT.getIntValue());
                // intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.BREAD.getIntValue());
                //  intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
        eggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.EGGS.getIntValue());
                // intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productCategory", ProductCategory.OTHERS.getIntValue());
                //  intent.putExtra("noProd", nr);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //actionBar.show();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.buyer_menu, menu);
        final View menu_hotlist = menu.findItem(R.id.shopping_cart).getActionView();
        cartImage = (ImageView) menu_hotlist.findViewById(R.id.hotlist_bell);
        cartTV = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);

        actionBar = getActionBar();
        actionBar.show();


        updateHotCount();
        new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyerMainMenu.this, "Click pe shop cart", LENGTH_LONG).show();
            }
        };

        menuItem = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void updateHotCount() {
        if (cartTV == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Utils.nr == 0) {
                    cartImage.setImageResource(R.drawable.ic_cart_empty);
                    cartTV.setVisibility(View.GONE);
                } else {
                    cartImage.setImageResource(R.drawable.ic_cart_full);
                    cartTV.setVisibility(View.VISIBLE);
                    cartTV.setText(Integer.toString(Utils.nr));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Toast.makeText(getBaseContext(), "COS DE CUMPARATURI", Toast.LENGTH_SHORT);
                onSearchRequested();
                return true;

            case R.id.search_buyer:
                Toast.makeText(getBaseContext(), "CAUTARE", Toast.LENGTH_SHORT);
                onSearchRequested();
                return true;

            case R.id.list_buyer:
                return true;

            case R.id.favorites_buyer:
                return true;

            case R.id.account_buyer:
                return true;

            case R.id.logout_icon_buyer:
                session.logoutUser();
                System.exit(0);
                return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        updateHotCount();
        super.onResume();
    }


}
