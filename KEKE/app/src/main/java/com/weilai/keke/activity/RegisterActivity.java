package com.weilai.keke.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weilai.keke.R;
import com.weilai.keke.base.BaseActivity;
import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.model.RegisterModel;
import com.weilai.keke.service.listener.RegisterListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private EditText input_name, input_password, input_repassword, input_person_id;
    private Button btn_signup;
    private TextView link_signup;
    private MaterialDialog.Builder progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        progress = new MaterialDialog.Builder(this)
                .title("提示")
                .content("注册中")
                .progress(true, 0);
        input_name = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        input_person_id = findViewById(R.id.person_id);
        input_repassword = findViewById(R.id.input_repassword);
        btn_signup = findViewById(R.id.btn_signup);
        link_signup = findViewById(R.id.link_login);
        btn_signup.setOnClickListener(this);
        link_signup.setOnClickListener(this);
    }

    RegisterListener registerListener=new RegisterListener() {
        @Override
        public void onResponse(SuccessEntity registerBean) {
            if (registerBean!=null && "1".equals(registerBean.getSuccess())) {
                new MaterialDialog.Builder(RegisterActivity.this)
                        .title("提示")
                        .content("注册成功").autoDismiss(true)
                        .positiveText("返回登陆页").onPositive((dialog12, which12) -> {
                    dialog12.dismiss();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }).show();
            }else if(registerBean!=null){
                onFail("注册失败"+registerBean.getError());
            }
            if(registerBean==null){
                onFail("网络连接失败");
            }
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(RegisterActivity.this)
                    .title("提示")
                    .content(msg).autoDismiss(true)
                    .positiveText("重试")
                    .show();
        }
    };
    private MaterialDialog md;
    private void judgeRegister(String username, String password,String num) {
        md = progress.autoDismiss(false).cancelable(false).show();
        if("".equals(username)||"".equals(num)||
                "".equals(password)){
            new MaterialDialog.Builder(RegisterActivity.this)
                    .title("警告")
                    .content("输入不能为空").autoDismiss(false)
                    .positiveText("返回").onPositive((dialog12, which12) -> dialog12.dismiss()).show();
        }else{
            RegisterModel registerModel=new RegisterModel();
            registerModel.register(username,password,num,registerListener);
        }
        md.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                String username = input_name.getText().toString();
                String password = input_password.getText().toString();
                String person_id = input_person_id.getText().toString();
                if (!password.equals(input_repassword.getText().toString())) {

                    new MaterialDialog.Builder(RegisterActivity.this)
                            .title("注册失败")
                            .content("两次密码不相同").autoDismiss(false)
                            .negativeText("返回").onNegative((dialog12, which12) -> dialog12.dismiss()).show();
                    return;
                }
                judgeRegister(username,password,person_id);
                break;

            case R.id.link_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
