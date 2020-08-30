package com.weilai.keke.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weilai.keke.activity.MainActivity;
import com.weilai.keke.base.BaseApplication;
import com.weilai.keke.service.ClockService;
import com.weilai.keke.util.PreferencesKit;
import com.weilai.keke.util.ServiceUtils;

public class GoHomeBroadCast extends BroadcastReceiver {

    private static final String TAG = "GoHomeBroadCast";
    //监听进入home广播，如果计时服务已开启就关闭
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!ServiceUtils.isServiceRunning(context,"com.weilai.keke.service.ClockService"))
        {

            boolean isDown = PreferencesKit.getBoolean(BaseApplication.getContext(),"TimingClock","isDown",false);
            if(isDown) {
                Log.d(TAG, "start");
                context.startService(new Intent(context, ClockService.class));
            }
        }
    }
}
