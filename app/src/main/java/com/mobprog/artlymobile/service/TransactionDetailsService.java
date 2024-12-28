package com.mobprog.artlymobile.service;

import com.mobprog.artlymobile.result.GetTransactionDetailsResult;
import com.mobprog.artlymobile.utils.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TransactionDetailsService {
    @GET("/TransactionDetail/gettransactiondetails")
    Call<ApiResponse<List<GetTransactionDetailsResult>>> getTransactionDetails(@Header("Authorization") String apiKey, @Query("IdTransaction") String transactionId);
}
