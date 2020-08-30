package com.weilai.keke.base;

import android.app.Application;
import android.content.Context;

import io.rong.imkit.RongIM;

public class BaseApplication extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}