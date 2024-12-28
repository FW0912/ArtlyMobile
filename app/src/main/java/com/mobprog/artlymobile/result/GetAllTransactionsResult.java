package com.mobprog.artlymobile.result;

import com.mobprog.artlymobile.utils.EntityValue;

import java.util.Date;

public class GetAllTransactionsResult {
    private final String idTransaction;
    private final Date orderDate;
    private final EntityValue paymentMethod;
    private final EntityValue transactionStatus;
    private final int productCount;
    private final int quantityCount;
    private final int totalPrice;

    public GetAllTransactionsResult(String idTransaction, Date orderDate, EntityValue paymentMethod, EntityValue transactionStatus, int productCount, int quantityCount, int totalPrice) {
        this.idTransaction = idTransaction;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.transactionStatus = transactionStatus;
        this.productCount = productCount;
        this.quantityCount = quantityCount;
        this.totalPrice = totalPrice;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public EntityValue getPaymentMethod() {
        return paymentMethod;
    }

    public EntityValue getTransactionStatus() {
        return transactionStatus;
    }

    public int getProductCount() {
        return productCount;
    }

    public int getQuantityCount() {
        return quantityCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
