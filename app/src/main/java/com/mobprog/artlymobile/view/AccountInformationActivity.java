package com.mobprog.artlymobile.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.UserController;
import com.mobprog.artlymobile.request.UpdateProfileRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountInformationActivity extends AppCompatActivity {

    private Button back_btn, update_btn;
    private TextView username_text;
    private EditText fullname_text, email_text, password_text;
    private DatePicker datePicker;
    private SharedPreferences sharedPreferences;
    private UserController userController;

    private String idUser;

    public void init() {
        Date realDob = null;

        userController = new UserController(this);

        back_btn = findViewById(R.id.btn_account_information_back);
        update_btn = findViewById(R.id.btn_account_information_save);

        sharedPreferences = getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String fullName = sharedPreferences.getString("fullName", null);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        long dobTimestamp = sharedPreferences.getLong("dob", -1);
        if (dobTimestamp != -1) {
            realDob = new Date(dobTimestamp);
        }

        idUser = sharedPreferences.getString("userId", null);

        username_text = findViewById(R.id.tv_account_information_username);
        fullname_text = findViewById(R.id.ET_account_information_fullname);
        email_text = findViewById(R.id.ET_account_information_email);
        password_text = findViewById(R.id.ET_account_information_password);
        datePicker = findViewById(R.id.dp_account_information_dob);

        username_text.setText(username);
        fullname_text.setText(fullName);
        email_text.setText(email);
        password_text.setText(password);

        if (realDob != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(realDob);

            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            datePicker.updateDate(year, month, day);
        } else {
            Calendar today = Calendar.getInstance();

            int year = today.get(java.util.Calendar.YEAR);
            int month = today.get(java.util.Calendar.MONTH);
            int day = today.get(java.util.Calendar.DAY_OF_MONTH);

            datePicker.updateDate(year, month, day);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        back_btn.setOnClickListener((v) -> finish());

        update_btn.setOnClickListener((e) -> {

            String input_Fullname = fullname_text.getText().toString();
            String input_email = email_text.getText().toString();
            String input_password = password_text.getText().toString();
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            Date input_dob = calendar.getTime();


            UpdateProfileRequest request = new UpdateProfileRequest(
                    idUser,
                    input_Fullname,
                    input_email,
                    input_password,
                    input_dob
            );

            userController.updateProfile(request, success -> {
                if (success) {
                    recreate(); // Refresh activity
                } else {
                    Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}