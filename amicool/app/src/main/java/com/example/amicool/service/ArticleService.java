package com.example.amicool.service;

import com.example.amicool.bean.ArticleBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("api.php/lists/mod/{mod}")
    Call<List<ArticleBean>> getArticleList(
            @Path("mod") String mod,
            @Query("page") int page,
            @Header("SessionID") String sessionID);
}
