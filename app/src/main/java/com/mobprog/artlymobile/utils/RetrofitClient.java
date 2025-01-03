package com.mobprog.artlymobile.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobprog.artlymobile.service.ProductService;
import com.mobprog.artlymobile.service.TransactionDetailsService;
import com.mobprog.artlymobile.service.TransactionService;
import com.mobprog.artlymobile.service.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://artlymobileapi-fdbtd8aqfbfndsha.southeastasia-01.azurewebsites.net/";
    private static Retrofit retrofit;
    private static Gson gson;

    public static Retrofit getInstance(){
        if(retrofit == null){
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static UserService getUserService() {
        return getInstance().create(UserService.class);
    }

    public static ProductService getProductService() {
        return getInstance().create(ProductService.class);
    }

    public static TransactionService getTransactionService() {
        return getInstance().create(TransactionService.class);
    }

    public static TransactionDetailsService getTransactionDetailsService() {
        return getInstance().create(TransactionDetailsService.class);
    }
}
