package com.example.amicool.model;

import com.example.amicool.bean.ArticleBean;
import com.example.amicool.bean.CollectBean;
import com.example.amicool.iface.CollectListListener;
import com.example.amicool.iface.CollectListiface;
import com.example.amicool.service.CollectListService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CArticleModel implements CollectListiface<ArticleBean> {
    private Retrofit retrofit;
    private String BASEURL
            ="http://amicool.neusoft.edu.cn/";
    //构造函数
    public CArticleModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Override
    public void getResultList(String mod, int page, String sessionID, final CollectListListener<ArticleBean> listener) {
        //使用Retrofit----2
        CollectListService service
                =retrofit.create(CollectListService.class);
        Call<List<CollectBean<ArticleBean>>> call
                =service.getCArticleList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<CollectBean<ArticleBean>>>() {
            @Override
            public void onResponse(Call<List<CollectBean<ArticleBean>>> call, Response<List<CollectBean<ArticleBean>>> response) {
                if(response!=null && response.isSuccessful())
                {  listener.onResponse(response.body());
                }
                else {
                    listener.onFail("onresponse fail");
                }
            }
            @Override
            public void onFailure(Call<List<CollectBean<ArticleBean>>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
