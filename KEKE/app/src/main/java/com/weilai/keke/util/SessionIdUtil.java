package com.weilai.keke.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.weilai.keke.base.BaseApplication;

public class SessionIdUtil {

    public static String getSession() {
        return PreferencesKit.getString(BaseApplication.getContext(),
                "loginState","sessionid","nosessionid");
    }

    public static void removeSession() {
        PreferencesKit.saveString(BaseApplication.getContext(),"loginState","sessionid", "nosessionid");
    }

}
