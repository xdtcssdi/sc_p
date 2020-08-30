package com.weilai.keke.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.alibaba.fastjson.JSON;
import com.weilai.keke.activity.JumpActivity;
import com.weilai.keke.entity.WhiteApp;
import com.weilai.keke.util.PreferencesKit;

import java.util.Set;

public class StopAppService extends AccessibilityService {
    private String myPackage = "com.weilai.keke";
    private static final String TAG = "StopAppService";
    private boolean isStop;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        isStop = PreferencesKit.getBoolean(getApplicationContext(),
                "isStopQAQ","isStop",true);
        Log.d(TAG, "onAccessibilityEvent: " + isStop);
        if(isStop)
            return;
        Log.d(TAG, "open: " + event.getPackageName());

        String app_list=PreferencesKit.getString(getApplicationContext(),
                "white_app_list","white_app_list","");

        Log.d(TAG, app_list);
        Log.d(TAG, "onAccessibilityEvent: " + event.getPackageName());

        if (event.getPackageName().toString().equals(myPackage)||
                event.getPackageName().toString().equals(("com.lbe.security.miui"))||
                event.getPackageName().toString().equals(("com.android.settings"))||
                event.getPackageName().toString().equals(("com.miui.home"))||
                event.getPackageName().toString().equals(("com.android.systemui"))
                ) {
            return;
        }
        if (!app_list.contains(event.getPackageName().toString())){
            Intent intent = new Intent(this, JumpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packagename",event.getPackageName().toString());
            startActivity(intent);
        }

    }

    @Override
    public void onInterrupt() {

    }

}
