package com.mobprog.artlymobile.service;

import com.mobprog.artlymobile.result.GetAllTransactionsResult;
import com.mobprog.artlymobile.utils.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TransactionService {
    @GET("/Transaction/getalltransactions")
    Call<ApiResponse<List<GetAllTransactionsResult>>> getAllTransactions(@Header("Authorization") String apiKey, @Query("IdUser") String userId);
}
