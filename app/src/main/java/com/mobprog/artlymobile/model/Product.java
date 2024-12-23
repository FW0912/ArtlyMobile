package com.mobprog.artlymobile.model;

public class Product {
    private String id;
    private String productName;
    private String productCategory;
    private String productType;
    private int price;
    private int stock;
    private String productImage;
    private String productDescription;

    public Product(String productDescription, String productImage, int stock, int price, String productType, String productCategory, String productName, String id) {
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.stock = stock;
        this.price = price;
        this.productType = productType;
        this.productCategory = productCategory;
        this.productName = productName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
