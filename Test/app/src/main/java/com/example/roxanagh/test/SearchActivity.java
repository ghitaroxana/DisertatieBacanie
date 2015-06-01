package com.example.roxanagh.test;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by roxanagh on 4/21/2015.
 */
public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("search","query="+query);
            //aici caut in bd produse care au in descriere sau denumire cuvantul cautat si intorc o lista de produse (list view)
        }

    }
}
