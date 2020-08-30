package com.weilai.keke.service.iface;

import com.weilai.keke.entity.LoginState;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Loginiface {
    @GET("login")
    Call<LoginState> login(
            @Query("username") String username,
            @Query("password") String password);
}
