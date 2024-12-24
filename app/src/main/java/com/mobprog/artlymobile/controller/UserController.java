package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.request.RegisterRequest;
import com.mobprog.artlymobile.service.UserService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.ErrorToast;
import com.mobprog.artlymobile.utils.RetrofitClient;
import com.mobprog.artlymobile.view.BottomNavigationActivity;
import com.mobprog.artlymobile.view.LoginActivity;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {
    private final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private UserService service;
    private Context context;

    public UserController(Context context) {
        service = RetrofitClient.getUserService();
        this.context = context;
    }

    public void login(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            ErrorToast.makeToast(context, "One or both fields are missing!");
            return;
        }

        Call<ApiResponse<JsonObject>> call = service.login(API_KEY, email, password);
        call.enqueue(new Callback<ApiResponse<JsonObject>>() {
            @Override
            public void onResponse(Call<ApiResponse<JsonObject>> call, Response<ApiResponse<JsonObject>> response) {
                ApiResponse<JsonObject> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.clear();

                        JsonObject json = r.getData();
                        String userId = json.get("id").getAsString();
                        String username = json.get("userName").getAsString();
                        String fullName = json.get("fullName").getAsString();
                        String email = json.get("email").getAsString();
                        String password = json.get("password").getAsString();
                        int balance = json.get("balance").getAsInt();

                        editor.putString("userId", userId);
                        editor.putString("username", username);
                        editor.putString("fullName", fullName);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putInt("balance", balance);

                        if(!json.get("gender").isJsonNull()) {
                            JsonObject genderJson = json.get("gender").getAsJsonObject();
                            String gender = genderJson.get("description").getAsString();
                            editor.putString("gender", gender);
                        }

                        if(!json.get("role").isJsonNull()) {
                            JsonObject roleJson = json.get("role").getAsJsonObject();
                            String role = roleJson.get("description").getAsString();
                            editor.putString("role", role);
                        }

                        if(!json.get("dob").isJsonNull()) {
                            String dob = json.get("dob").getAsString();
                            editor.putString("dob", dob);
                        }

                        editor.commit();

                        Intent intent = new Intent(context, BottomNavigationActivity.class);
                        context.startActivity(intent);
                    }
                    else {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<JsonObject>> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }

    public void register(String username, String fullname, String email, String password) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            ErrorToast.makeToast(context, "One or more required fields are empty!");
            return;
        }

        if(username.length() < 5) {
            ErrorToast.makeToast(context, "Username must be more than 5 characters long!");
            return;
        }

        if(!Pattern.matches("^[a-zA-Z0-9]*$", username)) {
            ErrorToast.makeToast(context, "Username must be alphanumeric!");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ErrorToast.makeToast(context, "Email must be a valid email address!");
            return;
        }

        if(password.length() < 5) {
            ErrorToast.makeToast(context, "Password must be at least 5 characters long!");
            return;
        }

        if(!Pattern.matches("^[a-zA-Z0-9]*$", password)) {
            ErrorToast.makeToast(context, "Password must be alphanumeric!");
            return;
        }

        RegisterRequest request = new RegisterRequest(username, password, email, fullname);
        Call<ApiResponse<String>> call = service.register(API_KEY, request);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                ApiResponse<String> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }
}
