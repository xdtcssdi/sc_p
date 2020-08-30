package com.example.music163.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.music163.R;
import com.example.music163.activity.LoginActivity;
import com.example.music163.activity.StarListActivity;
import com.example.music163.bean.Success;
import com.example.music163.bean.User;
import com.example.music163.common.Url;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    @BindView(R.id.star)
    SuperTextView star;
    @BindView(R.id.username)
    SuperTextView username;

    @BindView(R.id.nickname)
    SuperTextView nickname;

    @BindView(R.id.phone)
    SuperTextView phone;

    String session;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_supertextview_common_use, container, false);
        ButterKnife.bind(this, rootView);
        context = rootView.getContext();
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", 0);
        session = userInfo.getString("session","");
        initView(rootView);
        return rootView;
    }

    private void initView(final View rootView) {

        EasyHttp.get("index/UserController/showDetail")
                .params("session", session)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        User user = JSON.parseObject(data, User.class);

                        username.setCenterString(user.getUsername());
                        nickname.setCenterString(user.getNickname());
                        phone.setCenterString(user.getMobilphone());

                        if ("http".equals(user.getAvatar())) {
                            Glide.with(rootView.getContext()).load(user.getAvatar()).crossFade().into(username.getLeftIconIV());
                        } else {
                            Glide.with(rootView.getContext()).load(Url.UpImg + user.getAvatar()).crossFade().into(username.getLeftIconIV());
                        }
                    }

                    @Override
                    public void onError(ApiException error) {
                        Log.d(TAG, "onError: " + error);
                    }
                });
    }


    @OnClick(R.id.username)
    public void setAva(){
        callReadPermission();
    }
    @OnClick(R.id.logout)
    public void logout(){
        EasyHttp.get("index/UserController/logout")
                .params("session", session)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Success success = JSON.parseObject(data,Success.class);
                        if (success.getSuccess()==1){
                            ActivityUtils.finishAllActivities();
                            ActivityUtils.startActivity(LoginActivity.class);
                        }else{
                            ToastUtils.showShort(success.getError());
                        }
                    }

                    @Override
                    public void onError(ApiException error) {
                        Log.d(TAG, "onError: " + error);
                    }
                });

    }

    public void callReadPermission() {
        String readstorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String writestorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String[] permissions = new String[]{readstorage, writestorage};
        int selfPermission = ActivityCompat.checkSelfPermission(context, readstorage);
        int selfwrite = ActivityCompat.checkSelfPermission(context, writestorage);

        if (selfPermission != PackageManager.PERMISSION_GRANTED || selfwrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            Matisse.from(getActivity())
                    .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                    .maxSelectable(1)
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(24);

        }
    }

    //处理申请权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                int gsize = grantResults.length;
                int flag = 0;
                for (int i = 0; i < gsize; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    ToastUtils.showShort("申请权限成功");
                } else {
                    ToastUtils.showShort("申请权限失败");
                }
                break;
        }
    }

    @OnClick(R.id.star)
    public void openStarList() {
        ActivityUtils.startActivity(StarListActivity.class);
    }
    @OnClick(R.id.phone)
    public void setPhone() {

        final IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage("发表中");
                return dialog;
            }
        };
        new MaterialDialog.Builder(context)
                .title("修改手机号")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input("",
                        "",
                        false,
                        ((dialog, input) -> {
                        }))
                .inputRange(11, 11)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive((dialog, which) -> EasyHttp.post("index/UserController/setMobilPhone")
                        .params("session", session)
                        .params("phone", dialog.getInputEditText().getText().toString())
                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                if (success.getSuccess() == 1) {
                                    ToastUtils.showShort("发布成功");
                                    phone.setCenterString(dialog.getInputEditText().getText().toString());
                                } else {
                                    ToastUtils.showShort(success.getError() + "");
                                }
                            }
                            @Override
                            public void onError(ApiException error) {
                                Log.d(TAG, "onFailed: " + error);
                            }
                        }))
                .cancelable(false)
                .show();

    }


    @OnClick(R.id.nickname)
    public void setNickName() {

        final IProgressDialog mProgressDialog = () -> {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("发表中");
            return dialog;
        };

        new MaterialDialog.Builder(context)
                .title("修改昵称")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input("",
                        "",
                        false,
                        ((dialog, input) -> {
                        }))
                .inputRange(4, 20)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive((dialog, which) -> EasyHttp.post("index/UserController/setNickName")
                        .params("session", session)
                        .params("nickname", dialog.getInputEditText().getText().toString())
                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                if (success.getSuccess() == 1) {
                                    ToastUtils.showShort("发布成功");
                                    nickname.setCenterString(dialog.getInputEditText().getText().toString());
                                } else {
                                    ToastUtils.showShort(success.getError() + "");
                                }
                            }
                            @Override
                            public void onError(ApiException error) {
                                Log.d(TAG, "onFailed: " + error);
                            }
                        }))
                .cancelable(false)
                .show();

    }

    @OnClick(R.id.pwd)
    public void setPwd(){
        final IProgressDialog mProgressDialog = () -> {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("修改中");
            return dialog;
        };

        new MaterialDialog.Builder(context)
                .title("修改密码")
                .inputType(
                        InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input("",
                        "",
                        false,
                        ((dialog, input) -> {
                        }))
                .inputRange(8, 20)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive((dialog, which) -> EasyHttp.post("index/UserController/setPWD")
                        .params("session", session)
                        .params("pwd", dialog.getInputEditText().getText().toString())
                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                if (success.getSuccess() == 1) {
                                    ToastUtils.showShort("修改成功");
                                } else {
                                    ToastUtils.showShort(success.getError() + "");
                                }
                            }
                            @Override
                            public void onError(ApiException error) {
                                Log.d(TAG, "onFailed: " + error);
                            }
                        }))
                .cancelable(false)
                .show();
    }


}
