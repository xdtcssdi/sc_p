package com.example.amicool.model;

import com.example.amicool.bean.TwareBean;
import com.example.amicool.iface.AttentionListListener;
import com.example.amicool.service.AttentionListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ATwareModel {
    private Retrofit retrofit;
    private String BASEURL
            ="http://amicool.neusoft.edu.cn/";
    //构造函数
    public ATwareModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public void getResultList(String mod, int page, String sessionID, String userid,final AttentionListListener<TwareBean> listener) {
        //使用Retrofit----2
        AttentionListService service
                =retrofit.create(AttentionListService.class);
        Call<List<TwareBean>> call
                =service.getTwareList(mod,page,sessionID,userid);
        //使用Retrofit----3
        call.enqueue(new Callback<List<TwareBean>>() {
            @Override
            public void onResponse(Call<List<TwareBean>> call, Response<List<TwareBean>> response) {
                if(response!=null && response.isSuccessful())
                {  listener.onResponse(response.body());
                }
                else {
                    listener.onFail("onresponse fail");
                }
            }
            @Override
            public void onFailure(Call<List<TwareBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
