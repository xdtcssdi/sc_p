package com.weilai.keke.model;

import android.util.Log;

import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.entity.WhiteApp;
import com.weilai.keke.service.iface.GetInfoiface;
import com.weilai.keke.service.iface.GetWhiteAppiface;
import com.weilai.keke.service.listener.GetInfoListener;
import com.weilai.keke.service.listener.GetWhiteAppListener;
import com.weilai.keke.util.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetWhiteAppModel extends BaseModel {

    private static final String TAG = "GetWhiteAppModel";

    public GetWhiteAppModel(){
        super();
        retrofit = new Retrofit.Builder()
        .baseUrl(Common.BASE)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build();
    }

    public void getWhiteApp(String cookie,final GetWhiteAppListener getWhiteAppListener) {
        Log.d(TAG, "getInfo: " +cookie);
        GetWhiteAppiface getWhiteAppiface= retrofit.create(GetWhiteAppiface.class);
        Call<String> call = getWhiteAppiface.my_white_app(cookie);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    getWhiteAppListener.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                getWhiteAppListener.onFail(t.toString());
            }
        });
    }
}
