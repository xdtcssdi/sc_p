package com.example.amicool.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttentionService {
    //关注
    @GET("api.php/create/mod/{mod}")
    Call<String> focus(@Query("mod") String mod, @Query("idolid") int idolid,
                       @Header("SessionID") String sessionID);
    //是否已关注
    @GET("api.php/exists/mod/{mod}")
    Call<String> exists(@Query("mod") String mod,@Query("idolid") int idolid, @Header("SessionID") String sessionID);

    //取消关注
    @GET("api.php/delete/mod/{mod}")
    Call<String> unfocus(@Path("mod") String mod,@Query("idolid") int idolid,@Header("SessionID") String sessionID);

}
