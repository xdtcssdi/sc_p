package com.example.amicool.iface;


public interface RegisterListener {
    void onResponse(String registerBean);
    void onFail(String msg);
}
