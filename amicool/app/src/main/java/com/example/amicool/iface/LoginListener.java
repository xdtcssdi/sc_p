package com.example.amicool.iface;

import com.example.amicool.bean.LoginBean;

public interface LoginListener {
    void onResponse(LoginBean loginBean);
    void onFail(String msg);
}
