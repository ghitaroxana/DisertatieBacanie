package com.example.roxanagh.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;

/**
 * Created by Roxana on 5/22/2015.
 */
public class WebServiceActivity extends Activity {
    private final String NAMESPACE = "http://tempuri.org/";
    //Change IP to your machine IP
    private final String URL = "http://192.168.1.101/JSONService/Service.asmx";
    private final String SOAP_ACTION = "http://tempuri.org/";
    private String TAG = "PGGURU";
    private static String responseJSON;
    //Country Spinner Control
    Spinner countrySpinnerCtrl;
    //City Spinner Control
    Spinner citySpinnerCtrl;
    ProgressBar pg;
    String[] placelist;
    //GSON object
    Gson gson = new Gson();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        //City Spinner control
//        citySpinnerCtrl = (Spinner) findViewById(R.id.citySpinner);
//        //Country Spinner control
//        countrySpinnerCtrl = (Spinner) findViewById(R.id.countrySpinner);
//        //Progress bar to be displayed until app gets web service response
//        pg = (ProgressBar) findViewById(R.id.progressBar1);
        //AysnTask class to handle Country WS call as separate UI Thread
        AsyncCountryWSCall task = new AsyncCountryWSCall();
        //Execute the task to set Country list in Country Spinner Control
        task.execute();

        countrySpinnerCtrl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //When an item is selected from Country Spinner Control
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                //Get the selected  item value
                String floor = countrySpinnerCtrl.getSelectedItem().toString();
                //AsynTask class to handle City WS call as separate UI Thread
                AsyncCityWSCall task = new AsyncCityWSCall();
                //Execute the task
                task.execute(floor);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    //AysnTask class to handle Country WS call as separate UI Thread
    private class AsyncCountryWSCall extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            //Invoke web method 'PopulateCountries' with dummy value
            //  invokeJSONWS("dummy", "PopulateCountries");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Convert 'Countries' JSON response into String array using fromJSON method
            placelist = gson.fromJson(responseJSON, String[].class);
            //Assign the String array as Country Spinner Control's items
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_dropdown_item_1line, placelist);
            countrySpinnerCtrl.setAdapter(adapter);
            //Make the progress bar invisible
            pg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //Display progress bar
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }

    //AsynTask class to handle City WS call as separate UI Thread
    private class AsyncCityWSCall extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            //Invoke web method 'PopulateCities'
            // invokeJSONWS(params[0], "PopulateCities");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Convert 'Cities' JSON response into String array using fromJSON method
            placelist = gson.fromJson(responseJSON, String[].class);
            //Assign the String array as Country Spinner Control's items
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_dropdown_item_1line, placelist);
            citySpinnerCtrl.setAdapter(adapter);
            //Make the progress bar invisbile
            pg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //Display the progress bar
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");

        }

    }

    //Method which invoke web methods

}
