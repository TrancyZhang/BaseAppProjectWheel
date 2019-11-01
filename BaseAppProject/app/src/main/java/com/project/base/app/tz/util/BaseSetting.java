package com.project.base.app.tz.util;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseSetting {

    public static final String SETTING_FILE = "com.project.base.app.tz.setting";
    public static final String SETTING_BASE64 = "com.project.base.app.tz.base64";

    public static void setIntAttr(Context context, String key, int value) {
        SharedPreferences.Editor editor = getBaseSettingEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntAttr(Context context, String key, int defaultValue) {
        return getBaseSettingShared(context).getInt(key, defaultValue);
    }

    public static void setStringAttr(Context context, String key, String value) {
        SharedPreferences.Editor editor = getBaseSettingEditor(context);
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
        editor.commit();
    }

    public static String getStringAttr(Context context, String key, String defaultValue) {
        return getBaseSettingShared(context).getString(key, defaultValue);
    }

    public static void setBooleanAttr(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getBaseSettingEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanAttr(Context context, String key, boolean defaultValue) {
        return getBaseSettingShared(context).getBoolean(key, defaultValue);
    }

    public static SharedPreferences.Editor getBaseSettingEditor(Context context) {
        return getBaseSettingShared(context).edit();
    }

    public static SharedPreferences getBaseSettingShared(Context context) {
        return context.getSharedPreferences(SETTING_FILE, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getBaseSettingShareds(Context context) {
        return context.getSharedPreferences(SETTING_BASE64, Context.MODE_PRIVATE);
    }

}
