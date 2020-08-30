package com.example.music163.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.example.music163.R;
import com.example.music163.activity.CarDetailActivity;
import com.example.music163.bean.Car;
import com.example.music163.common.Url;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class CarAdapter extends SmartRecyclerAdapter<Car> {


    public CarAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder viewHolder, final Car item, int position) {

        SuperTextView stv = viewHolder.findViewById(R.id.content);
        stv.setLeftTopString(item.getBname());
        stv.setRightBottomString(item.getTypename());
        stv.setRightTopString(item.getCname());
        stv.setLeftBottomString(item.getDetail());

        if (item.getMainimg().startsWith("http")) {
            Glide.with(viewHolder.itemView.getContext()).load(item.getMainimg()).crossFade().into((ImageView) viewHolder.findViewById(R.id.img));
        } else {
            Glide.with(viewHolder.itemView.getContext()).load(Url.UpImg + item.getMainimg()).crossFade().into((ImageView) viewHolder.findViewById(R.id.img));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CarDetailActivity.class);
                intent.putExtra("bid", item.getCid());
                ActivityUtils.startActivity(intent);
            }
        });
    }
}