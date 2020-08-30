package com.weilai.keke.model;

import com.weilai.keke.util.Common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseModel {
    protected Retrofit retrofit;

    public BaseModel(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
