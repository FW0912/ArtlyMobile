package com.mobprog.artlymobile.model;

import java.util.Date;

public class Transaction {
    private String id;
    private Date orderDate;
    private String paymentMethod;
    private String transactionStatus;
    private int productCount;
    private int quantityCount;
    private int totalPrice;

    public Transaction(String id, Date orderDate, String paymentMethod, String transactionStatus, int productCount, int quantityCount, int totalPrice) {
        this.id = id;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.transactionStatus = transactionStatus;
        this.productCount = productCount;
        this.quantityCount = quantityCount;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getQuantityCount() {
        return quantityCount;
    }

    public void setQuantityCount(int quantityCount) {
        this.quantityCount = quantityCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
