package com.ceyu.carsteward.common.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceManager {
    public static SharedPreferences sharePreference;
    public static Editor edit;

    private SharePreferenceManager(Context context) {
        super();
        if (sharePreference == null) {
            String preferenceFileName = "Dnurse_preference";
            sharePreference = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        }
    }

    public static String getName(String uid, String key) {
        return uid + "_" + key;
    }

    public static SharePreferenceManager newInstance(Context context) {
        return new SharePreferenceManager(context);
    }

    public void setValue(String name, String value) {
        edit = sharePreference.edit();
        edit.putString(name, value);
        edit.apply();
    }

    public void setValue(String name, boolean value) {
        edit = sharePreference.edit();
        edit.putBoolean(name, value);
        edit.apply();
    }

    public void setValue(String name, int value) {
        edit = sharePreference.edit();
        edit.putInt(name, value);
        edit.apply();
    }

    public void setValue(String name, float value) {
        edit = sharePreference.edit();
        edit.putFloat(name, value);
        edit.apply();
    }

    public void setValue(String name, long value) {
        edit = sharePreference.edit();
        edit.putLong(name, value);
        edit.commit();
    }

    public boolean getBooeanValue(String name) {
        return sharePreference.getBoolean(name, false);
    }

    public int getIntValue(String name) {
        return sharePreference.getInt(name, 0);
    }
    public int getIntValue(String name,int defaultValues) {
        return sharePreference.getInt(name,defaultValues);
    }

    public String getStringValue(String name) {
        return sharePreference.getString(name, "");
    }

    public float getFloatValue(String name) {
        return sharePreference.getFloat(name, 0f);
    }

    public long getLongValue(String name) {
        return sharePreference.getLong(name, 0l);
    }

}
