package com.mobprog.artlymobile.controller;

import android.content.Context;
import android.content.Intent;
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
import com.mobprog.artlymobile.request.BuyProductsRequest;
import com.mobprog.artlymobile.request.BuyProductsRequestVm;
import com.mobprog.artlymobile.service.ProductService;
import com.mobprog.artlymobile.utils.ApiResponse;
import com.mobprog.artlymobile.utils.ErrorToast;
import com.mobprog.artlymobile.utils.RetrofitClient;
import com.mobprog.artlymobile.utils.SuccessToast;
import com.mobprog.artlymobile.view.BottomNavigationActivity;
import com.mobprog.artlymobile.view.LoginActivity;
import com.mobprog.artlymobile.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemController {
    private final String API_KEY = "653553a75cb34229975e4fc428e26d32";
    private ProductService service;
    private ProductController productController;
    private UserController userController;
    private Context context;

    public CartItemController(Context context) {
        service = RetrofitClient.getProductService();
        this.context = context;
        productController = new ProductController(context);
        userController = new UserController(context);
    }

    public void addToCart(String productId, int qty) {
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

    private List<CartItem> getCartItems() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cart = sharedPreferences.getString("CartItems", null);
        List<CartItem> cartItems = new ArrayList<>();
        Gson gson = new Gson();

        if(cart != null) {
            JsonArray cartItemsJson = gson.fromJson(cart, JsonArray.class);
            List<JsonElement> cartItemsElements = cartItemsJson.asList();

            for (JsonElement cartItemElement : cartItemsElements) {
                JsonObject cartItemJson = cartItemElement.getAsJsonObject();

                String productId = cartItemJson.get("productId").getAsString();
                int qty = cartItemJson.get("qty").getAsInt();

                Product product = productController.getProductById(productId);

                CartItem cartItem = CartItemFactory.create(product, qty);
                cartItems.add(cartItem);
            }
        }

        return cartItems;
    }

    public void bindCart(RecyclerView rv, CartViewModel cartViewModel) {
        CartItemAdapter cartItemAdapter = new CartItemAdapter(context, cartViewModel, getCartItems());
        rv.setAdapter(cartItemAdapter);
    }

    public void buyProducts(RecyclerView rv, CartViewModel cartViewModel) {
        List<CartItem> cartItems = getCartItems();

        if(cartItems == null) {
            ErrorToast.makeToast(context, "Cart is empty!");
            return;
        }

        SharedPreferences userSharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        String userId = userSharedPreferences.getString("userId", null);

        if(userId == null) {
            ErrorToast.makeToast(context, "You are not logged in!");

            userController.logout();

            return;
        }

        List<BuyProductsRequestVm> products = new ArrayList<>();

        for(CartItem cartItem : cartItems) {
            BuyProductsRequestVm product = new BuyProductsRequestVm(cartItem.getProduct().getId(), cartItem.getQty());
            products.add(product);
        }

        BuyProductsRequest request = new BuyProductsRequest(userId, products);

        Call<ApiResponse<Integer>> call = service.buyProducts(API_KEY, request);
        call.enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                ApiResponse<Integer> r = response.body();

                if(response.isSuccessful()) {
                    if(r.getStatusCode() == 200) {
                        userController.refreshLatestUserData(userId);

                        SharedPreferences cartSharedPreferences = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = cartSharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        bindCart(rv, cartViewModel);

                        int currentBalance = r.getData();
                        cartViewModel.updateBalanceAfterPurchase(currentBalance);

                        SuccessToast.makeToast(context, "Products successfully purchased.");
                    }
                    else
                    {
                        ErrorToast.makeToast(context, r.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable throwable) {
                ErrorToast.makeToast(context, "Something went wrong!");
            }
        });
    }
}
