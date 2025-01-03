package com.mobprog.artlymobile.request;

public class BuyProductsRequestVm {
    private final String IdProduct;
    private final int Quantity;

    public BuyProductsRequestVm(String idProduct, int quantity) {
        IdProduct = idProduct;
        Quantity = quantity;
    }
}
