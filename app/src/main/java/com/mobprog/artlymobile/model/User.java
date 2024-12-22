package com.mobprog.artlymobile.model;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class User {
    private String IdUser;
    private String UserName;
    @Nullable
    private String FullName;
    private String Email;
    @Nullable
    private String Gender;
    private String Role;
    @Nullable
    private LocalDateTime DOB;
    private boolean IsActive;
    private double Balance;

    public User(String idUser, String userName, String fullName, String email, String gender, String role, LocalDateTime DOB, boolean isActive, double balance) {
        IdUser = idUser;
        UserName = userName;
        FullName = fullName;
        Email = email;
        Gender = gender;
        Role = role;
        this.DOB = DOB;
        IsActive = isActive;
        Balance = balance;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public LocalDateTime getDOB() {
        return DOB;
    }

    public void setDOB(LocalDateTime DOB) {
        this.DOB = DOB;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }
}
