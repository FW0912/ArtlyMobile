package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mobprog.artlymobile.adapter.ProductAdapter;
import com.mobprog.artlymobile.factory.ProductFactory;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.service.ProductService;
import com.mobprog.artlymobile.utils.RetrofitClient;
import com.mobprog.artlymobile.view.LoginActivity;

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

                    List<JsonElement> productsElement = data.asList();
                    List<Product> products = new ArrayList<>();

                    for(JsonElement productElement : productsElement) {
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

                    List<JsonElement> productsElement = data.asList();
                    List<Product> products = new ArrayList<>();
                    List<Integer> idxList = new ArrayList<>();

                    for(int i = 0; i < 4; i++ ) {
                        Random random = new Random();
                        int idx = random.nextInt(productsElement.size());

                        while(idxList.contains(idx)) {
                            idx = random.nextInt(productsElement.size());
                        }

                        idxList.add(idx);

                        JsonObject productJson = productsElement.get(idx).getAsJsonObject();

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

    public void addToCart(String productId, String productImage, String productName, int productPrice, String productDescription, String productCategory, String productType, int productStock) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();
        JsonArray cartItemsJson;

        if(cart == null) {
            cartItemsJson = new JsonArray();
        }
        else {
            cartItemsJson = gson.fromJson(cart, JsonArray.class);
        }

        JsonObject productJson = new JsonObject();

        productJson.addProperty("productId", productId);
        productJson.addProperty("productImage", productImage);
        productJson.addProperty("productName", productName);
        productJson.addProperty("productPrice", productPrice);
        productJson.addProperty("productDescription", productDescription);
        productJson.addProperty("productCategory", productCategory);
        productJson.addProperty("productType", productType);
        productJson.addProperty("productStock", productStock);

        cartItemsJson.add(productJson);

        String productsJson = cartItemsJson.toString();
        editor.putString("CartItems", productsJson);

        editor.commit();
    }
}
