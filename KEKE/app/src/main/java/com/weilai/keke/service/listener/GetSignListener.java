package com.weilai.keke.service.listener;

import com.weilai.keke.entity.LoginState;
import com.weilai.keke.entity.SignEntity;

public interface GetSignListener {
    void onResponse(SignEntity signEntity);
    void onFail(String msg);
}
