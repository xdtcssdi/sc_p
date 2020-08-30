package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.TeachingClassEntity;
import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.service.iface.GetInfoiface;
import com.weilai.keke.service.iface.GetMyClassiface;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.service.listener.GetMyClassListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMyClassModel extends BaseModel {

    private static final String TAG = "GetMyClassModel";
    public void get_my_class(String cookie, final GetMyClassListener getMyClassListener) {
        Log.d(TAG, "get_my_class: " +cookie);
        GetMyClassiface getMyClassiface= retrofit.create(GetMyClassiface.class);
        Call<TeachingClassEntity> call = getMyClassiface.get_my_class(cookie);
        call.enqueue(new Callback<TeachingClassEntity>() {
            @Override
            public void onResponse(Call<TeachingClassEntity> call, Response<TeachingClassEntity> response) {
                if (response != null) {
                    getMyClassListener.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<TeachingClassEntity> call, Throwable t) {
                getMyClassListener.onFail(t.toString());
            }
        });
    }
}
