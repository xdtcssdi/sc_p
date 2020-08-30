package com.weilai.keke.service.listener;

import com.weilai.keke.entity.UserInfo;

public interface GetInfoListener {
    void onResponse(UserInfo dataBean);
    void onFail(String msg);
}
