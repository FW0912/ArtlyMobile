package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobprog.artlymobile.request.RegisterRequest;
import com.mobprog.artlymobile.request.TopupUserBalanceRequest;
import com.mobprog.artlymobile.request.UpdateProfileRequest;
import com.mobprog.artlymobile.result.GetUserByIdResult;
import com.mobprog.artlymobile.service.UserService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.EntityValue;
import com.mobprog.artlymobile.utils.ErrorToast;
import com.mobprog.artlymobile.utils.RetrofitClient;
import com.mobprog.artlymobile.utils.SuccessToast;
import com.mobprog.artlymobile.view.BottomNavigationActivity;
import com.mobprog.artlymobile.view.LoginActivity;
import com.mobprog.artlymobile.view.TopupActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
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

                        if (!json.get("dob").isJsonNull()) {
                            String dobString = json.get("dob").getAsString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                Date dob = dateFormat.parse(dobString);
                                long dobTimestamp = dob.getTime();
                                editor.putLong("dob", dobTimestamp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        editor.commit();

                        Intent intent = new Intent(context, BottomNavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public void logout() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        SuccessToast.makeToast(context, "You have successfully logged out.");
    }

    public void refreshLatestUserData(String IdUser) {
        if(IdUser.equals("") || IdUser == null){
            ErrorToast.makeToast(context, "Error");
            return;
        }

        Call<ApiResponse<GetUserByIdResult>> call = service.getUserById(API_KEY, IdUser);
        call.enqueue(new Callback<ApiResponse<GetUserByIdResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<GetUserByIdResult>> call, Response<ApiResponse<GetUserByIdResult>> response) {
                ApiResponse<GetUserByIdResult> r = response.body();
                Log.d("UserController", "Status Code: " + r.getStatusCode());

                if(response.isSuccessful()){
                    if(r.getStatusCode() == 200){
                        GetUserByIdResult data = r.getData();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();

                        String userId = data.getId();
                        String username = data.getUserName();
                        String fullName = data.getFullName();
                        String email = data.getEmail();
                        String password = data.getPassword();
                        int balance = data.getBalance();

                        editor.putString("userId", userId);
                        editor.putString("username", username);
                        editor.putString("fullName", fullName);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putInt("balance", balance);

                        if(data.getGender() != null) {
                            EntityValue gender = data.getGender();
                            String genderDescription = gender.getDescription();
                            editor.putString("gender", genderDescription);
                        }

                        if(data.getRole() != null) {
                            EntityValue role = data.getRole();
                            String roleDescription = role.getDescription();
                            editor.putString("role", roleDescription);
                        }

                        if(data.getDob() != null) {
                            if (data.getDob() != null) {
                                Date dob = data.getDob();
                                long dobTimestamp = dob.getTime();
                                editor.putLong("dob", dobTimestamp);
                            }
                        }

                        editor.commit();
                    }
                    else{
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<GetUserByIdResult>> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }

    public void topupBalance(TopupUserBalanceRequest request) {
        if(request.getIdUser().equals("") || request.getIdUser() == null){
            ErrorToast.makeToast(context, "You have to log in before you can topup your balance");
            return;
        }
        if(request.getAmount() == 0){
            ErrorToast.makeToast(context, "Input have to be greater than 0!");
            return;
        }

        Call<ApiResponse> call = service.topupUserBalance(API_KEY, request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse r = response.body();

                if(response.isSuccessful()){
                    if(r.getStatusCode() == 200){
                        refreshLatestUserData(request.getIdUser());

                        SuccessToast.makeToast(context, "Top-up successful!");
                    }
                    else {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }

    public void updateProfile(UpdateProfileRequest request, Consumer<Boolean> callback) {
        if (request.getIdUser() == null || request.getIdUser().isEmpty()) {
            ErrorToast.makeToast(context, "You have to log in before you can update your profile");
            callback.accept(false); // Kirim status gagal jika ID User tidak valid
            return;
        }

        Call<ApiResponse> call = service.updateProfile(API_KEY, request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse r = response.body();
                    if (r.getStatusCode() == 200) {
                        refreshLatestUserData(request.getIdUser());
                        SuccessToast.makeToast(context, "Update Profile successful!");
                        new android.os.Handler().postDelayed(() -> {
                            callback.accept(true);
                        }, 1000);

                    } else {
                        ErrorToast.makeToast(context, "Failed to update profile!");
                        callback.accept(false);
                    }
                } else {
                    ErrorToast.makeToast(context, "Server error: " + response.message());
                    callback.accept(false);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong: " + throwable.getMessage());
                callback.accept(false);
            }
        });
    }

}
