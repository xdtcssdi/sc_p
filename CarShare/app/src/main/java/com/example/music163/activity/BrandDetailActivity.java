package com.example.music163.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.bean.Brand;
import com.example.music163.bean.Image;
import com.example.music163.bean.Success;
import com.example.music163.common.GlideImageLoaderPath;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.textview.ExpandableTextView;
import com.youth.banner.Banner;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BrandDetailActivity extends BaseActivity {
    private static final String TAG = "PublishActivity";
    @BindView(R.id.banner)
    Banner banner;
    int bid;
    @BindView(R.id.tb_main_title_bar)
    TitleBar titleBar;
    @BindView(R.id.affirm)
    ButtonView buttonView;
    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandableTextView;
    List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bar_detail);
        super.onCreate(savedInstanceState);


        images = new ArrayList<>();
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

                EasyHttp.get("index/BrandController/starForCar")
                        .params("cid", bid + "")
                        .params("session", getSession())
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                if (success.getSuccess() == 1) {
                                    if (titleBar.getRightView().getText().toString().equals("未收藏")) {
                                        titleBar.setRightTitle("已收藏");
                                    } else {
                                        titleBar.setRightTitle("未收藏");
                                    }
                                } else {
                                    ToastUtils.showShort(success.getError());
                                }
                            }

                            @Override
                            public void onError(ApiException error) {
                                ToastUtils.showShort("收藏失败");
                            }
                        });
            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra("bid")) {

            bid = intent.getIntExtra("bid", 0);

            EasyHttp.get("index/BrandController/isStar")
                    .params("cid", bid + "")
                    .params("session", getSession())
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Success success = JSON.parseObject(data, Success.class);
                            if (success.getSuccess() == 1)
                                titleBar.setRightTitle("已收藏");
                            else {
                                titleBar.setRightTitle("未收藏");
                            }
                        }

                        @Override
                        public void onError(ApiException error) {
                            Log.d(TAG, "onFailed: " + error);
                        }
                    });
            Log.d(TAG, "onCreate: bid: " + bid);
            EasyHttp.get("index/BrandController/showBrandDetail").params("bid", bid + "")
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Brand brand = JSON.parseObject(data, Brand.class);
                            mExpandableTextView.setText(brand.getDetail());
                        }

                        @Override
                        public void onError(ApiException error) {
                            Log.d(TAG, "onFailed: " + error);
                        }
                    });
            buttonView.setText("评论");
            titleBar.setTitle("详情");
            if (bid == 0) {
                ToastUtils.showShort("获取数据失败");
            } else
                EasyHttp.get("index/CarController/showAtlas").params("cid", bid + "")
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                images = JSON.parseArray(data, Image.class);
                                banner.setImageLoader(new GlideImageLoaderPath());
                                banner.setImages(images);
                                banner.start();
                            }

                            @Override
                            public void onError(ApiException error) {
                                Log.d(TAG, "onFailed: " + error);
                            }
                        });
        }
    }

    @OnClick(R.id.affirm)
    public void affirm() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("id", bid);
        intent.putExtra("type", 3);
        ActivityUtils.startActivity(intent);
    }

}
