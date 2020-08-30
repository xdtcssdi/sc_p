package com.weilai.keke.service.iface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface VerificationAnsweriface {
    @GET("is_correct")
    Call<String> is_correct(@Header("Cookie") String cookie,
                            @Query("pk") int pk,
                            @Query("option") String option,
                            @Query("StuAndQuePk") int StuAndQuePk);
}
