package com.example.music163.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public String getSession() {
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        return userInfo.getString("session", "");
    }

    public String getUserName() {
        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
        return userInfo.getString("USER_NAME", "");
    }
}
