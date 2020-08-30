package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.SignEntity;
import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.service.iface.GetSignInfoiface;
import com.weilai.keke.service.iface.Successiface;
import com.weilai.keke.service.listener.GetSignListener;
import com.weilai.keke.service.listener.SuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignModel extends BaseModel {

    private static final String TAG = "SignModel";
    public void getSignInfo(String now_time, String week, String cookie, GetSignListener listener) {
        Log.d(TAG, "getSignInfo: "+now_time +week+cookie);
        GetSignInfoiface getSignInfoiface = retrofit.create(GetSignInfoiface.class);
        Call<SignEntity> call = getSignInfoiface.getSignInfo(now_time,week,cookie);
        call.enqueue(new Callback<SignEntity>() {
            @Override
            public void onResponse(Call<SignEntity> call, Response<SignEntity> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<SignEntity> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }

    public void addSignInfo(String now_time, String week, String sign_time, String cookie, GetSignListener listener) {
        GetSignInfoiface getSignInfoiface = retrofit.create(GetSignInfoiface.class);
        Call<SignEntity> call = getSignInfoiface.addSignInfo(now_time,week,sign_time,cookie);
        call.enqueue(new Callback<SignEntity>() {
            @Override
            public void onResponse(Call<SignEntity> call, Response<SignEntity> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<SignEntity> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
