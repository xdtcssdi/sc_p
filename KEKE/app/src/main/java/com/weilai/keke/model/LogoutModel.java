package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.LoginState;
import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.service.iface.Loginiface;
import com.weilai.keke.service.iface.Successiface;
import com.weilai.keke.service.listener.LoginListener;
import com.weilai.keke.service.listener.SuccessListener;
import com.weilai.keke.util.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogoutModel extends BaseModel {

    private static final String TAG = "LogoutModel";
    public void logout(String cookie, SuccessListener loginListener) {
        Log.d(TAG, cookie);
        Successiface loginiface = retrofit.create(Successiface.class);
        Call<SuccessEntity> call = loginiface.logout(cookie);
        call.enqueue(new Callback<SuccessEntity>() {
            @Override
            public void onResponse(Call<SuccessEntity> call, Response<SuccessEntity> response) {
                if (response != null) {
                    loginListener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessEntity> call, Throwable t) {
                loginListener.onFail(t.toString());
            }
        });
    }
}
