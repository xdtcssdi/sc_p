package com.example.music163.adapter;

import com.example.music163.R;
import com.example.music163.bean.Comment;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class CommentAdapter extends SmartRecyclerAdapter<Comment> {
    public CommentAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, final Comment model, int position) {
        SuperTextView stv = holder.findViewById(R.id.super_cb_tv);
        stv.setRightBottomString("发帖人：" + model.getUsername());
        stv.setLeftTopString(model.getComment());
    }

}
