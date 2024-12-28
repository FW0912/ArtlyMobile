package com.mobprog.artlymobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TransactionsViewModel extends AndroidViewModel {
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> transactionsCount;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);

        username = new MutableLiveData<>();
        transactionsCount = new MutableLiveData<>();

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);

        username.setValue(sharedPreferences.getString("username", ""));
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void setTransactionsCount(int count) {
        if(count == 1) {
            transactionsCount.setValue("You have a transaction.");
        }
        else {
            transactionsCount.setValue("You have " + count + " transactions.");
        }
    }

    public MutableLiveData<String> getTransactionsCount() {
        return transactionsCount;
    }
}