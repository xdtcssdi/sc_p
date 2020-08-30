package com.example.amicool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.bean.LoginBean;
import com.example.amicool.fragment.BaseFragment;
import com.example.amicool.iface.LoginListener;
import com.example.amicool.model.LoginModel;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etuser, etpass;
    private String username = "", password = "", sessionID = "";
    private Switch sw;

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onResponse(LoginBean loginBean) {
            sessionID = loginBean.getSessionid();
            System.out.println("----sessionID=" + sessionID);
            if (sessionID != null) {
                getSharedPreferences(BaseFragment.FILE, MODE_PRIVATE).edit().putString(BaseFragment.KEY_SESSION_ID,sessionID).apply();
                Toast.makeText(LoginActivity.this, "登录成功--sessionID=" + sessionID, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail(String msg) {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        SharedPreferences sp = getSharedPreferences("remeber",MODE_PRIVATE);
        String tmp=sp.getString("username","");
        etuser.setText(tmp);
        if(!"".equals(tmp)){
            sw.setChecked(true);
        }else{
            sw.setChecked(false);
        }
        etpass.setText(sp.getString("password",""));
    }

    private void init() {
        findViewById(R.id.btnlogin).setOnClickListener(this);
        findViewById(R.id.btnregister).setOnClickListener(this);
        etuser = findViewById(R.id.editText);
        etpass = findViewById(R.id.editText2);
        sw=findViewById(R.id.switch1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogin://登录
                username = etuser.getText().toString();
                password = etpass.getText().toString();
                if(sw.isChecked()){
                    getSharedPreferences("remeber",MODE_PRIVATE).edit()
                            .putString("username",etuser.getText().toString())
                            .putString("password",etpass.getText().toString()).apply();
                }else{
                    getSharedPreferences("remeber",MODE_PRIVATE).edit()
                            .remove("username").remove("password")
                            .apply();
                }
                LoginModel loginModel = new LoginModel();
                loginModel.getLoginResult(username, password, loginListener);
                break;
            case R.id.btnregister://注册
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            default:break;
        }
    }
}

