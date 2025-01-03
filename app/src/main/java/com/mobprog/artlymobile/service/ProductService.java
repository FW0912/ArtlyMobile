package com.mobprog.artlymobile.service;

import com.google.gson.JsonObject;
import com.mobprog.artlymobile.request.BuyProductsRequest;
import com.mobprog.artlymobile.result.GetProductByIdResult;
import com.mobprog.artlymobile.utils.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/Product/getallproducts")
    Call<JsonObject> getAllProducts(@Header("Authorization") String apiKey);

    @GET("/Product/getproductbyid")
    Call<ApiResponse<GetProductByIdResult>> getProductById(@Header("Authorization") String apiKey, @Query("id") String productId);

    @POST("/Product/buyproducts")
    Call<ApiResponse<Integer>> buyProducts(@Header("Authorization") String apiKey, @Body BuyProductsRequest request);
}
