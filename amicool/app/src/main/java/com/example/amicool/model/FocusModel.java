package com.example.amicool.model;

import android.util.Log;

import com.example.amicool.iface.AttentionListener;
import com.example.amicool.iface.Attentioniface;
import com.example.amicool.service.AttentionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.amicool.common.Common.BASEURL;

public class FocusModel implements Attentioniface {
    @Override
    public void focus(String mod, int idolid, String sessionID, final AttentionListener attentionListener) {
        //使用Retrofit----2
        AttentionService collectService = retrofit.create(AttentionService.class);
        Call<String> call = collectService.focus(mod, idolid, sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body().trim().toString().equals("0")) {
                        attentionListener.onResponse("1");//关注失败
                    } else {
                        attentionListener.onResponse("2");//关注成功
                    }

                } else {
                    attentionListener.onFail("collect onresponse fail");//
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                attentionListener.onFail("关注失败：" + t.toString());
            }
        });
    }


    @Override
    public void exists(String mod, int idolid, String sessionID, final AttentionListener attentionListener) {
        Log.d(TAG,  " " +idolid + " " +sessionID);
        //使用Retrofit----2
        final AttentionService attentionService = retrofit.create(AttentionService.class);
        Call<String> call = attentionService.exists(mod,idolid, sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body().trim().toString().equals("0"))//已关注
                    {
                        attentionListener.onResponse("5");
                    } else
                    {
                        attentionListener.onResponse("6");
                    }

                } else {
                    attentionListener.onFail("collect exist onresponse fail");//
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                attentionListener.onFail("判断关注失败：" + t.toString());
            }
        });
    }
    private static final String TAG = "FocusModel";

    @Override
    public void unfocus(String mod, int idolid, String sessionID, final AttentionListener attentionListener) {
        Log.d(TAG, mod + " " +idolid + " " +sessionID);
        //使用Retrofit----2
        AttentionService attentionService = retrofit.create(AttentionService.class);
        Call<String> call = attentionService.unfocus(mod, idolid, sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body().trim().toString().equals("0")) {
                        attentionListener.onResponse("3");//取消关注失败
                    } else {
                        attentionListener.onResponse("4");//取消关注成功
                    }
                } else {
                    attentionListener.onFail("uncollect onresponse fail");//uncollect onresponse fail
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                attentionListener.onFail("取消关注失败：" + t.toString());
            }
        });
    }

    ;


    private Retrofit retrofit;

    //构造函数
    public FocusModel() {   //使用Retrofit----1
        retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

}
