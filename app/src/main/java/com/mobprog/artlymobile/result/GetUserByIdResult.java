package com.mobprog.artlymobile.result;

import com.mobprog.artlymobile.utils.EntityValue;

import java.util.Date;

public class GetUserByIdResult extends EntityValue {
    private final String userName;
    private final String fullName;
    private final String email;
    private final String password;
    private final EntityValue gender;
    private final EntityValue role;
    private final Date dob;
    private final int balance;

    public GetUserByIdResult(String id, String description, String userName, String fullName, String email, String password, EntityValue gender, EntityValue role, Date dob, int balance) {
        super(id, description);
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.dob = dob;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public EntityValue getGender() {
        return gender;
    }

    public EntityValue getRole() {
        return role;
    }

    public Date getDob() {
        return dob;
    }

    public int getBalance() {
        return balance;
    }
}
