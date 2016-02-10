package com.supprojectstarter.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.supprojectstarter.activities.LoginActivity;

import java.util.HashMap;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    private static final String PREF_NAME = "SPS_PREF";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_FIRSTNAME = "firstname";
    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static String getKeyEmail() {
        return KEY_EMAIL;
    }

    public static String getKeyName() {
        return KEY_NAME;
    }

    public static String getKeyFirstname() {
        return KEY_FIRSTNAME;
    }

    public void createLoginSession(String email, String name, String firstname) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(getKeyEmail(), email);
        editor.putString(getKeyName(), name);
        editor.putString(getKeyFirstname(), firstname);
        editor.commit();
    }

    public void modifyLoginSession(String name, String firstname) {
        editor.putString(getKeyName(), name);
        editor.putString(getKeyFirstname(), firstname);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(getKeyEmail(), pref.getString(getKeyEmail(), null));
        user.put(getKeyName(), pref.getString(getKeyName(), null));
        user.put(getKeyFirstname(), pref.getString(getKeyFirstname(), null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void destroySharedPreferences() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
