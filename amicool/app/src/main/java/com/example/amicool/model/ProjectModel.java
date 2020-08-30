package com.example.amicool.model;

import com.example.amicool.bean.ProjectBean;
import com.example.amicool.bean.TcaseBean;
import com.example.amicool.common.Common;
import com.example.amicool.iface.ProjectListener;
import com.example.amicool.iface.Projectiface;
import com.example.amicool.iface.TcaseListener;
import com.example.amicool.iface.Tcaseiface;
import com.example.amicool.service.ProjectService;
import com.example.amicool.service.TcaseService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectModel implements Projectiface {
    private Retrofit retrofit;

    //构造函数
    public ProjectModel()
    {   //使用Retrofit----1
        retrofit=new Retrofit.Builder()
                .baseUrl(Common.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void getResultList(String mod, int page, String sessionID, final ProjectListener listener) {
        //使用Retrofit----2
        ProjectService service
                =retrofit.create(ProjectService.class);
        Call<List<ProjectBean>> call
                =service.getProjectList(mod,page,sessionID);
        //使用Retrofit----3
        call.enqueue(new Callback<List<ProjectBean>>() {
            @Override
            public void onResponse(Call<List<ProjectBean>> call, Response<List<ProjectBean>> response) {
                if( response!=null&&response.isSuccessful() )
                {
                    listener.onResponse(response.body());
                }
                else {
                    listener.onFail("on response fail");
                }
            }
            @Override
            public void onFailure(Call<List<ProjectBean>> call, Throwable t) {
                listener.onFail(t.toString());
            }
        });
    }
}
