package com.mobprog.artlymobile.result;

import com.mobprog.artlymobile.utils.EntityValue;

public class GetProductByIdResult {
    private final String productName;
    private final EntityValue productCategory;
    private final EntityValue productType;
    private final int price;
    private final int stock;
    private final String productImage;
    private final String id;
    private final String description;

    public GetProductByIdResult(String productName, EntityValue productCategory, EntityValue productType, int price, int stock, String productImage, String id, String description) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productType = productType;
        this.price = price;
        this.stock = stock;
        this.productImage = productImage;
        this.id = id;
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public EntityValue getProductCategory() {
        return productCategory;
    }

    public EntityValue getProductType() {
        return productType;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
