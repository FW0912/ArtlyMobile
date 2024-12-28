package com.mobprog.artlymobile.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.CartItemController;
import com.mobprog.artlymobile.controller.ProductController;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.utils.RangeInputFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView ivProductImage;

    private TextView tvProductName, tvProductPrice,
            tvProductDescription, tvProductCategory,
            tvProductType, tvProductStock, tvAlreadyInCart;
    private EditText etQty;
    private Button btnBack, btnDecrementQty, btnIncrementQty, btnAddCart;
    private LinearLayout llQty;
    private CartItemController cartController;

    private void init() {
        ivProductImage = findViewById(R.id.iv_product_details_image);
        tvProductName = findViewById(R.id.tv_product_details_name);
        tvProductPrice = findViewById(R.id.tv_product_details_price);
        tvProductDescription = findViewById(R.id.tv_product_details_description);
        tvProductCategory = findViewById(R.id.tv_product_details_category);
        tvProductType = findViewById(R.id.tv_product_details_type);
        tvProductStock = findViewById(R.id.tv_product_details_stock);
        tvAlreadyInCart = findViewById(R.id.tv_product_details_already_in_cart);

        etQty = findViewById(R.id.et_product_details_quantity);

        btnBack = findViewById(R.id.btn_product_details_back);
        btnDecrementQty = findViewById(R.id.btn_product_details_decrement_quantity);
        btnIncrementQty = findViewById(R.id.btn_product_details_increment_quantity);
        btnAddCart = findViewById(R.id.btn_product_details_add_cart);

        llQty = findViewById(R.id.ll_product_details_quantity);

        cartController = new CartItemController(this);
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

        ProductController controller = new ProductController(this);
        Product product = controller.getProductById(productId);

        if(product == null) {
            finish();
            return;
        }

        String productImage = product.getProductImage();
        String productName = product.getProductName();
        int productPrice = product.getPrice();
        String productDescription = product.getProductDescription();
        String productCategory = product.getProductCategory();
        String productType = product.getProductType();
        int productStock = product.getStock();

        RangeInputFilter rangeInputFilter = new RangeInputFilter(1, productStock);
        InputFilter[] filters = new InputFilter[] {rangeInputFilter};
        etQty.setFilters(filters);

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
        tvProductPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(productPrice));
        tvProductDescription.setText(productDescription);
        tvProductCategory.setText(productCategory);
        tvProductType.setText(productType);

        btnBack.setOnClickListener((v) -> {
            finish();
        });

//      Check if activity is started from Cart fragment or if it is already in cart
        if(cartController.foundProductInCart(productId)) {
            tvAlreadyInCart.setVisibility(View.VISIBLE);

            if(productType.equals("Digital")) {
                ((ViewGroup) tvProductStock.getParent()).removeAllViews();
            }
            else {
                tvProductStock.setText(String.valueOf(productStock));
            }

            ((ViewGroup) llQty.getParent()).removeView(llQty);
            ((ViewGroup) btnAddCart.getParent()).removeView(btnAddCart);
            return;
        }

//      Check if product type is digital
        if(productType.equals("Digital")) {
            ((ViewGroup) tvProductStock.getParent()).removeAllViews();
            ((ViewGroup) llQty.getParent()).removeView(llQty);
        }
        else {
            tvProductStock.setText(String.valueOf(productStock));

            btnDecrementQty.setOnClickListener((v) -> {
                String qtyString = etQty.getText().toString();

                if(qtyString.isEmpty()) {
                    etQty.setText("1");
                    return;
                }

                int qty = Integer.parseInt(qtyString);

                if(qty > 1) {
                    qty--;
                    etQty.setText(String.valueOf(qty));
                }
            });

            btnIncrementQty.setOnClickListener((v) -> {
                String qtyString = etQty.getText().toString();

                if(qtyString.isEmpty()) {
                    etQty.setText("1");
                    return;
                }

                int qty = Integer.parseInt(qtyString);

                if(qty < productStock) {
                    qty++;
                    etQty.setText(String.valueOf(qty));
                }
            });
        }

//      Check if product is out of stock
        if(productStock == 0) {
            ((ViewGroup) btnAddCart.getParent()).removeView(btnAddCart);
            return;
        }

        btnAddCart.setOnClickListener((v) -> {
            String qtyString = etQty.getText().toString();

            if(qtyString.isEmpty()) {
                return;
            }

            int qty = Integer.parseInt(qtyString);

            if(qty < 1 || qty > productStock) {
                return;
            }

            cartController.addToCart(productId, qty);
            finish();
        });
    }
}