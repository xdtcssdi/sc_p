package com.example.amicool.iface;

import com.example.amicool.bean.VideoBean;

public interface AttentionListiface<T> {
    void getResultList(String mod,//模块
                       int page,//页数
                       String sessionID,
                       int userid,
                       AttentionListListener<VideoBean> listener

    );
}
