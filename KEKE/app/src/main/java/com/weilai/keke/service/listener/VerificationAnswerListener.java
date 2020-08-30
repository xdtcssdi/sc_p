package com.weilai.keke.service.listener;

import com.weilai.keke.entity.SuccessEntity;

public interface VerificationAnswerListener {
    void onResponse(String resp);
    void onFail(String msg);
}
