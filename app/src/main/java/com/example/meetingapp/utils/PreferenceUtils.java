package com.example.meetingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtils {

    public PreferenceUtils() {

    }

    public static void saveUserId(int userId, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putInt(Constants.KEY_USER_ID, userId);
        preferencesEditor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getInt(Constants.KEY_USER_ID, 0);
    }


    public static boolean hasFirebaseToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.contains(Constants.KEY_FIREBASE_TOKEN);
    }

    public static void saveFirebaseToken(String token, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putString(Constants.KEY_FIREBASE_TOKEN, token);
        preferencesEditor.apply();
    }

    public static String getFirebaseToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getString(Constants.KEY_FIREBASE_TOKEN, "");
    }

    public static void removeFirebaseToken(Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.remove(Constants.KEY_FIREBASE_TOKEN).apply();
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

    public static boolean hasContentType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.contains(Constants.KEY_CONTENT_TYPE);
    }

    public static void saveContentType(String contentType, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putString(Constants.KEY_CONTENT_TYPE, contentType);
        preferencesEditor.apply();
    }

    public static String getContentType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getString(Constants.KEY_CONTENT_TYPE, Constants.KEY_EVENTS_RUS);
    }

    public static void saveFilled(boolean isFilled, Context context) {
        SharedPreferences.Editor preferencesEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
        preferencesEditor.putBoolean(Constants.KEY_FILLED, isFilled);
        preferencesEditor.apply();
    }

    public static boolean isFilled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return preferences.getBoolean(Constants.KEY_FILLED, false);
    }
}