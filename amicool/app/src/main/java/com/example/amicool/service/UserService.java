package com.example.amicool.service;


import com.example.amicool.bean.LoginBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET("api.php/login/username/{username}/password/{password}")
    Call<LoginBean> login(@Path("username") String username,
                          @Path("password") String password);


    @GET("api.php/reg/username/{username}/password/{password}/tel/{tel}/roleid/{roleid}/email/{email}")
    Call<String> register(@Path("username") String username,
                                @Path("password") String password,
                                @Path("tel") String tel,
                                @Path("roleid") String roleid,
                                @Path("email") String email);
    
}