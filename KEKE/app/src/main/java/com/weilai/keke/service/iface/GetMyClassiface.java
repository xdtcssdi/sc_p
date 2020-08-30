package com.weilai.keke.service.iface;

import com.weilai.keke.entity.TeachingClassEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetMyClassiface {
    @GET("get_my_class")
    Call<TeachingClassEntity> get_my_class(@Header("Cookie") String cookie);
}
