package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.service.iface.GetInfoiface;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.util.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetInfoModel extends BaseModel {

    private static final String TAG = "GetInfoModel";
    public void getInfo(String cookie,final GetInfoListener getInfoListener) {
        Log.d(TAG, "getInfo: " +cookie);
        GetInfoiface getInfoiface= retrofit.create(GetInfoiface.class);
        Call<UserInfo> call = getInfoiface.getInfo(cookie);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response != null) {
                    getInfoListener.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                getInfoListener.onFail(t.toString());
            }
        });
    }
}
