package com.example.meetingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.meetingapp.models.Location;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtils {

    public PreferenceUtils() {

    }

    public static boolean saveEmail(String email, Context context) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
        prefsEditor.putString(Constants.KEY_EMAIL, email);
        prefsEditor.apply();
        return true;
    }

    public static boolean saveLocation(String latitude, String longitude, Context context) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences("LocationToGo", MODE_PRIVATE).edit();
        prefsEditor.putString("latitude", latitude);
        prefsEditor.putString("longitude", longitude);
        prefsEditor.apply();
        return true;
    }

    public static Location getLocation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LocationToGo", MODE_PRIVATE);
        return new Location(
                prefs.getString("latitude", null),
                prefs.getString("longitude", null)
        );
    }


    public static boolean getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        return prefs.contains(Constants.KEY_EMAIL);
    }

    public static boolean savePassword(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }
}