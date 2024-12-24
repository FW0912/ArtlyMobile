package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
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
import com.mobprog.artlymobile.viewmodel.CartViewModel;

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

        JsonObject cartItemJson = new JsonObject();

        cartItemJson.addProperty("productId", productId);
        cartItemJson.addProperty("productImage", productImage);
        cartItemJson.addProperty("productName", productName);
        cartItemJson.addProperty("productPrice", productPrice);
        cartItemJson.addProperty("productDescription", productDescription);
        cartItemJson.addProperty("productCategory", productCategory);
        cartItemJson.addProperty("productType", productType);
        cartItemJson.addProperty("productStock", productStock);
        cartItemJson.addProperty("qty", qty);

        cartItemsJson.add(cartItemJson);

        editor.putString("CartItems", cartItemsJson.toString());

        editor.commit();
    }

    public void removeFromCart(String productId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();

        if(cart != null) {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);
            boolean removed = false;

            for(int i = 0; i < cartItemsJson.size(); i++) {
                JsonObject cartItemJson = cartItemsJson.get(i).getAsJsonObject();

                if(cartItemJson.get("productId").getAsString().equals(productId)) {
                    cartItemsJson.remove(i);

                    removed = true;
                }
            }

            if(!removed) {
                return;
            }

            if(cartItemsJson.isEmpty()) {
                editor.remove("CartItems");
            }
            else {
                editor.putString("CartItems", cartItemsJson.toString());
            }

            editor.commit();
        }
    }

    public boolean foundProductInCart(String productId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();

        if(cart != null) {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);

            for(int i = 0; i < cartItemsJson.size(); i++) {
                JsonObject cartItemJson = cartItemsJson.get(i).getAsJsonObject();

                if(cartItemJson.get("productId").getAsString().equals(productId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void changeCartItemQty(String productId, int newQty) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();

        if(cart != null) {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);
            boolean changed = false;

            for(int i = 0; i < cartItemsJson.size(); i++) {
                JsonObject cartItemJson = cartItemsJson.get(i).getAsJsonObject();

                if(cartItemJson.get("productId").getAsString().equals(productId)) {
                    cartItemJson.addProperty("qty", newQty);
                    cartItemsJson.remove(i);
                    cartItemsJson.add(cartItemJson);

                    changed = true;
                }
            }

            if(!changed) {
                return;
            }

            editor.putString("CartItems", cartItemsJson.toString());

            editor.commit();
        }
    }

    public void bindCart(RecyclerView rv, CartViewModel cartViewModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cart = sharedPreferences.getString("CartItems", null);
        Gson gson = new Gson();

        if(cart != null) {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);
            List<JsonElement> cartItemsElements = cartItemsJson.asList();
            List<CartItem> cartItems = new ArrayList<>();

            for(JsonElement cartItemElement : cartItemsElements) {
                JsonObject cartItemJson = cartItemElement.getAsJsonObject();

                String productId = cartItemJson.get("productId").getAsString();
                String productName = cartItemJson.get("productName").getAsString();
                int price = cartItemJson.get("productPrice").getAsInt();
                int stock = cartItemJson.get("productStock").getAsInt();
                String productImage = cartItemJson.get("productImage").getAsString();
                String productDescription = cartItemJson.get("productDescription").getAsString();
                String productCategory = cartItemJson.get("productCategory").getAsString();
                String productType = cartItemJson.get("productType").getAsString();

                Product product = ProductFactory.create(productId, productName, productCategory, productType, price, stock, productImage, productDescription);

                int qty = cartItemJson.get("qty").getAsInt();

                CartItem cartItem = CartItemFactory.create(product, qty);
                cartItems.add(cartItem);
            }

            CartItemAdapter cartItemAdapter = new CartItemAdapter(context, cartViewModel, cartItems);
            rv.setAdapter(cartItemAdapter);
        }
    }
}
