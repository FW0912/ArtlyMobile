package com.mobprog.artlymobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class ProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> balance;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        username = new MutableLiveData<>();
        balance = new MutableLiveData<>();

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);

        username.setValue(sharedPreferences.getString("username", ""));
        balance.setValue("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(sharedPreferences.getInt("balance", 0)));
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getBalance() {
        return balance;
    }
}