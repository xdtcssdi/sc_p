package com.weilai.keke.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weilai.keke.entity.WhiteApp;
import com.weilai.keke.model.GetWhiteAppModel;
import com.weilai.keke.service.listener.GetWhiteAppListener;
import com.weilai.keke.util.PreferencesKit;
import com.weilai.keke.util.SessionIdUtil;

public class WhiteAppListReceiver extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String sessionid = SessionIdUtil.getSession();
        Log.d(TAG, "onReceive: " +sessionid);
        GetWhiteAppModel getWhiteAppModel = new GetWhiteAppModel();
        getWhiteAppModel.getWhiteApp("sessionid="+sessionid, getWhiteAppListener);
    }

    private static final String TAG = "WhiteAppListReceiver";
    private GetWhiteAppListener getWhiteAppListener = new GetWhiteAppListener() {
        @Override
        public void onResponse(String whiteApp) {
            if(whiteApp==null){
                onFail("获取错误");
                return;
            }
            PreferencesKit.saveString(context,"white_app_list","white_app_list",whiteApp);
            Log.d(TAG, "onResponse: "+whiteApp);

        }

        @Override
        public void onFail(String msg) {
            Log.d(TAG, "onFail: "+msg);
        }
    };
}
