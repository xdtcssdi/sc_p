package com.example.amicool.iface;

import com.example.amicool.bean.ArticleBean;

import java.util.List;

public interface ArticleListener {
    //成功返回登录信息
    void onResponse(List<ArticleBean> beanlist);
    //失败返回错误字符串
    void onFail(String msg);
}
