package com.example.amicool.service;

import com.example.amicool.bean.ArticleBean;
import com.example.amicool.bean.FocusResultBean;
import com.example.amicool.bean.ProjectBean;
import com.example.amicool.bean.TcaseBean;
import com.example.amicool.bean.TwareBean;
import com.example.amicool.bean.UserBean;
import com.example.amicool.bean.VideoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttentionListService {
    @GET("api.php/lists/mod/{mod}")
    Call<List<ArticleBean>> getArticleList(@Path("mod")String mod, @Query("page") int page, @Header("SessionID") String sessionID, @Query("userid") String userid);

    @GET("api.php/lists/mod/{mod}")
    Call<List<ProjectBean>> getProjectList(@Path("mod")String mod, @Query("page") int page, @Header("SessionID") String sessionID, @Query("userid") String userid);

    @GET("api.php/lists/mod/{mod}")
    Call<List<TcaseBean>> getTcaseList(@Path("mod")String mod, @Query("page") int page, @Header("SessionID") String sessionID, @Query("userid") String userid);

    @GET("api.php/lists/mod/{mod}")
    Call<List<TwareBean>> getTwareList(@Path("mod")String mod, @Query("page") int page, @Header("SessionID") String sessionID, @Query("userid") String userid);

    @GET("api.php/lists/mod/{mod}")
    Call<List<VideoBean>> getVideoList(@Path("mod")String mod, @Query("page") int page, @Header("SessionID") String sessionID, @Query("userid") String userid);

    @GET("api.php/listmyfocus/mod/{mod}focus")
    Call<List<FocusResultBean<UserBean>>> getFocusUserList(@Path("mod") String mod, @Query("page") int page, @Header("SessionID") String sessionID);

}
