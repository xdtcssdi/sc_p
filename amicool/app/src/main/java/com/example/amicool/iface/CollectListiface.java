package com.example.amicool.iface;

public interface CollectListiface<T> {
    void getResultList(String mod,//模块
                       int page,//页数
                       String sessionID,
                       CollectListListener<T> listener
    );
}
