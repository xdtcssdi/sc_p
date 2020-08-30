package com.example.amicool.iface;

import com.example.amicool.bean.ProjectBean;
import com.example.amicool.bean.TcaseBean;

import java.util.List;

public interface ProjectListener {
    //成功返回登录信息
    void onResponse(List<ProjectBean> beanlist);
    //失败返回错误字符串
    void onFail(String msg);
}
