package com.mobprog.artlymobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.TransactionDetailsController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDetailsActivity extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView rvTransactionDetails;
    private TextView tvTransactionId, tvTransactionDate, tvPaymentMethod, tvProductCount, tvQuantityCount;
    private TransactionDetailsController controller;

    private void init() {
        btnBack = findViewById(R.id.btn_transaction_details_back);

        rvTransactionDetails = findViewById(R.id.rv_transaction_details);

        tvTransactionId = findViewById(R.id.tv_transaction_details_transaction_id);
        tvTransactionDate = findViewById(R.id.tv_transaction_details_transaction_date);
        tvPaymentMethod = findViewById(R.id.tv_transaction_details_payment_method);
        tvProductCount = findViewById(R.id.tv_transaction_details_product_count);
        tvQuantityCount = findViewById(R.id.tv_transaction_details_quantity_count);

        controller = new TransactionDetailsController(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();

        String transactionId = intent.getStringExtra("transactionId");
        String transactionDate = intent.getStringExtra("transactionDate");
        String paymentMethod = intent.getStringExtra("paymentMethod");
        int productCount = intent.getIntExtra("productCount", 0);
        int quantityCount = intent.getIntExtra("quantityCount", 0);

        tvTransactionId.setText(transactionId);
        tvTransactionDate.setText(transactionDate);
        tvPaymentMethod.setText(paymentMethod);

        if(productCount == 1) {
            tvProductCount.setText("1 product was purchased in this transaction.");
        }
        else {
            tvProductCount.setText(productCount + " products were purchased in this transaction.");
        }

        if(quantityCount == 1) {
            tvQuantityCount.setText("1 item was purchased in this transaction.");
        }
        else {
            tvQuantityCount.setText(quantityCount + " items were purchased in this transaction.");
        }

        btnBack.setOnClickListener((v) -> finish());

        rvTransactionDetails.setLayoutManager(new LinearLayoutManager(this));
        controller.bindTransactionDetails(rvTransactionDetails, transactionId);
    }
}