package com.mobprog.artlymobile.controller;

import android.util.Patterns;

import com.mobprog.artlymobile.factory.UserFactory;
import com.mobprog.artlymobile.model.User;
import com.mobprog.artlymobile.utils.ControllerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class UserController {
    private List<User> userTable;

    public UserController() {
        userTable = new ArrayList<>();
        userTable.add(UserFactory.create(UUID.randomUUID().toString(), "user@gmail.com", "password", "user", 0, null));
    }

    public ControllerResponse login(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            return new ControllerResponse(false, "Email or password field is empty!");
        }

        for(User user : userTable) {
            if(!email.equals(user.getEmail())) {
                return new ControllerResponse(false, "Found no user registered with the email!");
            }

            if(!password.equals(user.getPassword())) {
                return new ControllerResponse(false, "Password is incorrect!");
            }
        }

        return new ControllerResponse(true, "Login successful");
    }

    public ControllerResponse register(String username, String email, String password) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ControllerResponse(false, "One or more fields are empty!");
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

        for(User user : userTable) {
            if(email.equals(user.getEmail())) {
                return new ControllerResponse(false, "Found another user with the same email!");
            }
        }

        userTable.add(UserFactory.create(UUID.randomUUID().toString(), email, password, username, 0, null));

        return new ControllerResponse(true, "Register successful");
    }
}
