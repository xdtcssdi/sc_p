package com.weilai.keke.service.listener;

import com.weilai.keke.entity.LoginState;

public interface LoginListener {
    void onResponse(LoginState loginState);
    void onFail(String msg);
}
