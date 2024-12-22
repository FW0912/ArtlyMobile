package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;

import com.google.gson.JsonObject;
import com.mobprog.artlymobile.request.RegisterRequest;
import com.mobprog.artlymobile.service.UserService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.ControllerResponse;
import com.mobprog.artlymobile.utils.RetrofitClient;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {
    private static final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private UserService service;
    private Context context;
    private ControllerResponse controllerResponse;

    public UserController(Context context) {
        service = RetrofitClient.getUserService();
        this.context = context;
    }

    public ControllerResponse login(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            return new ControllerResponse(false, "Email or password field is empty!");
        }

        Call<ApiResponse<String>> call = service.login(API_KEY, email, password);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                ApiResponse<String> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        String UserId = r.getData();

                        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("UserId", UserId);

                        editor.commit();

                        controllerResponse = new ControllerResponse(true, "Login successful");
                    }
                    else {
                        controllerResponse = new ControllerResponse(false, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                controllerResponse = new ControllerResponse(false, "Failed");
            }
        });

        return controllerResponse;
    }

    public ControllerResponse register(String username, String fullname, String email, String password) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ControllerResponse(false, "One or more required fields are empty!");
        }

        if(username.length() < 5) {
            return new ControllerResponse(false, "Username must be more than 5 characters long!");
        }

        if(!Pattern.matches("^[a-zA-Z0-9]*$", username)) {
            return new ControllerResponse(false, "Username must be alphanumeric!");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ControllerResponse(false, "Email must be a valid email address!");
        }

        if(password.length() < 5) {
            return new ControllerResponse(false, "Password must be more than 5 characters long!");
        }

        if(!Pattern.matches("^[a-zA-Z0-9]*$", password)) {
            return new ControllerResponse(false, "Password must be alphanumeric!");
        }

        RegisterRequest request = new RegisterRequest(username, password, email, fullname);
        Call<ApiResponse<String>> call = service.register(API_KEY, request);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                ApiResponse<String> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        controllerResponse = new ControllerResponse(true, "Register successful");
                    }
                    else {
                        controllerResponse = new ControllerResponse(false, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                controllerResponse = new ControllerResponse(false, throwable.getMessage());
            }
        });

        return controllerResponse;
    }
}
