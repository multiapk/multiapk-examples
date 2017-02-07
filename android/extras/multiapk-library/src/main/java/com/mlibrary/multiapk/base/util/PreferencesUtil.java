//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mlibrary.multiapk.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {
    private static final String DATA_NAME = "mlibrarypatch.preferences";
    private static PreferencesUtil instance;
    private SharedPreferences mSharedPreferences;

    private PreferencesUtil() {
    }

    public static synchronized PreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtil();
            instance.mSharedPreferences = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public boolean getBoolean(String name, boolean bDefault) {
        return this.mSharedPreferences.getBoolean(name, bDefault);
    }

    public boolean putBoolean(String name, boolean value) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(name, value);
        return editor.commit();
    }

    public boolean putString(String key, String value) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key) {
        return this.mSharedPreferences.getString(key, "");
    }

    public boolean putInt(String key, int value) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key, int iDefault) {
        return this.mSharedPreferences.getInt(key, iDefault);
    }

    public boolean putLong(String key, Long value) {
        Editor editor = this.mSharedPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public Long getLong(String key, Long defaultValue) {
        return this.mSharedPreferences.getLong(key, defaultValue);
    }
}
