package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mobprog.artlymobile.adapter.CartItemAdapter;
import com.mobprog.artlymobile.factory.CartItemFactory;
import com.mobprog.artlymobile.factory.ProductFactory;
import com.mobprog.artlymobile.model.CartItem;
import com.mobprog.artlymobile.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartItemController {
    private Context context;

    public CartItemController(Context context) {
        this.context = context;
    }

    public void addToCart(String productId, String productImage, String productName, int productPrice, String productDescription, String productCategory, String productType, int productStock, int qty) {
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
        productJson.addProperty("qty", qty);

        cartItemsJson.add(productJson);

        String productsJson = cartItemsJson.toString();
        editor.putString("CartItems", productsJson);

        editor.commit();
    }

    public void bindCart(RecyclerView rv) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();

        if(cart == null) {
            return;
        }
        else {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);
            List<JsonElement> cartItemsElements = cartItemsJson.asList();
            List<CartItem> cartItems = new ArrayList<>();

            for(JsonElement cartItemElement : cartItemsElements) {
                JsonObject cartItemJson = cartItemElement.getAsJsonObject();

                String productId = cartItemJson.get("productId").getAsString();
                String productName = cartItemJson.get("productName").getAsString();
                int price = cartItemJson.get("price").getAsInt();
                int stock = cartItemJson.get("stock").getAsInt();
                String productImage = cartItemJson.get("productImage").getAsString();
                String productDescription = cartItemJson.get("description").getAsString();
                String productCategory = cartItemJson.get("productCategory").getAsString();
                String productType = cartItemJson.get("productType").getAsString();

                Product product = ProductFactory.create(productId, productName, productCategory, productType, price, stock, productImage, productDescription);

                int qty = cartItemJson.get("qty").getAsInt();

                CartItem cartItem = CartItemFactory.create(product, qty);
                cartItems.add(cartItem);
            }

            CartItemAdapter cartItemAdapter = new CartItemAdapter(context, cartItems);
            rv.setAdapter(cartItemAdapter);
        }
    }
}
