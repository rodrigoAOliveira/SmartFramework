package com.arcthos.arcthosmart.smartintegration;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Heitor Araujo on 11/08/2015.
 */
public class PreferencesManager {
    private static final String PREF_NAME = "SyncPreferences";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initialize(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    public void setValue(String key, String value) {
        mPref.edit()
                .putString(key, value)
                .apply();
    }

    public void setValue(String key, int value) {
        mPref.edit()
                .putInt(key, value)
                .apply();

    }

    public void setValue(String key, boolean value) {
        mPref.edit()
                .putBoolean(key, value)
                .apply();

    }

    public int getIntValue(String key) {
        return mPref.getInt(key, -1);
    }

    public String getStringValue(String key) {
        return mPref.getString(key, "");
    }

    public boolean getBooleanValue(String key) {
        return mPref.getBoolean(key, true);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public void clear() {
        mPref.edit()
                .clear()
                .apply();
    }
}