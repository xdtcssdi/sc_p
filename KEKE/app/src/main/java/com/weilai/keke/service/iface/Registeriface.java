package com.weilai.keke.service.iface;

import com.weilai.keke.entity.SuccessEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Registeriface {
    @GET("register")
    Call<SuccessEntity> register(@Query("username") String username,
                                 @Query("password") String password,
                                 @Query("stuNo") String stuNo);
}
