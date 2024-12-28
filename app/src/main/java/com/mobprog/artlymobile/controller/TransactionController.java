package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.adapter.TransactionAdapter;
import com.mobprog.artlymobile.factory.TransactionFactory;
import com.mobprog.artlymobile.model.Transaction;
import com.mobprog.artlymobile.result.GetAllTransactionsResult;
import com.mobprog.artlymobile.service.TransactionService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.ErrorToast;
import com.mobprog.artlymobile.utils.RetrofitClient;
import com.mobprog.artlymobile.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionController {
    private final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private Context context;
    private TransactionService service;

    public TransactionController(Context context) {
        this.context = context;
        service = RetrofitClient.getTransactionService();
    }

    public void bindTransactions(RecyclerView rv, TransactionsViewModel transactionsViewModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        Call<ApiResponse<List<GetAllTransactionsResult>>> call = service.getAllTransactions(API_KEY, userId);
        call.enqueue(new Callback<ApiResponse<List<GetAllTransactionsResult>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetAllTransactionsResult>>> call, Response<ApiResponse<List<GetAllTransactionsResult>>> response) {
                ApiResponse<List<GetAllTransactionsResult>> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        List<GetAllTransactionsResult> results = r.getData();
                        List<Transaction> transactions = new ArrayList<>();

                        for(GetAllTransactionsResult result : results) {
                            Transaction transaction = TransactionFactory.create(result.getIdTransaction(),
                                    result.getOrderDate(), result.getPaymentMethod().getDescription(),
                                    result.getTransactionStatus().getDescription(), result.getProductCount(),
                                    result.getQuantityCount(), result.getTotalPrice());

                            transactions.add(transaction);
                        }

                        Collections.sort(transactions, Comparator.comparing(Transaction::getOrderDate).reversed());

                        TransactionAdapter adapter = new TransactionAdapter(transactions, transactionsViewModel, context);
                        rv.setAdapter(adapter);
                    }
                    else {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetAllTransactionsResult>>> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }
}
