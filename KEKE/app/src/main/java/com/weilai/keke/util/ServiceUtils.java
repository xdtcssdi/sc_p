package com.weilai.keke.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ServiceUtils {
    private static final String TAG = "c";

    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            Log.d(TAG, "isServiceRunning: " + runningService.get(i).service.getClassName().toString());
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean fonishService(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);

        for (int i = 0; i < runningService.size(); i++) {

            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {

                return true;
            }
        }
        return false;
    }
}