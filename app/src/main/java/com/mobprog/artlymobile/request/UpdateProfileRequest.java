package com.mobprog.artlymobile.request;

import java.util.Date;

public class UpdateProfileRequest {
    private final String idUser;
    private final String fullName;
    private final String email;
    private final String Password;
    private final Date dob;

    public UpdateProfileRequest(String idUser, String fullName, String email, String password, Date dob) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        Password = password;
        this.dob = dob;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return Password;
    }

    public Date getDob() {
        return dob;
    }
}
