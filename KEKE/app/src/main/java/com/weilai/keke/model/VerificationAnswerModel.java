package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.service.iface.GetInfoiface;
import com.weilai.keke.service.iface.VerificationAnsweriface;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.service.listener.VerificationAnswerListener;
import com.weilai.keke.util.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VerificationAnswerModel extends BaseModel {

    private static final String TAG = "VerificationAnswerModel";
    public VerificationAnswerModel(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    public void is_correct(String cookie,int pk,String option,int StuAndQuePk,final VerificationAnswerListener listener) {
        Log.d(TAG, "is_correct: " +cookie);
        VerificationAnsweriface answeriface= retrofit.create(VerificationAnsweriface.class);
        Call<String> call = answeriface.is_correct(cookie,pk,option,StuAndQuePk);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    listener.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
