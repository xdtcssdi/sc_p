package com.weilai.keke.service.iface;

import com.weilai.keke.entity.WhiteApp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetWhiteAppiface {

    @GET("my_white_app")
    Call<String> my_white_app(@Header("Cookie") String cookie);
}
