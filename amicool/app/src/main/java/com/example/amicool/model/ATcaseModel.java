package com.example.amicool.model;

import com.example.amicool.bean.TcaseBean;
import com.example.amicool.iface.AttentionListListener;
import com.example.amicool.service.AttentionListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ATcaseModel {
    private Retrofit retrofit;
    private String BASEURL
            ="http://amicool.neusoft.edu.cn/";
    //构造函数
    public ATcaseModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public void getResultList(String mod, int page, String sessionID, String userid,final AttentionListListener<TcaseBean> listener) {
        //使用Retrofit----2
        AttentionListService service
                =retrofit.create(AttentionListService.class);
        Call<List<TcaseBean>> call
                =service.getTcaseList(mod,page,sessionID,userid);
        //使用Retrofit----3
        call.enqueue(new Callback<List<TcaseBean>>() {
            @Override
            public void onResponse(Call<List<TcaseBean>> call, Response<List<TcaseBean>> response) {
                if(response!=null && response.isSuccessful())
                {  listener.onResponse(response.body());
                }
                else {
                    listener.onFail("onresponse fail");
                }
            }
            @Override
            public void onFailure(Call<List<TcaseBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
