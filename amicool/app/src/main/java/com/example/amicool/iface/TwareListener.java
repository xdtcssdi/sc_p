package com.example.amicool.iface;

import com.example.amicool.bean.ArticleBean;
import com.example.amicool.bean.TwareBean;

import java.util.List;

public interface TwareListener {
    //成功返回登录信息
    void onResponse(List<TwareBean> beanlist);
    //失败返回错误字符串
    void onFail(String msg);
}
