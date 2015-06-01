package com.example.roxanagh.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roxana on 3/29/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gto.db";
    public static Context mContext;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            executeSqlFromFile(db, mContext, R.raw.data_base);
            //    db.execSQL("CREATE TABLE IF NOT EXISTS COUNTY(ID_COUNTY integer primary key autoincrement, CODE VARCHAR2(10), DESCRIPTION VARCHAR2(255));");
            executeSqlFromFile(db, mContext, R.raw.insert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            executeSqlFromFile(db, mContext, R.raw.insert);
            //     onCreate(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int executeSqlFromFile(SQLiteDatabase db, Context context, int resourceId) throws IOException {

        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
            result++;
        }
        insertReader.close();

        // returning number of inserted rows
        return result;
    }

    public List<County> getAllCounties() {
        List<County> counties = new ArrayList<County>();
        County county = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM COUNTY";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                county = new County(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                counties.add(county);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return counties;
    }

    public County getCountyById(Long id) {
        County county = null;
        String selectQuery = "SELECT * FROM COUNTY WHERE ID_COUNTY = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            county = new County(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        }
        return county;
    }

    public Locality getLocalityById(Long id, County county) {
        Locality locality = null;
        String selectQuery = "SELECT * FROM LOCALITY WHERE ID_LOCALITY = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            locality = new Locality(Long.parseLong(cursor.getString(0)), county, cursor.getString(1), cursor.getString(2));
        }
        return locality;
    }

    public List<Locality> getAllLocalities(County county) {
        List<Locality> localities = new ArrayList<Locality>();
        Locality locality = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM LOCALITY WHERE ID_COUNTY = " + county.getId();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //    public Locality(Long id, County county, String code, String description) {

                locality = new Locality(Long.parseLong(cursor.getString(0)), county, cursor.getString(1), cursor.getString(2));
                localities.add(locality);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return localities;
    }
}
