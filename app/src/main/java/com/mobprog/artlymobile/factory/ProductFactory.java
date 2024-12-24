package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.Product;

public class ProductFactory {
    public static Product create(String id, String productName, String productCategory, String productType, int price, int stock, String productImage, String productDescription) {
        return new Product(productDescription, productImage, stock, price, productType, productCategory, productName, id);
    }
}
