package com.example.appbanhang.utils.eventbus;

import com.example.appbanhang.model.ViewOrder;

public class OrderEventBus {
    ViewOrder viewOrder;

    public OrderEventBus(ViewOrder viewOrder) {
        this.viewOrder = viewOrder;
    }

    public ViewOrder getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(ViewOrder viewOrder) {
        this.viewOrder = viewOrder;
    }
}
