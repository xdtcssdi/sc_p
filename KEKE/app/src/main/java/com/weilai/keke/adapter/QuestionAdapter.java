package com.weilai.keke.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.weilai.keke.R;
import com.weilai.keke.entity.QuestionEntity;
import com.weilai.keke.model.VerificationAnswerModel;
import com.weilai.keke.service.listener.VerificationAnswerListener;
import com.weilai.keke.util.SessionIdUtil;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<QuestionEntity.DataBean> questions;
    private View view;
    public QuestionAdapter(List<QuestionEntity.DataBean> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_detail_layout, parent, false);
        this.view =view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionEntity.DataBean questionRequest = questions.get(position);
        holder.questionDetail.setText(questionRequest.getQuestionDescribe());
        holder.timeLimit.setText("限时："+questionRequest.getLimited()+"秒");

        List<String> options=questionRequest.getQuestionOption();

        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<options.size();++i){
            stringBuffer.append("选项" + (char) (65 + i)+": "+options.get(i)+"\n");
        }
        holder.options.setText(stringBuffer.toString());

        holder.itemView.setOnClickListener(v -> {
//            if ("teacher".equals(Common.user.isIs_staff())) {
//                showSetQuestionDialog(holder,v.getContext(), questions.get(position));
//            } else {
//                showQuestionDialog(v.getContext(), questions.get(position));
//            }
            showQuestionDialog(v.getContext(), questionRequest);

        });
        String detail = "";

        switch (questionRequest.getDetail()){
            case 0:detail = "错误"; break;
            case 1:detail = "正确"; break;
            case 2:detail = "还没有回答"; break;
        }

        holder.reply.setText(detail);

        switch (questionRequest.getType()){
            case 0:
                holder.riv.setImageResource(R.drawable.single_choose);
                break;
            case 1:
                holder.riv.setImageResource(R.drawable.judgment);
                break;
            case 2:
                holder.riv.setImageResource(R.drawable.muti_choose);
                break;
            default:
                break;
        }
    }



    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionDetail;
        TextView options,reply;
        TextView timeLimit;
        RoundedImageView riv;
        public ViewHolder(View view) {
            super(view);
            questionDetail = view.findViewById(R.id.questionItemTitle);
            options = view.findViewById(R.id.options);
            timeLimit = view.findViewById(R.id.timeLimit);
            riv = view.findViewById(R.id.questionType);
            reply = view.findViewById(R.id.reply);
        }
    }

    private static final String TAG = "QuestionAdapter";

