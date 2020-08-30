package com.example.music163.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.bean.Success;
import com.example.music163.fragment.BBSFragment;
import com.example.music163.fragment.MyFragment;
import com.example.music163.fragment.OneFragment;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.zhihu.matisse.Matisse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.request.PostRequest;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.music163.activity.PublishActivity.getRealFilePath;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.mDrawerLayout)
    public DrawerLayout mDrawerLayout;
    @BindView(R.id.main_viewpager)
    public ViewPager main_viewpager;
    @BindView(R.id.ImageView1)
    public ImageView ImageView1;
    @BindView(R.id.ImageView2)
    public ImageView ImageView2;
    @BindView(R.id.ImageView3)
    public ImageView ImageView3;
    @BindView(R.id.search_go_btn)
    public ImageView search_go_btn;
    SearchFragment searchFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private MainAdapter mAdapter;
    private OneFragment oneFragment;
    private BBSFragment twoFragment;
    private MyFragment threeFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        context = this;
        initView();

    }

    private void initView() {
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                Intent intent = new Intent(context, SearchResultActivity.class);
                intent.putExtra("brand", keyword);
                ActivityUtils.startActivity(intent);
            }
        });

        oneFragment = new OneFragment();
        twoFragment = new BBSFragment();
        threeFragment = new MyFragment();

        fragments.add(oneFragment);
        fragments.add(twoFragment);
        fragments.add(threeFragment);

        ImageView1.setSelected(true);

        mAdapter = new MainAdapter(getSupportFragmentManager());
        main_viewpager.setAdapter(mAdapter);

        main_viewpager.addOnPageChangeListener(this);
    }

    @OnClick(R.id.search_go_btn)
    public void openSearchDialog() {
        searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ImageView1.setSelected(true);
                ImageView2.setSelected(false);
                ImageView3.setSelected(false);
                break;
            case 1:
                ImageView1.setSelected(false);
                ImageView2.setSelected(true);
                ImageView3.setSelected(false);
                break;
            case 2:
                ImageView1.setSelected(false);
                ImageView2.setSelected(false);
                ImageView3.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void one_iv(View view) {

        main_viewpager.setCurrentItem(0);
        ImageView1.setSelected(true);
        ImageView2.setSelected(false);
        ImageView3.setSelected(false);
    }

    public void two_iv(View view) {
        main_viewpager.setCurrentItem(1);
        ImageView1.setSelected(false);
        ImageView2.setSelected(true);
        ImageView3.setSelected(false);
    }

    public void three_iv(View view) {
        main_viewpager.setCurrentItem(2);
        ImageView1.setSelected(false);
        ImageView2.setSelected(false);
        ImageView3.setSelected(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " ," + resultCode);
        if (requestCode == BBSFragment.REQUEST_CODE_CHOOSE) {
            if (data == null)
                return;
            data.setClass(context, PublishActivity.class);
            ActivityUtils.startActivity(data);
        } else if (requestCode == 24) {
            PostRequest params = EasyHttp.post("index/UserController/setAvatar")
                    .params("session", getSession());

            final IProgressDialog mProgressDialog = new IProgressDialog() {
                @Override
                public Dialog getDialog() {
                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("提交中");
                    return dialog;
                }
            };
            final UIProgressResponseCallBack listener = new UIProgressResponseCallBack() {
                @Override
                public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                    if (done) {//完成
                        mProgressDialog.getDialog().dismiss();
                    }
                }
            };
            List<Uri> uris;
            try {
                uris = Matisse.obtainResult(data);
            } catch (Exception e) {
                return;
            }
            File file = new File(getRealFilePath(context, uris.get(0)));
            //如果有文件名字可以不用再传Type,会自动解析到是image/*
            params.params("image", file, file.getName(), listener);

            params.execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                @Override
                public void onSuccess(String data) {
                    Success success = JSON.parseObject(data, Success.class);
                    if (success.getSuccess() == 1) {
                        ToastUtils.showShort("修改成功");
                    } else {
                        ToastUtils.showShort(success.getError() + "");
                    }

                }

                @Override
                public void onError(ApiException error) {
                    Log.d(TAG, "onFailed: " + error);
                }
            });

        }
    }

    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
