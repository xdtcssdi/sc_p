package com.example.amicool.service;

import com.example.amicool.bean.VideoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListSpecialService {
    @GET("api.php/listspecial/mod/{mod}")
    Call<List<VideoBean>> getVideoList(
            @Path("mod") String mod,
            @Query("page") int page,
            @Header("SessionID") String sessionID);
}
