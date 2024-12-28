package com.mobprog.artlymobile.factory;

import com.mobprog.artlymobile.model.Transaction;

import java.util.Date;

public class TransactionFactory {
    public static Transaction create(String id, Date orderDate, String paymentMethod, String transactionStatus, int productCount, int quantityCount, int totalPrice) {
        return new Transaction(id, orderDate, paymentMethod, transactionStatus, productCount, quantityCount, totalPrice);
    }
}
