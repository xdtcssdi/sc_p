package com.weilai.keke.model;

import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.service.iface.Registeriface;
import com.weilai.keke.service.listener.RegisterListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterModel extends BaseModel{

    public void register(String username, String password,String num, RegisterListener registerListener) {
        Registeriface registeriface= retrofit.create(Registeriface.class);
        Call<SuccessEntity> call = registeriface.register(username, password, num);
        call.enqueue(new Callback<SuccessEntity>() {
            @Override
            public void onResponse(Call<SuccessEntity> call, Response<SuccessEntity> response) {
                if (response != null) {
                    registerListener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessEntity> call, Throwable t) {
                registerListener.onFail(t.toString());
            }
        });
    }
}
