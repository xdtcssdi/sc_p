package com.example.music163.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.example.music163.R;
import com.example.music163.adapter.CommentAdapter;
import com.example.music163.bean.Comment;
import com.example.music163.bean.Success;
import com.example.music163.util.DynamicTimeFormat;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class CommentActivity extends BaseActivity {
    private static final String TAG = "CommentActivity";
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tb_main_title_bar)
    TitleBar titleBar;
    private Context context;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    private SmartRecyclerAdapter<Comment> mAdapter;
    private int o_page = 1;
    private int aid;
    private int type;
    private String s1, s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        super.onCreate(savedInstanceState);
        context = this;
        Intent intent = getIntent();
        aid = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);

        Log.d(TAG, "onCreate: id =" + aid + "  ,type =" + type);
        switch (type) {
            case 1:
                s1 = "index/CarController/showComments";
                s2 = "index/CarController/commentForCar";
                break;
            case 2:
                s1 = "index/ArticleController/showComments";
                s2 = "index/ArticleController/commentForCar";
                break;
            case 3:
                s1 = "index/BrandController/showComments";
                s2 = "index/BrandController/commentForCar";
                break;
            default:
                ToastUtils.showShort("数据错误，请返回重试");
                break;
        }

        initView();
    }

    private void initView() {
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


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialDialog.Builder(context)
                        .title("评论")
                        .inputType(
                                InputType.TYPE_CLASS_TEXT
                                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .input("评论内容",
                                "",
                                false,
                                (new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                    }
                                }))
                        .inputRange(0, 100)
                        .positiveText("发布")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final String content = dialog.getInputEditText().getText().toString();
                                final IProgressDialog mProgressDialog = new IProgressDialog() {
                                    @Override
                                    public Dialog getDialog() {
                                        ProgressDialog dialog = new ProgressDialog(context);
                                        dialog.setMessage("发表中");
                                        return dialog;
                                    }
                                };

                                EasyHttp.post(s2)
                                        .params("session", getSession())
                                        .params("cid", aid + "")
                                        .params("content", content)
                                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                                            @Override
                                            public void onSuccess(String data) {
                                                Success success = JSON.parseObject(data, Success.class);
                                                if (success.getSuccess() == 1) {
                                                    ToastUtils.showShort("发布成功");
                                                    Comment comment = new Comment();
                                                    comment.setComment(content);
                                                    comment.setUsername(getUserName());
                                                    mAdapter.load(comment);
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
                        })
                        .cancelable(false)
                        .show();
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
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter = new CommentAdapter(R.layout.article_item));
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            o_page = 1;
                            mAdapter.getListData().clear();
                            mAdapter.notifyListDataSetChanged();
                            getData(o_page, 0, refreshLayout, aid);
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
                            getData(++o_page, 1, refreshLayout, aid);
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

    public void getData(int page, final int flag, final RefreshLayout refreshLayout, int cid) {
        EasyHttp.get(s1).params("page", page + "").params("cid", cid + "")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (flag == 0) {
                            refreshLayout.finishRefresh();
                            refreshLayout.resetNoMoreData();
                        } else {

                            refreshLayout.finishLoadMore();
                        }

                        List<Comment> comments = JSON.parseArray(data, Comment.class);
                        if (comments.size() == 0) {
                            ToastUtils.showShort("已经到底部~\\(≧▽≦)\\~啦啦啦");
                            o_page = 1;
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                            return;
                        }
                        //ToastUtils.showShort("加载下一页");
                        mAdapter.loadMore(comments);

                    }

                    @Override
                    public void onError(ApiException error) {
                        Log.d(TAG, "onFailed: " + error);
                    }
                });
    }
}
