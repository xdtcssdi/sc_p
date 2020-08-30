package com.weilai.keke.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.weilai.keke.R;
import com.weilai.keke.base.BaseActivity;
import com.weilai.keke.entity.LoginState;
import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.model.GetInfoModel;
import com.weilai.keke.model.LoginModel;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.service.listener.LoginListener;
import com.weilai.keke.util.ActivityCollector;
import com.weilai.keke.util.Common;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private Button btn_login;
    private EditText username, password;
    private TextView register, forget_password;
    private MaterialDialog.Builder progress;
    private SharedPreferences preferences;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCollector.activities.contains(LoginActivity.this)) {
            ActivityCollector.finishAll();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private Intent intent;
    private String sessionid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("loginState",MODE_PRIVATE);
        sessionid = preferences.getString("sessionid","nosessionid");

        intent = new Intent(LoginActivity.this, MainActivity.class);
        progress = new MaterialDialog.Builder(this)
                .title("提示")
                .content("登陆中")
                .progress(true, 0);
        if(!sessionid.equals("nosessionid")){
            md = progress.autoDismiss(false).cancelable(false).show();
            judgeLogin(preferences.getString("username",""),
                    preferences.getString("password",""));
        }


        forget_password = findViewById(R.id.forget_password);
        forget_password.setOnClickListener(v -> startActivity(new Intent(this, ForgetActivity.class)));
        btn_login = findViewById(R.id.btn_login);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        btn_login.setOnClickListener(v -> {
            md = progress.autoDismiss(false).cancelable(false).show();
            judgeLogin(username.getText().toString(), password.getText().toString());
        });
        register = findViewById(R.id.link_signup);
        register.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

    }

    private LoginListener loginListener = new LoginListener() {

        @Override
        public void onResponse(LoginState loginState) {
            if(loginState==null){
                onFail("登录失败");
            }
            if ("1".equals(loginState.getSuccess())) {
                if(loginState.getSessionid()==null){
                    onFail("获取session失败");
                    return;
                }
                getSharedPreferences("loginState",MODE_PRIVATE).edit()
                        .putString("sessionid",loginState.getSessionid())
                        .putString("username",usernameS)
                        .putString("password",passwordS).apply();

                GetInfoModel getInfoModel = new GetInfoModel();
                getInfoModel.getInfo("sessionid="+loginState.getSessionid(),getInfoListener);
            }else{
                onFail(loginState.getError());
            }
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(LoginActivity.this)
                    .title("登陆失败")
                    .content(msg).autoDismiss(false)
                    .negativeText("返回").onNegative((dialog, which) -> {
                dialog.dismiss();
            }).show();
            md.dismiss();
        }
    };
    private MaterialDialog md;
    GetInfoListener getInfoListener=new GetInfoListener() {
        @Override
        public void onResponse(UserInfo userInfo) {

            if(userInfo!=null){
                Common.user = userInfo;
                intent.putExtra("userinfo",userInfo);
                Log.d(TAG, userInfo.toString());
                try {
                    md.dismiss();
                }catch (Exception e){

                }
                startActivity(intent);
                finish();
            }else{
                onFail("访问失败");
            }
        }

        @Override
        public void onFail(String msg) {
            try {
                md.dismiss();
            }catch (Exception e){}
            new MaterialDialog.Builder(LoginActivity.this)
                    .title("警告")
                    .content(msg).autoDismiss(false)
                    .negativeText("返回").onNegative((dialog12, which12) -> {
                dialog12.dismiss();
            }).show();
        }
    };
    private String usernameS,passwordS;
    private void judgeLogin(String username, String password) {
        usernameS = username;
        passwordS = password;
        if("".equals(username)||"".equals(password)){
            new MaterialDialog.Builder(LoginActivity.this)
                    .title("警告")
                    .content("输入错误").autoDismiss(false)
                    .negativeText("返回").onNegative((dialog12, which12) -> {
                dialog12.dismiss();
            }).show();
        }else{
            LoginModel loginModel = new LoginModel();
            loginModel.login(username, password, loginListener);
        }
    }

}
