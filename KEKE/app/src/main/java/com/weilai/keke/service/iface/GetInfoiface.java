package com.weilai.keke.service.iface;
import com.weilai.keke.entity.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetInfoiface {
    @GET("getInfo")
    Call<UserInfo> getInfo(@Header("Cookie") String cookie);
}