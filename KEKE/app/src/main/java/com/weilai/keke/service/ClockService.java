package com.weilai.keke.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weilai.keke.R;
import com.weilai.keke.activity.MainActivity;
import com.weilai.keke.base.BaseApplication;
import com.weilai.keke.entity.SignEntity;
import com.weilai.keke.fragment.SignFragment;
import com.weilai.keke.model.SignModel;
import com.weilai.keke.service.listener.GetSignListener;
import com.weilai.keke.util.PreferencesKit;
import com.weilai.keke.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockService extends Service {
    private int second;
    private String week,now_time;
    private String sessionid;
    public ClockService() {


        this.second = PreferencesKit.getInt(BaseApplication.getContext(),
                "TimingClock","second", 0);

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        week = ft.format(dNow);
        ft = new SimpleDateFormat("HH:mm:ss");
        now_time= ft.format(dNow);

        PreferencesKit.saveBoolean(BaseApplication.getContext(),
                "TimingClock","isDown", true);
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void getSignTime() {
        //加载签到时间，应该从服务器读取数据
        SignModel signModel = new SignModel();
        signModel.getSignInfo(now_time,week,"sessionid="+ sessionid,getSignListener);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            stopSelf();//在service中停止service
        }
    };
    private Thread thread;
    private boolean flag = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            builder.setContentText(TimeUtil.secondTo24(second));
            notificationManager.notify(1, builder.build());

            if (second % 30 == 0) {
                SignModel signModel = new SignModel();
                signModel.addSignInfo(now_time,week,second+"","sessionid="+ sessionid,getSignListener);
            }
            Intent intent = new Intent();
            intent.putExtra("second",second);
            intent.setAction(SignFragment.ACTION_UPDATEUI);
            sendBroadcast(intent);

        }
    };
    private IntentFilter filter = new IntentFilter();
    GetSignListener getSignListener = new GetSignListener() {
        @Override
        public void onResponse(SignEntity sign) {
            if(sign==null){
                onFail("访问失败");
                return;
            }
            if ("1".equals(sign.getSuccess())){
                Log.d(TAG, "onResponse: success");
            }
        }

        @Override
        public void onFail(String msg) {
            Log.d(TAG, "onFail: " + msg);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        second=Integer.parseInt(intent.getStringExtra("second"));
        sessionid = intent.getStringExtra("sessionid");
        getSignTime();
        thread = new Thread(() -> {
            try {
                while (flag) {
                    Thread.sleep(1000);
                    second++;
                    Message message = new Message();
                    handler.sendMessage(message);
                   }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        //保存签到时间，应该上传到服务器中
        addSignTime(second+"");
        unregisterReceiver(receiver);
        flag = false;
        thread.interrupt();
        super.onDestroy();
    }
    private void addSignTime(String sign_time){
        SignModel signModel = new SignModel();
        signModel.addSignInfo(now_time,week,sign_time,"sessionid="+ sessionid,getSignListener);
    }
    private Notification notification;
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private static final String TAG = "ClockService";

    @Override
    public void onCreate() {
        super.onCreate();

        filter.addAction("com.weilai.keke.stopClockService");
        registerReceiver(receiver, filter);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(TAG, "Clock onCreate: ");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder = new Notification.Builder(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ONE_ID = "com.weilai.keke.channel";
            String CHANNEL_ONE_NAME = "Channel One";
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            notification = builder.setContentTitle("计时中")
                    .setContentText(TimeUtil.secondTo24(second))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.icon_circle)
                    .setContentIntent(pi).setChannelId(CHANNEL_ONE_ID)
                    .build();
        }else{
            notification = builder.setContentTitle("计时中")
                    .setContentText(TimeUtil.secondTo24(second))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.icon_circle)
                    .setContentIntent(pi)
                    .build();
        }
        notification.contentIntent = pi;
        startForeground(1, notification);
    }
}
