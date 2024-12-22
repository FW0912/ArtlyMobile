package com.mobprog.artlymobile.utils;

import com.mobprog.artlymobile.service.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://artlymobileapi-fdbtd8aqfbfndsha.southeastasia-01.azurewebsites.net/";
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UserService getUserService() {
        return getInstance().create(UserService.class);
    }
}
