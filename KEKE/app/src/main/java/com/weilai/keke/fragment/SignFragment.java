package com.weilai.keke.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weilai.keke.R;
import com.weilai.keke.base.BaseFragment;
import com.weilai.keke.broadcast.BootCompleteReceiver;
import com.weilai.keke.entity.SignEntity;
import com.weilai.keke.model.SignModel;
import com.weilai.keke.service.ClockService;
import com.weilai.keke.service.SearchAroundWifiService;

import com.weilai.keke.service.listener.GetSignListener;
import com.weilai.keke.util.AccessibilityUtil;

import com.weilai.keke.util.Common;
import com.weilai.keke.util.PreferencesKit;
import com.weilai.keke.util.ServiceUtils;
import com.weilai.keke.util.TimeUtil;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SignFragment extends BaseFragment implements View.OnClickListener {

    private boolean isDown;
    private String pur;
    private ImageView startClock;
    private TextView hintClock;
    private View view;
    private TextView showTime;
    private int second = 0;
    private int wifiScanCode = 1023;
    private static final String TAG = "TwoFragment";
    private Context context;
    public static final String ACTION_UPDATEUI = "action.updateUI";
    private UpdateUIBroadcastReceiver updateUIBroadcastReceiver;
    private BootCompleteReceiver bootCompleteReceiver;
    private AroundWifiReceiver broadcastReceiver;
    private String sessionid;
    private String now_time,week;
    private SignEntity signEntity;
    private TextView title,is_sign;

    private boolean first;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_layout, container, false);
        this.view = view;
        this.context = getContext();
        first = true;

        getCurrentTime();
        initComponet();

        sessionid = PreferencesKit.getString(context,"loginState",
                "sessionid","nosessionid");
        getSignTime();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        bootCompleteReceiver = new BootCompleteReceiver();
        context.registerReceiver(bootCompleteReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ACTION_UPDATEUI);
        updateUIBroadcastReceiver = new UpdateUIBroadcastReceiver();
        context.registerReceiver(updateUIBroadcastReceiver, filter1);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(SearchAroundWifiService.AroundWifiBroadcast);
        broadcastReceiver = new AroundWifiReceiver();
        context.registerReceiver(broadcastReceiver, filter2);

        return view;
    }

    public void getCurrentTime(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        week = ft.format(dNow);
        ft = new SimpleDateFormat("HH:mm:ss");
        now_time= ft.format(dNow);
        Log.d(TAG, "getCurrentTime: "+week+" "+now_time);
    }
    public void initComponet() {
        startClock = view.findViewById(R.id.startClock);
        startClock.setOnClickListener(this);
        showTime = view.findViewById(R.id.showTime);
        hintClock = view.findViewById(R.id.hintClock);
        title = view.findViewById(R.id.title);
        is_sign = view.findViewById(R.id.is_sign);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startClock:
                //检查权限
                //ServiceUtils.isServiceRunning(context,"");
                if (AccessibilityUtil.checkAccessibility(context)) {
                    if(!first) {
                        requestCallPermission();
                    }
                    if (!isDown) {
                        //开启签到 开启检查周围wifi服务
                        first=false;
                        isDown=true;
                        getSignTime();

                    } else {
                        //停止计时 关闭检查周围wifi服务
                        saveTimingClock();
                        stopApp(context);
                    }
                    Log.d(TAG, "onClick: " + hintClock.getText().toString());
                    saveIsDown();
                }
                break;

        }
    }

    public void stopApp(Context context){
        //关闭搜索wifi服务
        context.sendBroadcast(new Intent(Common.StopSearchWifi));
        //关闭计时功能
        context.sendBroadcast(new Intent(Common.StopClockService));
        //关闭禁止app
        PreferencesKit.saveBoolean(context,"isStopQAQ","isStop",true);
        isDown = false;
        pur = "点击上课签到";
        //时间下方的提示
        hintClock.setText("点击上课签到");
    }
    GetSignListener getSignListener = new GetSignListener() {
        @Override
        public void onResponse(SignEntity sign) {
            Log.d(TAG, "onResponse: "+sign);
            if(sign==null){
                onFail("获取失败");
                return;
            }
            signEntity = sign;
            try{
                second = Integer.parseInt(sign.getSign_time());
            }catch (NumberFormatException e){
                showTime.setText(TimeUtil.secondTo24(0));
                hintClock.setText("未到上课时间");
                startClock.setEnabled(false);
                md.dismiss();
                return;
            }

            Log.d(TAG, "onResponse: "+sign);
            if("0".equals(sign.getSuccess())){
                showTime.setText(TimeUtil.secondTo24(second));
                hintClock.setText("未到上课时间");
                startClock.setEnabled(false);
                md.dismiss();
                return;
            }

            title.setText(sign.getSign_date() + " " + sign.getClassAddress() + " " +
                    sign.getClassName()+ " " +sign.getTeacherName()+" 需要签到"+sign.getNeed_sign_time()+"秒");
            is_sign.setText(sign.isIs_sign()?"已签到":"未签到");

            isDown = PreferencesKit.getBoolean(context,"TimingClock","isDown", false);

            if (isDown) {
                //开启签到 开启检查周围wifi服务
                pur = "已签到时间";
                //时间下方的提示
                hintClock.setText(pur);
                //设置已经签到的时间
                showTime.setText(TimeUtil.secondTo24(Integer.parseInt(signEntity.getSign_time())));
                context.getApplicationContext().getSharedPreferences("isStopQAQ",Context.MODE_PRIVATE).edit().putBoolean("isStop",false).apply();
            } else {
                //停止计时 关闭检查周围wifi服务
                pur = "点击上课签到";
                //时间下方的提示
                hintClock.setText(pur);
                //设置已经签到的时间
                showTime.setText(TimeUtil.secondTo24(Integer.parseInt(signEntity.getSign_time())));
            }
            md.dismiss();
        }

        @Override
        public void onFail(String msg) {
            try {
                new MaterialDialog.Builder(context)
                        .title("警告")
                        .content(msg).autoDismiss(false)
                        .negativeText("返回").onNegative((dialog, which) -> {
                    dialog.dismiss();
                }).show();
            }catch (Exception ignored){
            }
            md.dismiss();
        }
    };
    private MaterialDialog md;
    private void getSignTime() {
        //加载签到时间，应该从服务器读取数据
        md = new MaterialDialog.Builder(context)
                .title("提示")
                .content("获取数据中")
                .progress(true, 0).show();
        SignModel signModel = new SignModel();
        signModel.getSignInfo(now_time,week,"sessionid="+ sessionid,getSignListener);
    }
    private void addSignTime(String sign_time){
        SignModel signModel = new SignModel();
        signModel.addSignInfo(now_time,week,sign_time,"sessionid="+ sessionid,getSignListener);
    }

    private void saveIsDown() {
        PreferencesKit.saveBoolean(context,"TimingClock","isDown", isDown);
    }

    public void saveTimingClock() {
        //保存签到时间，应该上传到服务器中
        addSignTime(second+"");
        PreferencesKit.saveBoolean(context,"TimingClock","isDown", false);
    }

    //请求wifiScan的权限
    private void requestCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //请求wifiScan的权限的权限
            String[] mPermissionList = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(context, mPermissionList)) {
                //已经同意过
                Log.d(TAG, "已经同意过");
                startMyServer(context);
                isDown = true;
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                Log.d(TAG, "未同意过,或者说是拒绝了，再次申请权限");
                EasyPermissions.requestPermissions(
                        SignFragment.this,  //上下文
                        "应用开启需要权限", //提示文言
                        wifiScanCode, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {
            //6.0以下，不需要授权
            Log.d(TAG, "6.0以下，不需要授权");
            startMyServer(context);
            isDown = true;
        }
    }


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.i(TAG, "onPermissionsGranted:" + requestCode + ":" + list.size());
        startMyServer(context);
        isDown = true;
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        Log.d(TAG, "onPermissionsDenied: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            Log.d(TAG, "onActivityResult: ");
            startMyServer(context);
            PreferencesKit.saveBoolean(context,"isStopQAQ","isStop",false);
            isDown = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        saveTimingClock();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveTimingClock();
        context.unregisterReceiver(bootCompleteReceiver);
        context.unregisterReceiver(updateUIBroadcastReceiver);
        context.unregisterReceiver(broadcastReceiver);
    }

    public class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            second = intent.getIntExtra("second", 0);
            showTime.setText(TimeUtil.secondTo24(second));
            is_sign.setText(Integer.parseInt(signEntity.getNeed_sign_time()) <= second ?"已签到":"未签到");
        }

    }

    public void startMyServer(Context context) {
        hintClock.setText("已签到时间");
        //开启搜索wifi功能
        if (!ServiceUtils.isServiceRunning(context, Common.SearchService)) {
            context.startService(new Intent(context, SearchAroundWifiService.class).putExtra("mac",signEntity.getMac()));
        }

        //开启计时功能
        if (!ServiceUtils.isServiceRunning(context, Common.ClockService)) {

            context.startService(new Intent(context, ClockService.class).putExtra("second",signEntity.getSign_time()).putExtra("sessionid",sessionid));
        }
    }
    public class AroundWifiReceiver extends BroadcastReceiver {
        private static final String TAG = "AroundWifiReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean flag = intent.getBooleanExtra("flag", false);

            if (flag) {
                Toast.makeText(context, "连接上wifi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "没有连接上wifi", Toast.LENGTH_SHORT).show();
                stopApp(context);
            }
        }
    }

}
