package com.example.amicool.model;

import com.example.amicool.bean.FocusResultBean;
import com.example.amicool.bean.UserBean;
import com.example.amicool.iface.AttentionListListener;
import com.example.amicool.service.AttentionListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttentionListModel {
    private Retrofit retrofit;
    private String BASEURL
            = "http://amicool.neusoft.edu.cn/";

    //构造函数
    public AttentionListModel() {   //使用Retrofit----1
        retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getResultList(String mod, int page, String sessionID, final AttentionListListener<FocusResultBean<UserBean>> listener) {
        //使用Retrofit----2
        AttentionListService service
                = retrofit.create(AttentionListService.class);
        Call<List<FocusResultBean<UserBean>>> call
                = service.getFocusUserList(mod, page, sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<FocusResultBean<UserBean>>>() {
            @Override
            public void onResponse(Call<List<FocusResultBean<UserBean>>> call, Response<List<FocusResultBean<UserBean>>> response) {
                if (response != null && response.isSuccessful()) {
                    listener.onResponse(response.body());
                } else {
                    listener.onFail("onresponse fail");
                }
            }

            @Override
            public void onFailure(Call<List<FocusResultBean<UserBean>>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
