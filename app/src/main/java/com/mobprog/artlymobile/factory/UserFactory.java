package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.User;

import java.time.LocalDateTime;

public class UserFactory {
    public static User create(String idUser, String userName, String fullName, String email, String gender, String role, LocalDateTime DOB, boolean isActive, double balance) {
        return new User(
                idUser,
                userName,
                fullName,
                email,
                gender,
                role,
                DOB,
                isActive,
                balance
        );
    }
}
