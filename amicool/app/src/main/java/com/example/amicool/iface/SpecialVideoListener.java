package com.example.amicool.iface;

import com.example.amicool.bean.VideoBean;

import java.util.List;

public interface SpecialVideoListener {
    //成功返回登录信息
    void onResponse(List<VideoBean> beanlist);
    //失败返回错误字符串
    void onFail(String msg);
}
