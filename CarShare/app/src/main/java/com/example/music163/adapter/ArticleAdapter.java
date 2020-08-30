package com.example.music163.adapter;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.music163.R;
import com.example.music163.activity.PublishActivity;
import com.example.music163.bean.Article;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class ArticleAdapter extends SmartRecyclerAdapter<Article> {
    public ArticleAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, final Article model, int position) {
        SuperTextView stv = holder.findViewById(R.id.super_cb_tv);
        stv.setLeftTopString("主题：" + model.getTitle());
        stv.setLeftBottomString(model.getContent());
        stv.setRightTopString("发帖时间：" + model.getTime());
        stv.setRightBottomString("发帖人：" + model.getUsername());
        stv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PublishActivity.class);
                intent.putExtra("aid", model.getAid());
                intent.putExtra("title", model.getTitle());
                intent.putExtra("content", model.getContent());
                ActivityUtils.startActivity(intent);

            }
        });
    }

}
