package com.example.meetingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtils {

    public PreferenceUtils() {

    }

    public static boolean hasToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.contains(Constants.KEY_TOKEN);
    }

    public static void saveToken(String token, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putString(Constants.KEY_TOKEN, token);
        preferencesEditor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getString(Constants.KEY_TOKEN, "");
    }

    public static void removeToken(Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.remove(Constants.KEY_TOKEN).apply();
    }

    public static boolean hasEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.contains(Constants.KEY_EMAIL);
    }

    public static void saveEmail(String email, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putString(Constants.KEY_EMAIL, email);
        preferencesEditor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getString(Constants.KEY_EMAIL, "");
    }

    public static boolean hasPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.contains(Constants.KEY_PASSWORD);
    }

    public static void savePassword(String password, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putString(Constants.KEY_PASSWORD, password);
        preferencesEditor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getString(Constants.KEY_PASSWORD, "");
    }
}