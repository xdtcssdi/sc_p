package com.example.music163.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.example.music163.R;
import com.example.music163.activity.BrandDetailActivity;
import com.example.music163.bean.Brand;
import com.example.music163.common.Url;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;

public class BrandAdapter extends SmartRecyclerAdapter<Brand> {

    public BrandAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, final Brand model, int position) {

        TextView bname = holder.findViewById(R.id.brand_name);
        bname.setText(model.getName());


        Glide.with(holder.itemView.getContext()).load(Url.CheBiao + model.getImg()).crossFade().into((ImageView) holder.findViewById(R.id.logo));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BrandDetailActivity.class);
                intent.putExtra("bid", model.getBid());
                ActivityUtils.startActivity(intent);
            }
        });
    }
}