package com.example.music163.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.adapter.CarAdapter;
import com.example.music163.bean.Car;
import com.example.music163.util.DynamicTimeFormat;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
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

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SearchResultActivity extends BaseActivity {


    private static final String TAG = "SearchResultActivity";
    @BindView(R.id.tb_main_title_bar)
    public TitleBar titleBar;
    @BindView(R.id.recv)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private Context context;
    private String brand;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private SmartRecyclerAdapter<Car> mAdapter;
    private int o_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);
        context = this;
        brand = getIntent().getStringExtra("brand");
        initView();
    }

    public void initView() {
        titleBar.setTitle(brand);

        titleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {
            }

            @Override
            public void onRightClick(View v) {
            }
        });

        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - delta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }

        View view = recyclerView;
        if (view != null) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
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
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mDrawableProgress.setTint(0xffffffff);
        }

    }

    public void getData(final int page, final int flag, final RefreshLayout refreshLayout) {
        EasyHttp.get("index/BrandController/searchCar?brand=" + brand).params("page", page + "")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        List<Car> cars = JSON.parseArray(data, Car.class);
                        if (cars.size() == 0) {
                            refreshLayout.finishRefresh();
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
