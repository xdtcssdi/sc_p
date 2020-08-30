package com.example.amicool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amicool.R;
import com.example.amicool.activities.ViewArticleActivity;
import com.example.amicool.bean.ProjectBean;
import com.example.amicool.bean.TcaseBean;
import com.example.amicool.common.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ProjectAdapter extends RecyclerView.Adapter {
    private List<ProjectBean> list;//向rv中填充的数据
    private Context context;//上下文
    private LayoutInflater layoutInflater;//动态布局

    //自定义 构造方法
    public ProjectAdapter(Context context)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    //自定义 ViewHolder子类，容纳item视图
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvtitle,tvauthor,tvtime;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tvtitle=itemView.findViewById(R.id.textView);
            tvauthor=itemView.findViewById(R.id.textView2);
            tvtime=itemView.findViewById(R.id.textView3);
        }
    }
    //自定义 设置数据list
    public void setList(List<ProjectBean> list)
    {
        this.list=list;
        notifyDataSetChanged();//通知RV刷新数据
    }


    @NonNull
    @Override  //为每一个item生成一个view
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(
                R.layout.item,parent,false);

        return new ViewHolder(v);
    }

    @Override  //为每个item填充数据，设置点击事件
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ProjectBean bean=list.get(position);
        if(bean==null) {
            return;
        }
        ViewHolder viewHolder=(ViewHolder)holder;
        Picasso.with(context)
                .load(Common.IMAGEURL+bean.getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.imageView);
        viewHolder.tvtitle.setText(bean.getName());

        viewHolder.tvtime.setText(bean.getUpdate_time());

        //item条目点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取出当前item的id
                Intent intent=new Intent(context, ViewArticleActivity.class);
                intent.putExtra("resid",list.get(position).getId());
                intent.putExtra("type",3);
                intent.putExtra("userid",list.get(position).getUserid());
                context.startActivity(intent);
            }
        });

    }

    @Override  //确定item个数
    public int getItemCount() {
        if(list!=null) {
            return list.size();
        } else {
            return 0;
        }
    }
}

