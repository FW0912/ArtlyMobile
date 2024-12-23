package com.mobprog.artlymobile.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.ProductController;
import com.mobprog.artlymobile.factory.ProductFactory;
import com.mobprog.artlymobile.model.Product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView ivProductImage;

    private TextView tvProductName, tvProductPrice,
            tvProductDescription, tvProductCategory,
            tvProductType, tvProductStock;

    private Button btnBack, btnAddCart;
    private ProductController productController;

    private void init() {
        ivProductImage = findViewById(R.id.iv_product_details_image);
        tvProductName = findViewById(R.id.tv_product_details_name);
        tvProductPrice = findViewById(R.id.tv_product_details_price);
        tvProductDescription = findViewById(R.id.tv_product_details_description);
        tvProductCategory = findViewById(R.id.tv_product_details_category);
        tvProductType = findViewById(R.id.tv_product_details_type);
        tvProductStock = findViewById(R.id.tv_product_details_stock);
        btnBack = findViewById(R.id.btn_product_details_back);
        btnAddCart = findViewById(R.id.btn_product_details_add_cart);

        productController = new ProductController(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();

        String productId = intent.getStringExtra("productId");
        String productImage = intent.getStringExtra("productImage");
        String productName = intent.getStringExtra("productName");
        int productPrice = intent.getIntExtra("productPrice", 0);
        String productDescription = intent.getStringExtra("productDescription");
        String productCategory = intent.getStringExtra("productCategory");
        String productType = intent.getStringExtra("productType");
        int productStock = intent.getIntExtra("productStock", 0);

        if(productImage != null) {
            try {
                URL url = new URL(productImage);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        handler.post(() -> {
                            ivProductImage.setImageBitmap(bitmap);
                        });
                    } catch (IOException e) {
                        ivProductImage.setImageDrawable(getDrawable(R.drawable.ic_image_not_found));
                    }
                });
            } catch (MalformedURLException e) {
                ivProductImage.setImageDrawable(getDrawable(R.drawable.ic_image_not_found));
            }
        }

        tvProductName.setText(productName);
        tvProductPrice.setText("IDR " + productPrice);
        tvProductDescription.setText(productDescription);
        tvProductCategory.setText(productCategory);
        tvProductType.setText(productType);

        if(productType.equals("Digital")) {
            tvProductStock.setText("Unlimited");
        }
        else {
            tvProductStock.setText(String.valueOf(productStock));
        }

        btnBack.setOnClickListener((v) -> {
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        btnAddCart.setOnClickListener((v) -> {
            productController.addToCart(productId, productImage, productName, productPrice, productDescription, productCategory, productType, productStock);
        });
    }
}