package com.example.music163.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
public class RegisterActivity extends AppCompatActivity {

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private ButtonView mSureButton;                       //确定按钮
    private ButtonView mCancelButton;                     //取消按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAccount = findViewById(R.id.resetpwd_edit_name);
        mPwd = findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = findViewById(R.id.register_btn_sure);
        mCancelButton = findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

    }
    //不同按钮按下的监听事件选择
    View.OnClickListener m_register_Listener = v -> {
        switch (v.getId()) {
            case R.id.register_btn_sure:                       //确认按钮的监听事件
                register_check();
                break;
            case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                Intent intent_Register_to_Login = new Intent(RegisterActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Register_to_Login);
                finish();
                break;
        }
    };
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();


            if(!userPwd.equals(userPwdCheck)){     //两次密码输入不一样
                Toast.makeText(this, "两次密码不相同",Toast.LENGTH_SHORT).show();
            } else {
                //访问网络
                EasyHttp.get("index/UserController/register")
                        .params("username", userName)
                        .params("password", userPwd)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                User2 user = JSON.parseObject(data, User2.class);
                                if (user.getUsername()!=null) {
                                    ToastUtils.showShort("注册成功，欢迎："+userName);
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
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名不可为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不可为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, "确认密码不可为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
class User2{

    /**
     * username : test123456
     * password : 16d7a4fca7442dda3ad93c9a726597e4
     * uid : 15
     */

    private String username;
    private String password;
    private String uid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
