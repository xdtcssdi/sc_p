package com.weilai.keke.service.iface;

import com.weilai.keke.entity.SignEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetSignInfoiface {

    @GET("add_sign")
    Call<SignEntity> getSignInfo(@Query("now_time") String now_time,
                                 @Query("week") String week,
                                 @Header("Cookie") String cookie);
    @GET("add_sign")
    Call<SignEntity> addSignInfo(@Query("now_time") String now_time,
                                 @Query("week") String week,
                                 @Query("sign_time") String sign_time,
                                 @Header("Cookie") String cookie);

}
