package com.example.amicool.model;

import com.example.amicool.bean.ArticleBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.ArticleListener;
import com.example.amicool.iface.Articleiface;
import com.example.amicool.service.ArticleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleModel implements Articleiface {
    private Retrofit retrofit;

    //构造函数
    public ArticleModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getResultList(String mod, int page, String sessionID, final ArticleListener listener) {
        //使用Retrofit----2
        ArticleService service
                =retrofit.create(ArticleService.class);
        Call<List<ArticleBean>> call
                =service.getArticleList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<ArticleBean>>() {
            @Override
            public void onResponse(Call<List<ArticleBean>> call, Response<List<ArticleBean>> response) {
                if(response.isSuccessful() && response!=null)
                {
                    listener.onResponse(response.body());
                }
                else {
                    listener.onFail("on response fail");
                }
            }
            @Override
            public void onFailure(Call<List<ArticleBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
