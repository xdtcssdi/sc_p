package com.example.music163.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
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
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class CarDetailActivity extends BaseActivity {

    private static final String TAG = "CarDetailActivity";
    @BindView(R.id.banner)
    Banner banner;
    int bid;
    @BindView(R.id.tb_main_title_bar)
    TitleBar titleBar;
    @BindView(R.id.affirm)
    ButtonView buttonView;
    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandableTextView;
    @BindView(R.id.videopaly)
    JzvdStd jzvdStd;


    List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_car_detail);
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

                EasyHttp.get("index/CarController/starForCar")
                        .params("cid", bid + "")
                        .params("session", getSession())
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Success success = JSON.parseObject(data, Success.class);
                                if (success.getSuccess() == 1) {
                                    if ("未收藏".equals(titleBar.getRightView().getText().toString())) {
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

            EasyHttp.get("index/CarController/isStar")
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
            EasyHttp.get("index/CarController/showCarDetail").params("bid", bid + "")
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Car2 car2 = JSON.parseObject(data, Car2.class);
                            mExpandableTextView.setText(car2.getDetail());
                            jzvdStd.setUp(car2.getVideo(), "" , Jzvd.SCREEN_WINDOW_NORMAL);
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
        intent.putExtra("type", 1);
        ActivityUtils.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}

class Car2 {


    /**
     * cid : 1
     * name : Polo
     * brand : 1
     * type : 3
     * detail : 手动 自动 手自一体 双离合
     * comment : null
     * mainimg : https://qncar2.autoimg.cn/cardfs/product/g2/M03/31/11/autohomecar__ChcCRFsD-EqAL9QXAAmfEH8T-X0511.jpg
     * video : https://n5-pl-agv.autohome.com.cn/video-33/0A33363922E51BDE/2019-03-05/38B8E11BE27EBA37-300.mp4
     */

    private int cid;
    private String name;
    private int brand;
    private int type;
    private String detail;
    private Object comment;
    private String mainimg;
    private String video;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public String getMainimg() {
        return mainimg;
    }

    public void setMainimg(String mainimg) {
        this.mainimg = mainimg;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}