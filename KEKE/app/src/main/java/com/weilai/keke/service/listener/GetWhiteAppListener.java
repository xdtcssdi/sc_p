package com.weilai.keke.service.listener;

import com.weilai.keke.entity.WhiteApp;

public interface GetWhiteAppListener {
    void onResponse(String whiteApp);
    void onFail(String msg);
}