//    public void showSetQuestionDialog(ViewHolder holder,Context context, QuestionEntity.DataBean questionRequest) {
//
//        List<MaterialEditText> optionContentEdit = new ArrayList<>();
//        List<String> optionContent = questionRequest.getQuestionOption();
//        int[] count = {optionContent.size()};
//        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
//                .title("编辑问题")
//                .customView(R.layout.set_question_layout, true)
//                .positiveText("确认提交").autoDismiss(false).cancelable(false)
//                .onPositive((dialog, which) -> {
//                            if ("".equals(((MaterialEditText) (dialog.getCustomView().findViewById(R.id.questionDetail))
//                            ).getText().toString())) {
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
//
//                            //限时
//
//                            int[] timeLimit = {0};
//                            try {
//                                timeLimit[0] = Integer.parseInt(((MaterialEditText) (dialog.getCustomView().findViewById(R.id.timeLimit))).getText().toString());
//                            } catch (NumberFormatException e) {
//                                timeLimit[0] = -1;
//                            }
//
//                            new MaterialDialog.Builder(dialog.getContext())
//                                    .title("选择题目类型")
//                                    .items(R.array.questionType)
//                                    .itemsCallback((dialog1, view, which1, text) -> {
//                                        //问题选项内容下标0对应
//
//                                        for (int i = 0; i < optionContentEdit.size(); ++i) {
//                                            if (i >= optionContent.size()) {
//                                                optionContent.add(optionContentEdit.get(i).getText().toString());
//                                            } else {
//                                                optionContent.set(i, optionContentEdit.get(i).getText().toString());
//                                            }
//                                        }
//
//                                        questionRequest.setLimitedTime(timeLimit[0]);
//                                        //问题描述
//                                        String title=((MaterialEditText) (dialog.getCustomView().findViewById(R.id.questionDetail))).getText().toString();
//                                        questionRequest.setQuestionDescribe(title);
//                                        questionRequest.setQuestionOption(optionContent);
//                                        questionRequest.setQuestionType(which1);
//
//
//                                        holder.questionDetail.setText(title);
//                                        holder.timeLimit.setText("限时："+timeLimit[0]+"秒");
//
//                                        StringBuffer stringBuffer = new StringBuffer();
//                                        for(int i=0;i<optionContent.size();++i){
//                                            stringBuffer.append("选项" + (char) (65 + i)+": "+optionContent.get(i)+"\n");
//                                        }
//
//                                        holder.options.setText(stringBuffer.toString());
//
//                                        switch (which1){
//                                            case 0:
//                                                holder.riv.setImageResource(R.drawable.single_choose);
//                                                break;
//                                            case 1:
//                                                holder.riv.setImageResource(R.drawable.judgment);
//                                                break;
//                                            case 2:
//                                                holder.riv.setImageResource(R.drawable.muti_choose);
//                                                break;
//                                            default:
//                                                break;
//                                        }
//                                        //访问网络
////                                        questionRequests = HelpUtil.mergeList(questionRequests, questionRequest);
////
////                                        LoadQuestion.saveQuestion(dialog1.getContext()
////                                                , questionRequests);
//
//                                        this.notifyItemChanged(questions.size() - 1);
//
//                                        dialog.dismiss();
//                                    })
//                                    .show();
//                        }
//                )
//                .neutralText("添加选项").onNeutral((dialog, which) -> {
//
//                    //动态添加选项
//                    LinearLayout linearLayout = dialog.getView().findViewById(R.id.setquestionlayout);
//
//                    MaterialEditText me = new MaterialEditText(dialog.getContext());
//                    optionContentEdit.add(me);
//                    me.setGravity(Gravity.CENTER_HORIZONTAL);
//                    me.setHint("请输入选项");
//                    me.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    TextView textView = new TextView(dialog.getContext());
//                    textView.setPadding(0, 0, 15, 0);
//                    textView.setText("选项" + (char) (65 + count[0]++));
//
//                    LinearLayout innerLayout = new LinearLayout(linearLayout.getContext());
//                    innerLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    innerLayout.addView(textView);
//                    innerLayout.addView(me);
//
//                    linearLayout.addView(innerLayout);
//                }).negativeText("取消").onNegative(((dialog, which) -> dialog.dismiss()))
//                .show();
//
//
//        LinearLayout linearLayout = materialDialog.getView().findViewById(R.id.setquestionlayout);
//
//        for (int i = 0; i < optionContent.size(); ++i) {
//            LinearLayout innerLayout = new LinearLayout(materialDialog.getCustomView().getContext());
//
//            MaterialEditText me = new MaterialEditText(materialDialog.getCustomView().getContext());
//
//            optionContentEdit.add(me);
//            me.setText(optionContent.get(i));
//            me.setGravity(Gravity.CENTER_HORIZONTAL);
//            me.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            TextView textView = new TextView(materialDialog.getCustomView().getContext());
//            textView.setPadding(0, 0, 15, 0);
//            textView.setText("选项" + (char) (65 + i));
//
//            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
//            innerLayout.addView(textView);
//            innerLayout.addView(me);
//            linearLayout.addView(innerLayout);
//        }
//        MaterialEditText t = materialDialog.getCustomView().findViewById(R.id.questionDetail);
//        t.setText(questionRequest.getQuestionDescribe());
//
//        MaterialEditText tt = materialDialog.getCustomView().findViewById(R.id.timeLimit);
//        if (questionRequest.getLimited() == -1) {
//            tt.setText("");
//        } else {
//            tt.setText(questionRequest.getLimited() + "");
//        }
//    }
    private MaterialDialog materialDialog;
    public void showQuestionDialog(Context context, QuestionEntity.DataBean question) {

        List<String> optionContent = question.getQuestionOption();

        materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.show_question_layout, true)
                .cancelable(true)
                .build();

        TextView questionDescribe = materialDialog.getCustomView().findViewById(R.id.questionDetail);
        questionDescribe.setGravity(Gravity.CENTER_HORIZONTAL);

        questionDescribe.setText(question.getQuestionDescribe());
        questionDescribe.setTextSize(20);

        TextView timeLimit = materialDialog.getCustomView().findViewById(R.id.timeLimit);
        if (question.getLimited() == -1) {
            timeLimit.setText("无限");
        } else {
            timeLimit.setText(question.getLimited() + "");
        }
        timeLimit.setEnabled(false);

        materialDialog.show();

        LinearLayout linearLayout = materialDialog.getView().findViewById(R.id.show_question_layout);

        List<LinearLayout> linearLayouts = new ArrayList<>();
        int is_select[] = new int[optionContent.size()];
        for (int i = 0; i < optionContent.size(); ++i) {
            is_select[i]=0;
            LinearLayout innerLayout = new LinearLayout(materialDialog.getCustomView().getContext());
            innerLayout.setPadding(10,20,10,20);
            linearLayouts.add(innerLayout);

            TextView option = new TextView(materialDialog.getCustomView().getContext());
            option.setTextSize(20);
            option.setText(optionContent.get(i));
            option.setGravity(Gravity.CENTER_HORIZONTAL);
            option.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView a2z = new TextView(materialDialog.getCustomView().getContext());
            a2z.setTextSize(20);
            a2z.setGravity(Gravity.LEFT);
            a2z.setPadding(0, 0, 15, 0);
            a2z.setText("选项" + (char) (65 + i));

            innerLayout.setOrientation(LinearLayout.HORIZONTAL);

            innerLayout.addView(a2z);
            innerLayout.addView(option);

            int finalI = i;
            innerLayout.setOnClickListener(v -> {

                if(question.getType()==0||question.getType()==1){
                    for(int idx=0;idx<linearLayouts.size();++idx) {
                        linearLayouts.get(idx).setBackgroundColor(Color.parseColor("#ffffff"));
                        is_select[idx]=0;
                    }
                }
                if(is_select[finalI]==0){
                    is_select[finalI]=1;
                    innerLayout.setBackgroundColor(Color.parseColor("#FF57D5E6"));
                }else{
                    is_select[finalI]=0;
                    innerLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                }

            });
            linearLayout.addView(innerLayout);
        }
        if(question.getDetail()==2) {
            Button ok = new Button(materialDialog.getCustomView().getContext());
            ok.setTextSize(20);
            ok.setGravity(Gravity.CENTER_HORIZONTAL);
            ok.setText("确定");
            //http://127.0.0.1:8000/keke/api/is_correct/?pk=2&option=[1,1,0,0]&StuAndQuePk=33
            ok.setOnClickListener(v -> {
                StringBuffer req_str = new StringBuffer("[");
                for (int i = 0; i < is_select.length; ++i) {
                    req_str.append(is_select[i]);
                    if (i != is_select.length - 1)
                        req_str.append(",");
                }
                req_str.append("]");
                Log.d(TAG, "showQuestionDialog: " + req_str.toString());

                VerificationAnswerModel model = new VerificationAnswerModel();
                model.is_correct("sessionid=" + SessionIdUtil.getSession(),
                        question.getQuestion_pk(), req_str.toString(),
                        question.getStuAndQuePk(), listener);
            });
            linearLayout.addView(ok);
        }

    }
    private VerificationAnswerListener listener = new VerificationAnswerListener() {
        @Override
        public void onResponse(String resp) {
            if (resp==null){
                onFail("访问错误");
                return;
            }
            if(resp.contains("1")){
                Toast.makeText(view.getContext(),"回答正确",Toast.LENGTH_SHORT).show();
            }else if(resp.contains("0")){
                Toast.makeText(view.getContext(),"回答错误",Toast.LENGTH_SHORT).show();
            }
            materialDialog.dismiss();
        }

        @Override
        public void onFail(String msg) {
            new MaterialDialog.Builder(view.getContext())
                .title("警告")
                .content(msg).autoDismiss(false)
                .negativeText("返回").onNegative((dialog, which) -> {
                    dialog.dismiss();
                }).show();
        }
    };
}
