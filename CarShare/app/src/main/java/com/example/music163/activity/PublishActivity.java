package com.example.music163.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.bean.Image;
import com.example.music163.bean.Success;
import com.example.music163.common.GlideImageLoaderPath;
import com.example.music163.common.GlideImageLoaderUri;
import com.example.music163.fragment.BBSFragment;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.UIProgressResponseCallBack;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.request.PostRequest;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PublishActivity extends BaseActivity {
    private static final String TAG = "PublishActivity";
    @BindView(R.id.content)
    public MaterialEditText comments;
    @BindView(R.id.title)
    public MaterialEditText title;
    @BindView(R.id.banner)
    Banner banner;
    int aid;
    @BindView(R.id.tb_main_title_bar)
    TitleBar titleBar;
    List<Uri> uris;
    @BindView(R.id.affirm)
    ButtonView buttonView;
    List<Image> images;
    private Context context;
    private boolean flag;

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_publish);
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
        uris = new ArrayList<>();
        context = this;
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

                EasyHttp.get("index/ArticleController/starForCar")
                        .params("cid", aid + "")
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


        if (intent.hasExtra("aid")) {
            flag = false;
            buttonView.setText("评论");
            titleBar.setTitle("内容");
            comments.setText(intent.getStringExtra("content"));
            title.setText(intent.getStringExtra("title"));
            comments.setEnabled(false);
            title.setEnabled(false);
            aid = intent.getIntExtra("aid", 0);

            EasyHttp.get("index/ArticleController/isStar")
                    .params("cid", aid + "")
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


            if (aid == 0) {
                ToastUtils.showShort("获取数据失败");
            } else
                EasyHttp.get("index/ArticleController/showAtlas").params("cid", aid + "")
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {
                                images = JSON.parseArray(data, Image.class);
                                //设置图片加载器
                                banner.setImageLoader(new GlideImageLoaderPath());
                                //设置图片集合
                                banner.setImages(images);
                                banner.start();
                            }

                            @Override
                            public void onError(ApiException error) {
                                Log.d(TAG, "onFailed: " + error);
                            }
                        });

        } else {
            flag = true;
            buttonView.setText("发表");

            uris = Matisse.obtainResult(intent);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoaderUri());
            //设置图片集合
            banner.setImages(uris);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
        //增加点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (flag)
                    Matisse.from((Activity) context)
                            .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                            .maxSelectable(3)
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(BBSFragment.REQUEST_CODE_CHOOSE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " ," + resultCode);
        if (requestCode == BBSFragment.REQUEST_CODE_CHOOSE) {
            if (data == null)
                return;
            uris = Matisse.obtainResult(data);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoaderUri());
            //设置图片集合
            banner.update(uris);
        }
    }

    @OnClick(R.id.affirm)
    public void affirm() {
        if (flag) {
            PostRequest params = EasyHttp.post("index/ArticleController/publishArticle")
                    .params("session", getSession())
                    .params("title", title.getText().toString())
                    .params("content", comments.getText().toString());

            final IProgressDialog mProgressDialog = new IProgressDialog() {
                @Override
                public Dialog getDialog() {
                    ProgressDialog dialog = new ProgressDialog(PublishActivity.this);
                    dialog.setMessage("发表中");
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

            for (int i = 0; i < uris.size(); ++i) {
                File file = new File(getRealFilePath(context, uris.get(i)));
                //如果有文件名字可以不用再传Type,会自动解析到是image/*
                params.params("image[]", file, file.getName(), listener);
            }

            params.execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                @Override
                public void onSuccess(String data) {
                    Success success = JSON.parseObject(data, Success.class);

                    if (success.getSuccess() == 1) {
                        ToastUtils.showShort("发布成功");
                        finish();
                    } else {
                        ToastUtils.showShort(success.getError() + "");
                    }

                }

                @Override
                public void onError(ApiException error) {
                    Log.d(TAG, "onFailed: " + error);
                }
            });
        } else {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("id", aid);
            intent.putExtra("type", 2);
            ActivityUtils.startActivity(intent);
        }
    }

}
