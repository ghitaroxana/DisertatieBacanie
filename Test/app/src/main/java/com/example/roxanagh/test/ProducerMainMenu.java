package com.example.roxanagh.test;

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
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Roxana on 4/19/2015.
 */
public class ProducerMainMenu extends Activity {
    SessionManager session;
    Button vegetables, fruits, meat, eggs, milk, bread, others;
    Menu menuItem;

    public static final String SETTING_CHECK_BOX = "SETTING_CHECK_BOX";
    private ArrayList<SettingCheckBox> settingList;

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

        vegetables.setVisibility(View.GONE);
        fruits.setVisibility(View.GONE);
        meat.setVisibility(View.GONE);
        eggs.setVisibility(View.GONE);
        milk.setVisibility(View.GONE);
        bread.setVisibility(View.GONE);
        others.setVisibility(View.GONE);

        settingList = new ArrayList<SettingCheckBox>();
        settingList.add(new SettingCheckBox("Legume", ProductCategory.VEGETABLES));
        settingList.add(new SettingCheckBox("Fructe", ProductCategory.FRUITS));
        settingList.add(new SettingCheckBox("Carne", ProductCategory.MEAT));
        settingList.add(new SettingCheckBox("Ouă", ProductCategory.EGGS));
        settingList.add(new SettingCheckBox("Lactate", ProductCategory.MILK));
        settingList.add(new SettingCheckBox("Panificație", ProductCategory.BREAD));
        settingList.add(new SettingCheckBox("Altele", ProductCategory.OTHERS));

        if (savedInstanceState != null) {
            settingList = (ArrayList<SettingCheckBox>) savedInstanceState.getSerializable(SETTING_CHECK_BOX);
            populateWithCategories(settingList);
        }

        ImageView icon = new ImageView(ProducerMainMenu.this); // Create an icon
        icon.setImageResource(R.drawable.ic_plus);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(ProducerMainMenu.this)
                .setContentView(icon)
                .setTheme(R.style.Theme_AppCompat)
                .build();
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(ProducerMainMenu.this, "Ati adaugat un produs", LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddNewProduct.class);
                startActivity(intent);
            }
        });

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SETTING_CHECK_BOX, settingList);
        Utils.settingList = settingList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //actionBar.show();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.producer_menu, menu);

        menuItem = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        if (Utils.settingList != null && Utils.settingList.size() > 0) {
            populateWithCategories(Utils.settingList);
            settingList = Utils.settingList;
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_producer:
                Toast.makeText(getBaseContext(), "CAUTARE", Toast.LENGTH_SHORT);
                onSearchRequested();
                return true;

            case R.id.list_producer:
                return true;

            case R.id.category_producer:
                Intent intent = new Intent(this, CategorySettings.class);
                intent.putExtra(SETTING_CHECK_BOX, settingList);
                startActivityForResult(intent, 0);

            case R.id.account_producer:
                return true;

            case R.id.logout_icon_producer:
                session.logoutUser();
                System.exit(0);
                return true;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null)
            return;
        settingList = (ArrayList<SettingCheckBox>) data.getSerializableExtra(SETTING_CHECK_BOX);
        Utils.settingList = settingList;
        populateWithCategories(settingList);
    }

    private void populateWithCategories(ArrayList<SettingCheckBox> settingList) {
        for (SettingCheckBox settingCheckBox : settingList) {
            if (settingCheckBox.getChecked())
                getButtonBySettings(settingCheckBox).setVisibility(View.VISIBLE);
            else getButtonBySettings(settingCheckBox).setVisibility(View.GONE);
        }
    }

    Button getButtonBySettings(SettingCheckBox settings) {
        switch (settings.getProductCategory().getIntValue()) {
            case 0:
                return vegetables;
            case 1:
                return fruits;
            case 2:
                return eggs;
            case 3:
                return meat;
            case 4:
                return bread;
            case 5:
                return milk;
            case 6:
                return others;
        }
        return null;
    }
}
