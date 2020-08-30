package com.weilai.keke.fragment;

import android.content.Context;
import android.content.PeriodicSync;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.weilai.keke.R;
import com.weilai.keke.activity.LoginActivity;
import com.weilai.keke.adapter.MyClassAdapter;
import com.weilai.keke.base.BaseFragment;
import com.weilai.keke.entity.TeachingClassEntity;
import com.weilai.keke.model.GetMyClassModel;
import com.weilai.keke.service.listener.GetMyClassListener;
import com.weilai.keke.util.PreferencesKit;

import java.util.ArrayList;


public class ScheduleFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private String sessionid;
    private Context context;
    private LinearLayoutManager layoutManager;
    private MaterialDialog md;
    private TeachingClassEntity teachingClassEntity;
    private MyClassAdapter myClassAdapter;
    private RefreshLayout refresh_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_layout, container, false);
        recyclerView = view.findViewById(R.id.recy);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        context = view.getContext();
        sessionid = PreferencesKit.getString(context,"loginState",
                "sessionid","nosessionid");
        layoutManager = new LinearLayoutManager(context);
        teachingClassEntity = new TeachingClassEntity();
        teachingClassEntity.setData(new ArrayList<>());

        myClassAdapter = new MyClassAdapter(teachingClassEntity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myClassAdapter);
        md = new MaterialDialog.Builder(context)
                .title("提示")
                .content("获取数据中")
                .progress(true, 0).show();
        GetMyClassModel getMyClassModel = new GetMyClassModel();

//        refresh_layout.setRefreshHeader(new BezierRadarHeader(getContext()).setEnableHorizontalDrag(true));
        refresh_layout.autoRefresh();//自动刷新
        refresh_layout.setEnableRefresh(true);//是否启用下拉刷新功能
        refresh_layout.setEnableLoadMore(false);//是否启用上拉加载功能
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMyClassModel.get_my_class("sessionid="+sessionid,getMyClassListener);
            }
        });


        return view;
    }

    private GetMyClassListener getMyClassListener =new GetMyClassListener() {
        @Override
        public void onResponse(TeachingClassEntity teaching) {
            refresh_layout.closeHeaderOrFooter();
            if (teachingClassEntity==null||teachingClassEntity.getData()==null){
                onFail("获取数据失败");
                refresh_layout.closeHeaderOrFooter();
                return;
            }
            teachingClassEntity.getData().clear();
            teachingClassEntity.setData(teaching.getData());
            myClassAdapter.notifyDataSetChanged();
            md.dismiss();
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(context)
                    .title("错误")
                    .content("获取数据失败").autoDismiss(false)
                    .negativeText("返回").onNegative((dialog, which) -> {
                dialog.dismiss();
            }).show();
            md.dismiss();
        }
    };
}
