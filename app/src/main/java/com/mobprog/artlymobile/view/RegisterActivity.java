package com.mobprog.artlymobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.UserController;
import com.mobprog.artlymobile.utils.ControllerResponse;
import com.mobprog.artlymobile.utils.ErrorToast;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username, et_full_name, et_email, et_password;
    private Button btn_register, btn_login;
    private UserController userController;

    private void init() {
        et_username = findViewById(R.id.et_register_username);
        et_full_name = findViewById(R.id.et_register_full_name);
        et_email = findViewById(R.id.et_register_email);
        et_password = findViewById(R.id.et_register_password);

        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_goToLogin);

        userController = new UserController(this);
    }

    private void setClickListeners() {
        btn_register.setOnClickListener((v) -> {
            String username = et_username.getText().toString();
            String fullName = et_full_name.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            ControllerResponse response = userController.register(username, fullName, email, password);

            if(response == null) {
                Log.e("response", "no response");
            }
            else if(response.isSuccessful()) {
                Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show();
            }
            else {
                ErrorToast.makeToast(this, response.getMessage());
            }
        });

        btn_login.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setClickListeners();
    }
}