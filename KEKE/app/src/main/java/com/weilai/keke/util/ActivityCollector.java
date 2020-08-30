package com.weilai.keke.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }

        }
        activities.clear();
    }

    public static void finishAny(String className) {


        for (Activity activity : activities) {
            if (activity.getClass().getName().equals(className)) {
                activity.finish();
                return;
            }
        }
    }
}
