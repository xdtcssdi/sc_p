package com.example.amicool.iface;

import com.example.amicool.bean.ArticleBean;
import com.example.amicool.bean.TcaseBean;

import java.util.List;

public interface TcaseListener {
    //成功返回登录信息
    void onResponse(List<TcaseBean> beanlist);
    //失败返回错误字符串
    void onFail(String msg);
}
