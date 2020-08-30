package com.weilai.keke.service.listener;

import com.weilai.keke.entity.SuccessEntity;

public interface SuccessListener {
    void onResponse(SuccessEntity success);
    void onFail(String msg);
}
