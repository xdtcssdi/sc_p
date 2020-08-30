package com.weilai.keke.service.iface;

import com.weilai.keke.entity.QuestionEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetMyQuestioniface {
    @GET("my_question")
    Call<QuestionEntity> my_question(@Header("Cookie") String cookie);
}
