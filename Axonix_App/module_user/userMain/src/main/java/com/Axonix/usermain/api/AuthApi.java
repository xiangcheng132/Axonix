package com.Axonix.usermain.api;

import com.Axonix.usermain.model.ApiResponse;
import com.Axonix.usermain.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/register")
    Call<ApiResponse> register(@Body RegisterRequest request);
}