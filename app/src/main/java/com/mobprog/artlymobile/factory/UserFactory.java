package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.User;

public class UserFactory {
    public static User create(String userId, String email, String password, String username, int balance, String profilePicPath) {
        return new User(
                userId,
                email,
                password,
                username,
                balance,
                profilePicPath
        );
    }
}
