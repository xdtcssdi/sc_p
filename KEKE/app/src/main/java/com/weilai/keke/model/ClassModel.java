package com.weilai.keke.model;

import com.weilai.keke.entity.SuccessEntity;
import com.weilai.keke.service.iface.Successiface;
import com.weilai.keke.service.listener.SuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassModel extends BaseModel{

    public void createClass(String classStartTime,
                            String classEndTime, String classSignTime,
                            String className, String teacherId,
                            SuccessListener listener){

        Successiface success= retrofit.create(Successiface.class);

        Call<SuccessEntity> call=success.createTC(classStartTime,classEndTime,classSignTime,className,teacherId);

        call.enqueue(new Callback<SuccessEntity>() {
            @Override
            public void onResponse(Call<SuccessEntity> call, Response<SuccessEntity> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<SuccessEntity> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });

    }
}
