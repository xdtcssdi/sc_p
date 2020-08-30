package com.example.amicool.iface;

import java.util.List;

public interface AttentionListListener<T> {
    void onResponse(List<T> beanlist);
    void onFail(String msg);
}


