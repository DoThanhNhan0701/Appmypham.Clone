package com.example.appbanhang.utils.eventbus;

import com.example.appbanhang.model.Product;

public class CrudProductEvent {
    Product product;

    public CrudProductEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
