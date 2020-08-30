package com.example.amicool.iface;

import com.example.amicool.bean.CollectBean;

import java.util.List;

public interface CollectListListener<T> {
    void onResponse(List<CollectBean<T>> beanlist);
    void onFail(String msg);
}


