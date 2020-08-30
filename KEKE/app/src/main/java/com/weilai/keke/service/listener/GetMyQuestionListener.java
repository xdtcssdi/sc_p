package com.weilai.keke.service.listener;

import com.weilai.keke.entity.QuestionEntity;

public interface GetMyQuestionListener {
    void onResponse(QuestionEntity questionEntity);
    void onFail(String msg);
}
