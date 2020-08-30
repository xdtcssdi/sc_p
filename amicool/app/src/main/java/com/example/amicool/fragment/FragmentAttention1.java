package com.example.amicool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.adapter.AVideoAdapter;
import com.example.amicool.bean.VideoBean;
import com.example.amicool.iface.AttentionListListener;
import com.example.amicool.model.AVideoModel;

import java.util.List;

public class FragmentAttention1 extends BaseFragment {
    private View view = null;
    private Context context;
    private String userId;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;//显示布局效果
    private AVideoAdapter adapter;//适配器
    private List<VideoBean> list = null;//数据源

    private String sessionID;

    private int page = 1;// 代表页数，并初始化为1，代表第1页。
    private int lastVisibleItemPosition;//最后一条可见条目的位置
    private static final String TAG = "FragmentAttent1";
    AttentionListListener<VideoBean> listListener = new AttentionListListener<VideoBean>() {
        @Override
        public void onResponse(List<VideoBean> beanlist) {
            if(beanlist==null) {
                return;
            }

            if (page == 1) {
                list = beanlist;
            } else {
                list.removeAll(beanlist);
                list.addAll(beanlist);
            }
            adapter.setList(list);//传给adapter
            adapter.notifyDataSetChanged();//通知更新
        }

        @Override
        public void onFail(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        userId = getActivity().getIntent().getStringExtra("userId");
        Log.d(TAG, userId);
        //动态加载Fragment1的布局文件
        view = inflater.inflate(R.layout.fragment1, container, false);

        readSP();
        Log.d(TAG, "onCreateView: " +sessionID);
        System.out.println("----onCreateView page=" + page);

        //实例化ArticleModel，调用方法获取网络数据
        AVideoModel aVideoModel = new AVideoModel();
        aVideoModel.getResultList("video", page, sessionID,userId, listListener);

        initRecyclerView();
        //返回动态生成的view
        return view;
    }

    private void readSP() {
        sessionID = getSessionId();
    }

    private void initRecyclerView() {
        //获取RecyclerView，设置属性，获取数据源，绑定
        recyclerView = view.findViewById(R.id.recyclerview);
        //创建默认的线性布局
        layoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //固定每个item高度，提高性能
        recyclerView.setHasFixedSize(true);
        //创建Adaper
        adapter = new AVideoAdapter(context);
        adapter.setList(list);
        //绑定RecyclerView和adapter
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == list.size()) {
                    page += 1;
                    //aVideoModel，调用方法获取网络数据，请求新一页数据
                    AVideoModel aVideoModel = new AVideoModel();
                    aVideoModel.getResultList("video", page, sessionID,userId, listListener);
                    System.out.println("----onScrollStateChanged  page=" + page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();//滚动结束后将赋值为可见条目中最后一条位置
                //lastVisibleItemPosition = list.size() - 1;
            }
        });

    }
}
