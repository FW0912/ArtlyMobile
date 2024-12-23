package com.mobprog.artlymobile.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ProductService {
    @GET("/Product/getallproducts")
    Call<JsonObject> getAllProducts(@Header("Authorization") String apiKey);
}
