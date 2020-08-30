package com.example.amicool.model;

import com.example.amicool.common.Common;
import com.example.amicool.iface.RegisterListener;
import com.example.amicool.iface.Registeriface;
import com.example.amicool.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterModel implements Registeriface {
    private Retrofit retrofit;

    public RegisterModel(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Override
    public void getRegisterResult(String username, String pass, String tel, String roleid, String email, final RegisterListener registerListener) {
        //使用Retrofit----2
        UserService userService
                =retrofit.create(UserService.class);
        Call<String> call
                =userService.register(username,pass,tel,roleid,email);
        //使用Retrofit----3
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response!=null && response.isSuccessful())
                {
                    registerListener.onResponse(response.body());
                }
                else{
                    registerListener.onFail("onresponse fail");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                registerListener.onFail(t.toString());
            }
        });
    }
}
