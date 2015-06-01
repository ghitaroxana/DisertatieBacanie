package com.example.roxanagh.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Roxana on 4/17/2015.
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Test";



    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name,String pass,UserRole userRole){
        editor.putBoolean(Utils.IS_LOGIN, true);
        editor.putString(Utils.KEY_NAME, name);
        editor.putString(Utils.PASSWORD, pass);
        editor.putInt(Utils.ID_USER_ROLE, userRole.getIntValue());

        editor.commit();
    }



    public boolean isLoggedIn(){
        return pref.getBoolean(Utils.IS_LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){

            Intent i = new Intent(_context,LoginActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public HashMap<String,String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Utils.KEY_NAME, pref.getString(Utils.KEY_NAME, null));
        user.put(Utils.PASSWORD, pref.getString(Utils.PASSWORD, null));
        user.put(Utils.ID_USER_ROLE, pref.getString(Utils.ID_USER_ROLE, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Utils.userLogged=null;
        Utils.settingList=null;

        Intent i = new Intent(_context,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
