package com.weilai.keke.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.weilai.keke.R;
/**
 * 跳转的页面
 */
public class JumpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        TextView viewById = findViewById(R.id.textView);
        viewById.setText(getIntent().getStringExtra("packagename")+"\n已被禁止使用");;

    }
}
