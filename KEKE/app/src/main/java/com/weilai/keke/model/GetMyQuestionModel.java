package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.QuestionEntity;
import com.weilai.keke.service.iface.GetMyQuestioniface;
import com.weilai.keke.service.listener.GetMyQuestionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMyQuestionModel extends BaseModel {

    private static final String TAG = "GetMyQuestionModel";
    public void get_my_question(String cookie, final GetMyQuestionListener getMyQuestionListener) {
        Log.d(TAG, "get_my_question: " +cookie);
        GetMyQuestioniface getMyQuestioniface= retrofit.create(GetMyQuestioniface.class);
        Call<QuestionEntity> call = getMyQuestioniface.my_question(cookie);
        call.enqueue(new Callback<QuestionEntity>() {
            @Override
            public void onResponse(Call<QuestionEntity> call, Response<QuestionEntity> response) {
                if (response != null) {
                    getMyQuestionListener.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<QuestionEntity> call, Throwable t) {
                getMyQuestionListener.onFail(t.toString());
            }
        });
    }
}
