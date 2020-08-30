package com.weilai.keke.service.iface;

import com.weilai.keke.entity.SuccessEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Successiface {
    @GET("createTC")
    Call<SuccessEntity> createTC(@Query("classStartTime") String classStartTime,
                                 @Query("classEndTime") String classEndTime,
                                 @Query("classSignTime") String classSignTime,
                                 @Query("className") String className,
                                 @Query("teacherId") String teacherId);

    @GET("logout")
    Call<SuccessEntity> logout(@Header("Cookie") String cookie);
}
