package com.mobprog.artlymobile.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.UserController;

public class LoginActivity extends AppCompatActivity {
    public static String PREFS_LOGIN_KEY = "LoggedInUser";

    private EditText et_email, et_password;
    private Button btn_login, btn_register;
    private UserController userController;
    private SharedPreferences sharedPreferences;

    private void init() {
        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_goToRegister);

        userController = new UserController(this);

        sharedPreferences = getSharedPreferences(PREFS_LOGIN_KEY, Context.MODE_PRIVATE);
    }

    private void setClickListeners() {
        btn_login.setOnClickListener((v) -> {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            userController.login(email, password);
        });

        btn_register.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setClickListeners();

        if(sharedPreferences.contains("userId")) {
            Intent intent = new Intent(this, BottomNavigationActivity.class);
            this.startActivity(intent);
        }
    }
}