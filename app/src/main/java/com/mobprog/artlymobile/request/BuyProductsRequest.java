package com.mobprog.artlymobile.request;

import java.util.List;

public class BuyProductsRequest {
    private final String IdUser;
    private final List<BuyProductsRequestVm> Products;

    public BuyProductsRequest(String idUser, List<BuyProductsRequestVm> products) {
        IdUser = idUser;
        Products = products;
    }
}

