package com.weilai.keke.service.listener;

import com.weilai.keke.entity.TeachingClassEntity;

public interface GetMyClassListener{
    void onResponse(TeachingClassEntity teachingClassEntity);
    void onFail(String msg);
}