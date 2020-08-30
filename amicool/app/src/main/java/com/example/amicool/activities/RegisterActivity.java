package com.example.amicool.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.iface.RegisterListener;
import com.example.amicool.model.RegisterModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initviews();
    }

    private static final String TAG = "RegisterActivity";
    private RegisterListener registerListener = new RegisterListener() {
        @Override
        public void onResponse(String call) {

            if (!call.equals("1")) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail(String msg) {

        }
    };

    private EditText username, password, tel, email;
    private Button register;
    private Switch type;
    private int tt = 3;
    private RegisterModel registerModel = new RegisterModel();

    public void initviews() {
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        tel = findViewById(R.id.et_tel);
        email = findViewById(R.id.et_email);
        type = findViewById(R.id.roleid);

        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tt = 2;
                } else {
                    tt = 3;
                }
            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerModel.getRegisterResult(username.getText().toString(), password.getText().toString(), tel.getText().toString(), String.valueOf(tt), email.getText().toString(), registerListener);
            }
        });


    }
}
