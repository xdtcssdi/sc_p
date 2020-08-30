package com.example.music163.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.adapter.CarAdapter;
import com.example.music163.bean.Car;
import com.example.music163.util.DynamicTimeFormat;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class CarFragment extends Fragment {

    private static final String TAG = "BrandFragment";
    @BindView(R.id.recv)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private Context context;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private SmartRecyclerAdapter<Car> mAdapter;
    private int o_page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brand, container, false);
        ButterKnife.bind(this, rootView);
        context = rootView.getContext();
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {

        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - delta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new CarAdapter(R.layout.car_item));
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            o_page = 1;
                            mAdapter.getListData().clear();
                            mAdapter.notifyListDataSetChanged();
                            getData(o_page, 0, refreshLayout);
                        }
                    }, 1000);
                }
            });
            //上拉加载
            mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData(++o_page, 1, refreshLayout);
                        }
                    }, 1000);
                }
            });
            //触发自动刷新
            mRefreshLayout.autoRefresh();
        }

        mRefreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary));
            mDrawableProgress.setTint(0xffffffff);
        }

    }

    public void getData(final int page, final int flag, final RefreshLayout refreshLayout) {
        EasyHttp.get("index/CarController/showCarList").params("page", page + "")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        List<Car> cars = JSON.parseArray(data, Car.class);
                        if (cars.size() == 0) {
                            ToastUtils.showShort("已经到底部~\\(≧▽≦)\\~啦啦啦");
                            o_page = 1;
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                            return;
                        }
                        //if (page!=1) ToastUtils.showShort("加载下一页");
                        mAdapter.loadMore(cars);
                        if (flag == 0) {
                            refreshLayout.finishRefresh();
                            refreshLayout.resetNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(ApiException error) {
                        if (flag == 0) {
                            refreshLayout.finishRefresh();
                            refreshLayout.resetNoMoreData();
                        } else {

                            refreshLayout.finishLoadMore();
                        }
                    }
                });

    }
}
