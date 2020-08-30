package com.weilai.keke.service.listener;

import com.weilai.keke.entity.SuccessEntity;

public interface RegisterListener {
    void onResponse(SuccessEntity registerBean);
    void onFail(String msg);
}
