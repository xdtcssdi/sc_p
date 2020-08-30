package com.weilai.keke.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


@SuppressWarnings("ALL")
public class SearchAroundWifiService extends Service {

    private static final String TAG = "SearchAroundWifiService";

    public static final String AroundWifiBroadcast = "com.weilai.keke.AroundWifiBroadcast";

    private Thread thread;
    private boolean flag = true;
    private IntentFilter filter = new IntentFilter();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressWarnings("AlibabaRemoveCommentedCode")
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();//在service中停止service
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        filter.addAction("com.weilai.keke.stopSearchWifi");

        registerReceiver(receiver, filter);
    }
    private String mac;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mac = intent.getStringExtra("mac");
        Log.d(TAG, "onStartCommand: " + mac);
        if (intent != null) {

            thread = new Thread(() -> {

                try {
                    while (true) {
                        Thread.sleep(5000);
                        Intent intent1 = new Intent(AroundWifiBroadcast);
                        if (isAround()) {
                            intent1.putExtra("flag", true);
                        } else {
                            intent1.putExtra("flag", false);
                        }
                        sendBroadcast(intent1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public boolean isAround() {
//        List<ScanResult> list = null;
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        list = wifiManager.getScanResults();
//        if (list == null || list.size() == 0) {
//            return false;
//        }
//
//        for (ScanResult s : list) {
//            Log.d(TAG, s.SSID + " " + s.BSSID);
//        }
//        for (ScanResult s : list) {
//            if (s.BSSID.equals(mac)) {
//                return true;
//            }
//        }
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d(TAG, "isAround: " + wifiInfo.getBSSID());
        if (wifiInfo.getBSSID()!=null&&wifiInfo.getBSSID().toUpperCase().equals(mac)){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        thread.interrupt();
        unregisterReceiver(receiver);
        flag = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
