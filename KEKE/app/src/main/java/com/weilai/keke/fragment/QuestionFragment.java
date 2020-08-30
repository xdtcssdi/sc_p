package com.weilai.keke.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goyourfly.multiple.adapter.MultipleAdapter;
import com.goyourfly.multiple.adapter.MultipleSelect;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.weilai.keke.R;
import com.weilai.keke.adapter.QuestionAdapter;
import com.weilai.keke.entity.QuestionEntity;
import com.weilai.keke.model.GetMyQuestionModel;
import com.weilai.keke.service.listener.GetMyQuestionListener;
import com.weilai.keke.util.Common;
import com.weilai.keke.util.PreferencesKit;

import java.util.ArrayList;
import java.util.TooManyListenersException;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private Context context;
    private RecyclerView questions;
    private QuestionEntity questionEntity;
    private QuestionAdapter questionAdapter;
    private static final String TAG = "QuestionFragment";
    private String sessionid;
    private BroadcastReceiver broadcastReceiver;
    private RefreshLayout refresh_layout;
    private GetMyQuestionListener listener = new GetMyQuestionListener() {
        @Override
        public void onResponse(QuestionEntity request) {
            if(request==null||request.getData()==null)
            {
                onFail("获取问题数据错误");
                return;
            }
            Log.d(TAG, "onResponse: "+request);
            questionEntity.getData().clear();
            questionEntity.getData().addAll(request.getData());
            questionAdapter.notifyDataSetChanged();
            md.dismiss();
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(context)
                    .title("警告")
                    .content(msg).autoDismiss(false)
                    .negativeText("返回").onNegative((dialog, which) -> {
                dialog.dismiss();
            }).show();
            md.dismiss();
        }
    };
    private MaterialDialog md;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_question_layout, container, false);
        refresh_layout = mView.findViewById(R.id.refresh_layout);
        context = getContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Common.UpdateQuestions);
        broadcastReceiver = new UpdateQuestions();
        context.registerReceiver(broadcastReceiver, filter);
        questionEntity = new QuestionEntity();
        questionEntity.setData(new ArrayList<>());
        //从服务器加载当前用户的问题数据

        sessionid = PreferencesKit.getString(context,"loginState","sessionid","nosessionid");

        initializationModule();
        md = new MaterialDialog.Builder(context)
                .title("提示")
                .content("获取数据中")
                .progress(true, 0).show();

        GetMyQuestionModel questionModel = new GetMyQuestionModel();

        refresh_layout.autoRefresh();//自动刷新
        refresh_layout.setEnableRefresh(true);//是否启用下拉刷新功能
        refresh_layout.setEnableLoadMore(false);//是否启用上拉加载功能
        refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                questionModel.get_my_question("sessionid="+sessionid, listener);
                refresh_layout.closeHeaderOrFooter();
            }
        });

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(broadcastReceiver);
    }

    private void initializationModule() {
        //初始化部件
        questions = mView.findViewById(R.id.questionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        questions.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(questionEntity.getData());
        questions.setAdapter(questionAdapter);
        questions.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.send_QuestionRequest:
//                setQuestionRequest();
//                break;
//
//        }
    }

    //老师设置问题
//    private void setQuestionRequest() {
//        final int[] count = {0};
//        List<MaterialEditText> optionContentEdit = new ArrayList<>();
//
//        List<String> optionContent = new ArrayList<>();
//
//        new MaterialDialog.Builder(mView.getContext())
//                .title("新建问题")
//                .customView(R.layout.set_question_layout, true)
//                .positiveText("确认提交").autoDismiss(false).cancelable(false)
//                .onPositive((dialog, which) -> {
//                            //获取问题内容
//
//                            MaterialEditText meDetail = dialog.getView().findViewById(R.id.questionDetail);
//                            String questionDetail = meDetail.getText().toString();
//                            //问题描述
//                            MaterialEditText meLimitTime = dialog.getView().findViewById(R.id.timeLimit);
//
//                            if ("".equals(questionDetail)) {
//                                new MaterialDialog.Builder(dialog.getContext())
//                                        .title("警告")
//                                        .content("问题描述不可为空").autoDismiss(false)
//                                        .negativeText("返回").onNegative((dialog12, which12) -> {
//                                    dialog12.dismiss();
//                                    return;
//                                })
//                                        .show();
//                                return;
//                            }
//                            //限时
//                            int[] timeLimit = {0};
//                            try {
//                                timeLimit[0] = Integer.parseInt(meLimitTime.getText().toString());
//                            } catch (NumberFormatException e) {
//                                timeLimit[0] = -1;
//                            }
//                            //弹出选择问题类型窗口
//                            new MaterialDialog.Builder(dialog.getContext())
//                                    .title("选择题目类型")
//                                    .items(R.array.questionType)
//                                    .itemsCallback((dialog1, view, which1, text) -> {
//
//                                        //问题选项内容下标0对应
//                                        for (MaterialEditText content : optionContentEdit) {
//                                            optionContent.add(content.getText().toString());
//                                            Log.d(TAG, content.getText().toString());
//                                        }
//                                        int questionId;
//                                        if (questionEntity.getData().size() == 0) {
//                                            questionId = 1;
//                                        } else {
//                                            questionId = questionRequests.get(questionRequests.size() - 1).getQuestionID() + 1;
//
//                                        }
//                                        //保存QuestionRequest到本地
//                                        QuestionRequest questionRequest =
//                                                HelpUtil.createQuestion(questionId, questionDetail, timeLimit[0], optionContent, which1);
//
//                                        questionRequests = HelpUtil.mergeList(questionRequests, questionRequest);
//
//                                        LoadQuestion.saveQuestion(dialog1.getContext()
//                                                , questionRequests);
//
//                                        adapter.notifyItemChanged(questionRequests.size() - 1);
//                                        dialog.dismiss();
//                                    })
//                                    .show();
//                        }
//                )
//                .neutralText("添加选项").onNeutral((dialog, which) -> {
//
//            //动态添加选项
//            LinearLayout linearLayout = dialog.getView().findViewById(R.id.setquestionlayout);
//
//            MaterialEditText me = new MaterialEditText(dialog.getContext());
//            optionContentEdit.add(me);
//            me.setHint("请输入选项");
//            me.setGravity(Gravity.CENTER_HORIZONTAL);
//            me.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            TextView textView = new TextView(dialog.getContext());
//            textView.setPadding(0, 0, 15, 0);
//            textView.setText("选项" + (char) (65 + count[0]++));
//
//            LinearLayout innerLayout = new LinearLayout(linearLayout.getContext());
//            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
//            innerLayout.addView(textView);
//            innerLayout.addView(me);
//
//            linearLayout.addView(innerLayout);
//        }).negativeText("取消").onNegative(((dialog, which) -> dialog.dismiss()))
//                .show();
//
//    }
//

    private class UpdateQuestions extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "UpdateQuestions: ");
            //从网络获取问题
//            questionRequests = HelpUtil.mergeList(questionRequests,
//                    LoadQuestion.loadQuestions(BaseApplication.getContext()));
//            adapter.notifyDataSetChanged();
            md = new MaterialDialog.Builder(context)
                    .title("提示")
                    .content("获取数据中")
                    .progress(true, 0).show();
            GetMyQuestionModel questionModel = new GetMyQuestionModel();
            questionModel.get_my_question("sessionid="+sessionid, listener);

            Toast.makeText(context,"数据刷新",Toast.LENGTH_SHORT).show();
        }
    }
}
