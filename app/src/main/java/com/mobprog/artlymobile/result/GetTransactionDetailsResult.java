package com.mobprog.artlymobile.result;

import com.mobprog.artlymobile.utils.EntityValue;

public class GetTransactionDetailsResult {
    private final String idProduct;
    private final String productName;
    private final EntityValue productCategory;
    private final EntityValue productType;
    private final int productPrice;
    private final String productDescription;
    private final String productImage;
    private final int quantity;

    public GetTransactionDetailsResult(String idProduct, String productName, EntityValue productCategory, EntityValue productType, int productPrice, String productDescription, String productImage, int quantity) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productType = productType;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.quantity = quantity;
    }

    public String getIdProduct() {
        return idProduct;
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

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getQuantity() {
        return quantity;
    }
}
