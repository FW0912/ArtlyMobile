package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.CartItem;
import com.mobprog.artlymobile.model.Product;

public class CartItemFactory {
    public static CartItem create(Product product, int qty) {
        return new CartItem(product, qty);
    }
}
