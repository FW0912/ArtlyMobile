package com.mobprog.artlymobile.request;

public class RegisterRequest {
    private final String username;
    private final String password;
    private final String email;
    private final String fullname;

    public RegisterRequest(String username, String password, String email, String fullname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
    }
}
