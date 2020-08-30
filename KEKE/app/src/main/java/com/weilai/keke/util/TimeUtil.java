package com.weilai.keke.util;

public class TimeUtil {
    public static String secondTo24(int second) {
        int hours = Math.round(second / 3600);
        int minutes = Math.round(second / 60) % 60;
        int seconds = second % 60;
        return (hours < 10 ? "0" + hours : hours) + ":"
                + (minutes < 10 ? "0" + minutes : minutes) + ":"
                + (seconds < 10 ? "0" + seconds : seconds);
    }

}
