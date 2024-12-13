package com.mobprog.artlymobile.model;

import androidx.annotation.Nullable;

public class User {
    private String userId;
    private String email;
    private String password;
    private String username;
    private int balance;

    @Nullable
    private String profilePicPath;

    public User(String userId, String email, String password, String username, int balance, String profilePicPath) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.balance = balance;
        this.profilePicPath = profilePicPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }
}
