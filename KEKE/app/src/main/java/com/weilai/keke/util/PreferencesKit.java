package com.weilai.keke.util;

import android.content.Context;

public class PreferencesKit {

    public static String getString(Context context, String filename, String key,String default_str){
        return context.getSharedPreferences(filename,context.MODE_PRIVATE)
                .getString(key,default_str);
    }
    public static boolean getBoolean(Context context, String filename, String key,boolean default_bol){
        return context.getSharedPreferences(filename,context.MODE_PRIVATE)
                .getBoolean(key,default_bol);
    }
    public static int getInt(Context context, String filename, String key,int default_int){
        return context.getSharedPreferences(filename,context.MODE_PRIVATE)
                .getInt(key,default_int);
    }

    public static void saveString(Context context, String filename, String key,String value){
        context.getSharedPreferences(filename,context.MODE_PRIVATE).edit()
                .putString(key,value).apply();
    }
    public static void saveBoolean(Context context, String filename, String key,boolean value){
        context.getSharedPreferences(filename,context.MODE_PRIVATE).edit()
                .putBoolean(key,value).apply();
    }
    public static void saveInt(Context context, String filename, String key,int value){
        context.getSharedPreferences(filename,context.MODE_PRIVATE).edit()
                .putInt(key,value).apply();
    }

}
