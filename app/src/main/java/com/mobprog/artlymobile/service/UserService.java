package com.mobprog.artlymobile.service;

import com.google.gson.JsonObject;
import com.mobprog.artlymobile.request.RegisterRequest;
import com.mobprog.artlymobile.request.TopupUserBalanceRequest;
import com.mobprog.artlymobile.result.GetUserByIdResult;
import com.mobprog.artlymobile.utils.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("/User/Login")
    Call<ApiResponse<JsonObject>> login(@Header("Authorization") String apiKey, @Query("Email") String email, @Query("Password") String password);

    @POST("/User/register")
    Call<ApiResponse<String>> register(@Header("Authorization") String apiKey, @Body() RegisterRequest request);

    @GET("/User/getuserbyid")
    Call<ApiResponse<GetUserByIdResult>> getUserById(@Header("Authorization") String apiKey, @Query("IdUser") String userId);

    @POST("/User/topupbalance")
    Call<ApiResponse>  topupUserBalance(@Header("Authorization") String apikey, @Body()TopupUserBalanceRequest request);
}
