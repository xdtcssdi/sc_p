package com.example.amicool.model;

import com.example.amicool.bean.TcaseBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.TcaseListener;
import com.example.amicool.iface.Tcaseiface;
import com.example.amicool.service.TcaseService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TcaseModel implements Tcaseiface {
    private Retrofit retrofit;

    //构造函数
    public TcaseModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getResultList(String mod, int page, String sessionID, final TcaseListener listener) {
        //使用Retrofit----2
        TcaseService service
                =retrofit.create(TcaseService.class);
        Call<List<TcaseBean>> call
                =service.getTcaseList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<TcaseBean>>() {
            @Override
            public void onResponse(Call<List<TcaseBean>> call, Response<List<TcaseBean>> response) {
                if(response.isSuccessful() && response!=null)
                {
                    listener.onResponse(response.body());
                }
                else {
                    listener.onFail("on response fail");
                }
            }
            @Override
            public void onFailure(Call<List<TcaseBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
