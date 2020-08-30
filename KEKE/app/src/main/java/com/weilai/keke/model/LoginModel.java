package com.weilai.keke.model;

import com.weilai.keke.entity.LoginState;
import com.weilai.keke.service.iface.Loginiface;
import com.weilai.keke.service.listener.LoginListener;
import com.weilai.keke.util.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel extends BaseModel {



    public void login(String username, String password, LoginListener loginListener) {
        Loginiface loginiface = retrofit.create(Loginiface.class);
        Call<LoginState> call = loginiface.login(username, password);
        call.enqueue(new Callback<LoginState>() {
            @Override
            public void onResponse(Call<LoginState> call, Response<LoginState> response) {
                if (response != null) {
                    loginListener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginState> call, Throwable t) {
                loginListener.onFail(t.toString());
            }
        });
    }
}
