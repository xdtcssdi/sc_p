package com.example.music163.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.bean.Success;
import com.example.music163.bean.User;
import com.xuexiang.xui.widget.button.ButtonView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

public class LoginActivity extends BaseActivity {

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private ButtonView mRegisterButton;                   //注册按钮
    private ButtonView mLoginButton;                      //登录按钮
    private CheckBox mRememberCheck;
    private SharedPreferences login_sp;
    //不同按钮按下的监听事件选择
    View.OnClickListener mListener = v -> {
        switch (v.getId()) {
            case R.id.login_btn_register:                            //登录界面的注册按钮
                Intent intent_Login_to_Register = new Intent(LoginActivity.this, RegisterActivity.class);    //切换Login Activity至User Activity
                startActivity(intent_Login_to_Register);
                break;
            case R.id.login_btn_login:                              //登录界面的登录按钮
                login();
                break;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //通过id找到相应的控件
        mAccount = findViewById(R.id.login_edit_account);
        mPwd = findViewById(R.id.login_edit_pwd);
        mRegisterButton = findViewById(R.id.login_btn_register);
        mLoginButton = findViewById(R.id.login_btn_login);
        mRememberCheck = findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");
        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
    }

    private static final String TAG = "LoginActivity";
    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();

            EasyHttp.get("index/UserController/login")
                    .params("username", userName)
                    .params("password", userPwd)
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Success success = JSON.parseObject(data, Success.class);
                            User user = JSON.parseObject(data, User.class);
                           if (user.getSession()!=null) {
                               SharedPreferences.Editor editor = login_sp.edit();
                               editor.putString("USER_NAME", userName);
                               editor.putString("PASSWORD", userPwd);
                               editor.putString("session", user.getSession());
                               editor.putBoolean("mRememberCheck", mRememberCheck.isChecked());
                               editor.apply();
                               ActivityUtils.startActivity(MainActivity.class);
                               finish();
                           }else{
                               ToastUtils.showShort(success.getError());
                           }
                        }

                        @Override
                        public void onError(ApiException error) {
                            ToastUtils.showShort("收藏失败");
                        }
                    });

        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
