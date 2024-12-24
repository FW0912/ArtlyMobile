package com.mobprog.artlymobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CartViewModel extends AndroidViewModel {
    private final MutableLiveData<String> username;

    public CartViewModel(@NonNull Application application) {
        super(application);

        username = new MutableLiveData<>();

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);

        username.setValue(sharedPreferences.getString("username", ""));
    }

    public LiveData<String> getUsername() {
        return username;
    }
}