package com.example.amicool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.adapter.ProjectAdapter;
import com.example.amicool.adapter.TcaseAdapter;
import com.example.amicool.bean.ProjectBean;
import com.example.amicool.bean.TcaseBean;
import com.example.amicool.iface.ProjectListener;
import com.example.amicool.iface.TcaseListener;
import com.example.amicool.model.ProjectModel;
import com.example.amicool.model.TcaseModel;

import java.util.List;

public class Fragment42 extends BaseFragment {
    private List<ProjectBean> list;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProjectAdapter adapter;
    private int page=1; // 代表页数，并初始化为1，代表第1页。
    private int lastVisibleItemPosition;//最后一条可见条目的位置
    private ProjectListener listener = new ProjectListener() {

        @Override
        public void onResponse(List<ProjectBean> beanlist) {
            if(page==1)
            {
                list=beanlist;
            }
            else {
                list.removeAll(beanlist);
                list.addAll(beanlist);
            }

            adapter.setList(list);
        }

        @Override
        public void onFail(String msg) {
            Toast.makeText(getContext(), "失败："+msg, Toast.LENGTH_SHORT).show();

        }
    };

    public Fragment42() {    }
    @Nullable
    @Override //生命周期方法，创建View
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment1,container,false);
    }
    @Override//生命周期方法，View创建完成
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("--f1--"+getSessionId());
        initRecyclerView(view);
        ProjectModel model=new ProjectModel();
        model.getResultList("project",page,getSessionId(),listener);
    }
    private void initRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        //每个item如果是确定高度，设置此项提高性能
        recyclerView.setHasFixedSize(true);
        //实例化适配器
        adapter=new ProjectAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
        //下拉更新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == list.size()) {
                    page += 1;
                    //再次实例化ArticleModel，调用方法获取网络数据，请求新一页数据
                    ProjectModel tcaseModel = new ProjectModel();
                    tcaseModel.getResultList("project", page, getSessionId(), listener);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();//滚动结束后将赋值为可见条目中最后一条位置
            }
        });

    }
}
