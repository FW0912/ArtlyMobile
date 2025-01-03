package com.mobprog.artlymobile.controller;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.adapter.TransactionDetailsAdapter;
import com.mobprog.artlymobile.factory.TransactionDetailsFactory;
import com.mobprog.artlymobile.model.TransactionDetails;
import com.mobprog.artlymobile.result.GetTransactionDetailsResult;
import com.mobprog.artlymobile.service.TransactionDetailsService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.ErrorToast;
import com.mobprog.artlymobile.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailsController {
    private final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private Context context;
    private TransactionDetailsService service;

    public TransactionDetailsController(Context context) {
        this.context = context;
        service = RetrofitClient.getTransactionDetailsService();
    }

    public void bindTransactionDetails(RecyclerView rv, String transactionId) {
        Call<ApiResponse<List<GetTransactionDetailsResult>>> call = service.getTransactionDetails(API_KEY, transactionId);
        call.enqueue(new Callback<ApiResponse<List<GetTransactionDetailsResult>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetTransactionDetailsResult>>> call, Response<ApiResponse<List<GetTransactionDetailsResult>>> response) {
                ApiResponse<List<GetTransactionDetailsResult>> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        List<GetTransactionDetailsResult> results = r.getData();
                        List<TransactionDetails> transactionDetails = new ArrayList<>();

                        for(GetTransactionDetailsResult result : results) {
                            TransactionDetails details = TransactionDetailsFactory.create(result.getIdProduct(), result.getProductName(),
                                    result.getProductCategory().getDescription(), result.getProductType().getDescription(),
                                    result.getProductPrice(), result.getProductDescription(), result.getProductImage(), result.getQuantity());
                            transactionDetails.add(details);
                        }

                        TransactionDetailsAdapter adapter = new TransactionDetailsAdapter(transactionDetails, context);
                        rv.setAdapter(adapter);
                    }
                    else {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetTransactionDetailsResult>>> call, Throwable throwable) {
                ErrorToast.makeToast(context, throwable.getMessage());
            }
        });
    }
}
