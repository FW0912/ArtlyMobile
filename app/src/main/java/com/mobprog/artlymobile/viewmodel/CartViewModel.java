package com.mobprog.artlymobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.NumberFormat;
import java.util.Locale;

public class CartViewModel extends AndroidViewModel {
    private int balance;
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> currentBalance;
    private final MutableLiveData<Integer> totalItems;
    private final MutableLiveData<String> totalItemsString;
    private final MutableLiveData<String> totalPrice;
    private final MutableLiveData<Integer> balanceAfter;
    private final MutableLiveData<String> balanceAfterString;

    public CartViewModel(@NonNull Application application) {
        super(application);

        username = new MutableLiveData<>();
        currentBalance = new MutableLiveData<>();
        totalItems = new MutableLiveData<>();
        totalItemsString = new MutableLiveData<>();
        totalPrice = new MutableLiveData<>();
        balanceAfter = new MutableLiveData<>();
        balanceAfterString = new MutableLiveData<>();

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        username.setValue(sharedPreferences.getString("username", ""));

        balance = sharedPreferences.getInt("balance", 0);
        currentBalance.setValue("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(balance));

        balanceAfter.setValue(balance);
        balanceAfterString.setValue(currentBalance.getValue());
    }

    public void updateBalanceAfterPurchase(int balance) {
        currentBalance.setValue("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(balance));

        balanceAfter.setValue(balance);
        balanceAfterString.setValue(currentBalance.getValue());
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getCurrentBalance() {
        return currentBalance;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems.setValue(totalItems);

        if(totalItems == 0) {
            this.totalItemsString.setValue("You have no items in your cart.");
        }
        else if(totalItems == 1) {
            this.totalItemsString.setValue("You have an item in your cart.");
        }
        else {
            this.totalItemsString.setValue("You have " + totalItems + " items in your cart.");
        }
    }

    public LiveData<Integer> getTotalItems() {
        return totalItems;
    }

    public LiveData<String> getTotalItemsString() {
        return totalItemsString;
    }

    public void setTotalPrice(int totalPrice) {
        if(totalPrice == 0) {
            this.totalPrice.setValue("IDR 0");
            balanceAfter.setValue(balance);
            balanceAfterString.setValue(currentBalance.getValue());
        }
        else {
            this.totalPrice.setValue("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(totalPrice));
            balanceAfter.setValue(balance - totalPrice);
            balanceAfterString.setValue("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(balanceAfter.getValue()));
        }
    }

    public LiveData<String> getTotalPrice() {
        return totalPrice;
    }

    public LiveData<Integer> getBalanceAfter() {
        return balanceAfter;
    }

    public LiveData<String> getBalanceAfterString() {
        return balanceAfterString;
    }
}