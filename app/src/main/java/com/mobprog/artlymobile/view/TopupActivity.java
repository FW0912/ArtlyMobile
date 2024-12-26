package com.mobprog.artlymobile.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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
import com.mobprog.artlymobile.request.TopupUserBalanceRequest;

public class TopupActivity extends AppCompatActivity {

    private TextView currentBalance;
    private EditText inputBalance;
    private Button backbtn, btnTopup;
    private UserController userController;
    private String idUser;

    protected void init(){
        currentBalance = findViewById(R.id.tv_topup_balance);
        inputBalance = findViewById(R.id.et_topup_balance);
        backbtn = findViewById(R.id.btn_topup_back);
        btnTopup = findViewById(R.id.btn_topup);

        userController = new UserController(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LoggedInUser", MODE_PRIVATE);
        int curBalance = sharedPreferences.getInt("balance", 0);
        idUser = sharedPreferences.getString("userId", null);

        currentBalance.setText(String.format("Rp. %,d", curBalance));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topup_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        backbtn.setOnClickListener(e -> {
            finish();
        });

        btnTopup.setOnClickListener(e -> {
            String inputedBalanceStr = inputBalance.getText().toString();

            if (inputedBalanceStr.isEmpty()) {
                inputBalance.setError("Please enter a valid amount");
                return;
            }

            int inputedBalance;

            try {
                inputedBalance = Integer.parseInt(inputedBalanceStr);
            } catch (NumberFormatException ex) {
                inputBalance.setError("Invalid number format");
                return;
            }

            if (inputedBalance <= 0) {
                inputBalance.setError("Amount must be greater than 0");
                return;
            }

            userController.topupBalance(new TopupUserBalanceRequest(idUser, inputedBalance));
            new android.os.Handler().postDelayed(() -> {
                finish();
            }, 1500);
        });
    }
}