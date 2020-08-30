package com.weilai.keke.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.weilai.keke.R;
import com.weilai.keke.activity.LoginActivity;

import com.weilai.keke.base.BaseFragment;
import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.model.GetInfoModel;
import com.weilai.keke.model.LogoutModel;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.service.listener.SuccessListener;
import com.weilai.keke.util.ActivityCollector;
import com.weilai.keke.util.Common;
import com.weilai.keke.util.PreferencesKit;
import com.weilai.keke.util.SessionIdUtil;

import pub.devrel.easypermissions.AppSettingsDialog;

import static android.content.Context.MODE_PRIVATE;

public class UserDataFragment extends BaseFragment implements View.OnClickListener {

    private TextView userName;
    private TextView userid;
    private TextView userIdName;
    private TextView email;
    private TextView phone;
    private Button exitLogin;
    private String sessionid;
    private Context context;
    private RoundedImageView avatar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_layout, container, false);
        initView(view);
        context = getContext();

        updateUI(SessionIdUtil.getSession());
        return view;
    }

    private static final String TAG = "UserDataFragment";

    public void initView(View view) {
        userName = view.findViewById(R.id.tv_name);
        userid = view.findViewById(R.id.tv_constellation);
        userIdName = view.findViewById(R.id.tv_user);
        email = view.findViewById(R.id.tv_email);
        phone = view.findViewById(R.id.tv_phone);
        exitLogin = view.findViewById(R.id.btn_exit);
        exitLogin.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
    }
    GetInfoListener getInfoListener=new GetInfoListener() {
        @Override
        public void onResponse(UserInfo dataBean) {
            if(dataBean!=null){
                userName.setText(dataBean.getUsername());
                phone.setText(dataBean.getTelephone());
                userid.setText(dataBean.getStuNo());
                userIdName.setText(dataBean.getNickname());
                email.setText(dataBean.getEmail());
                md.dismiss();
            }else{
                onFail("访问失败");
            }
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(context)
                    .title("警告")
                    .content(msg).autoDismiss(false)
                    .negativeText("返回").onNegative((dialog12, which12) -> {
                dialog12.dismiss();
            }).show();
            md.dismiss();
        }
    };

    SuccessListener successListener = new SuccessListener() {
        @Override
        public void onResponse(SuccessEntity success) {
            if (success==null){
                onFail("服务器登出失败");
                return;
            }
            if (!"1".equals(success.getSuccess())){
                onFail(success.getError());
            }
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

        }
    };
    private MaterialDialog md;
    public void updateUI(String sessionId) {

        md = new MaterialDialog.Builder(context)
                .title("提示")
                .content("获取数据中")
                .progress(true, 0).show();

        GetInfoModel getInfoModel = new GetInfoModel();
        getInfoModel.getInfo("sessionid="+sessionId,getInfoListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                startActivityForResult(
                        new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),1
                );
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d(TAG, "onActivityResult: ");
            LogoutModel logoutModel = new LogoutModel();
            logoutModel.logout("sessionid="+SessionIdUtil.getSession(),successListener);

            SessionIdUtil.removeSession();

            startActivity(new Intent(getContext(), LoginActivity.class));

            //关闭搜索wifi服务
            context.sendBroadcast(new Intent("com.weilai.keke.stopSearchWifi"));
            //关闭计时功能
            context.sendBroadcast(new Intent("com.weilai.keke.stopClockService"));
            //关闭禁止app功能
            context.sendBroadcast(new Intent("com.weilai.keke.StopApp"));

            PreferencesKit.saveBoolean(context,"isStopQAQ","isStop",true);

            PreferencesKit.saveBoolean(context,"TimingClock","isDown", false);

            ActivityCollector.finishAll();
        }
    }

}
