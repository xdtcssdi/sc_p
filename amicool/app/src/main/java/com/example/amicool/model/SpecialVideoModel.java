package com.example.amicool.model;

import com.example.amicool.bean.VideoBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.SpecailVideoiface;
import com.example.amicool.iface.SpecialVideoListener;
import com.example.amicool.service.ListSpecialService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpecialVideoModel implements SpecailVideoiface {
    private Retrofit retrofit;

    //构造函数
    public SpecialVideoModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getResultList(String mod, int page, String sessionID, final SpecialVideoListener listener) {
        //使用Retrofit----2
        ListSpecialService service
                =retrofit.create(ListSpecialService.class);
        Call<List<VideoBean>> call
                =service.getVideoList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<VideoBean>>() {
            @Override
            public void onResponse(Call<List<VideoBean>> call, Response<List<VideoBean>> response) {
                if(response!=null && response.isSuccessful())
                {
                    listener.onResponse(response.body());
                }
                else {
                    listener.onFail("on response fail");
                }
            }
            @Override
            public void onFailure(Call<List<VideoBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
