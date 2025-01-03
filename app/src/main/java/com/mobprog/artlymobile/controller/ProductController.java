package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mobprog.artlymobile.adapter.ProductAdapter;
import com.mobprog.artlymobile.factory.ProductFactory;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.result.GetProductByIdResult;
import com.mobprog.artlymobile.service.ProductService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductController {
    private final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private ProductService service;
    private Context context;
    private Product product;

    public ProductController(Context context) {
        service = RetrofitClient.getProductService();
        this.context = context;
    }

    public void bindAllProducts(RecyclerView rv) {
        Call<JsonObject> call = service.getAllProducts(API_KEY);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject json = response.body();

                    JsonArray data = json.getAsJsonArray("data");

                    List<JsonElement> productsElements = data.asList();
                    List<Product> products = new ArrayList<>();

                    for(JsonElement productElement : productsElements) {
                        JsonObject productJson = productElement.getAsJsonObject();

                        String productId = productJson.get("id").getAsString();
                        String productName = productJson.get("productName").getAsString();
                        int price = productJson.get("price").getAsInt();
                        int stock = productJson.get("stock").getAsInt();
                        String productImage = productJson.get("productImage").getAsString();
                        String productDescription = productJson.get("description").getAsString();

                        JsonObject categoryJson = productJson.get("productCategory").getAsJsonObject();
                        String productCategory = categoryJson.get("description").getAsString();

                        JsonObject typeJson = productJson.get("productType").getAsJsonObject();
                        String productType = typeJson.get("description").getAsString();

                        Product product = ProductFactory.create(productId, productName, productCategory, productType, price, stock, productImage, productDescription);
                        products.add(product);
                    }

                    ProductAdapter adapter = new ProductAdapter(context, products);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {

            }
        });
    }

    public void bindInterestedProducts(RecyclerView rv) {
        Call<JsonObject> call = service.getAllProducts(API_KEY);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject json = response.body();

                    JsonArray data = json.getAsJsonArray("data");

                    List<JsonElement> productsElements = data.asList();
                    List<Product> products = new ArrayList<>();
                    List<Integer> idxList = new ArrayList<>();

                    for(int i = 0; i < 3; i++ ) {
                        Random random = new Random();
                        int idx = random.nextInt(productsElements.size());

                        while(idxList.contains(idx)) {
                            idx = random.nextInt(productsElements.size());
                        }

                        idxList.add(idx);

                        JsonObject productJson = productsElements.get(idx).getAsJsonObject();

                        String productId = productJson.get("id").getAsString();
                        String productName = productJson.get("productName").getAsString();
                        int price = productJson.get("price").getAsInt();
                        int stock = productJson.get("stock").getAsInt();
                        String productImage = productJson.get("productImage").getAsString();
                        String productDescription = productJson.get("description").getAsString();

                        JsonObject categoryJson = productJson.get("productCategory").getAsJsonObject();
                        String productCategory = categoryJson.get("description").getAsString();

                        JsonObject typeJson = productJson.get("productType").getAsJsonObject();
                        String productType = typeJson.get("description").getAsString();

                        Product product = ProductFactory.create(productId, productName, productCategory, productType, price, stock, productImage, productDescription);
                        products.add(product);
                    }

                    ProductAdapter adapter = new ProductAdapter(context, products);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {

            }
        });
    }

    public Product getProductById(String productId) {
        Call<ApiResponse<GetProductByIdResult>> call = service.getProductById(API_KEY, productId);

        Thread thread = new Thread(() -> {
            try {
                ApiResponse<GetProductByIdResult> response = call.execute().body();
                GetProductByIdResult productByIdResult = response.getData();
                Product result = ProductFactory.create(productId, productByIdResult.getProductName(),
                        productByIdResult.getProductCategory().getDescription(),
                        productByIdResult.getProductType().getDescription(), productByIdResult.getPrice(),
                        productByIdResult.getStock(), productByIdResult.getProductImage(), productByIdResult.getDescription());

                product = result;
            } catch (Exception e) {
                product = null;
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            product = null;
        }

        return product;
    }
}
