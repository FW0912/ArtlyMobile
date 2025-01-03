package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.TransactionDetails;

public class TransactionDetailsFactory {
    public static TransactionDetails create(String productId, String productName, String productCategory, String productType, int productPrice, String productDescription, String productImage, int qty) {
        return new TransactionDetails(productId, productName, productCategory, productType, productPrice, productDescription, productImage, qty);
    }
}
