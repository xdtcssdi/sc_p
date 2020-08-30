package com.example.amicool.model;


import com.example.amicool.bean.LoginBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.LoginListener;
import com.example.amicool.iface.Loginiface;
import com.example.amicool.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel implements Loginiface {
    private Retrofit retrofit;

    public LoginModel(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getLoginResult(String username, String pass, final LoginListener loginListener) {
        //使用Retrofit----2
        UserService userService
                =retrofit.create(UserService.class);
        Call<LoginBean> call
                =userService.login(username,pass);
        //使用Retrofit----3
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                if(response!=null && response.isSuccessful())
                {
                    loginListener.onResponse(response.body());
                }
                else
                    loginListener.onFail("onresponse fail");
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                loginListener.onFail(t.toString());
            }
        });
    }
}