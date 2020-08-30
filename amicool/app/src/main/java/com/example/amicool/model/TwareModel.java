package com.example.amicool.model;

import com.example.amicool.bean.TwareBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.TwareListener;
import com.example.amicool.iface.Twareiface;
import com.example.amicool.service.TwareService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwareModel implements Twareiface {
    private Retrofit retrofit;

    //构造函数
    public TwareModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getResultList(String mod, int page, String sessionID, final TwareListener listener) {
        //使用Retrofit----2
        TwareService service
                =retrofit.create(TwareService.class);
        Call<List<TwareBean>> call
                =service.getTwareList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<TwareBean>>() {
            @Override
            public void onResponse(Call<List<TwareBean>> call, Response<List<TwareBean>> response) {
                if(response!=null && response.isSuccessful())
                {
                    listener.onResponse(response.body());
                }
                else {
                    listener.onFail("on response fail");
                }
            }
            @Override
            public void onFailure(Call<List<TwareBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
