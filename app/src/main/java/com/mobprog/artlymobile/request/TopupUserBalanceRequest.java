package com.mobprog.artlymobile.request;

public class TopupUserBalanceRequest {
    private final String idUser;
    private final int amount;
    public TopupUserBalanceRequest(String idUser, int amount) {
        this.idUser = idUser;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getIdUser() {
        return idUser;
    }
}
