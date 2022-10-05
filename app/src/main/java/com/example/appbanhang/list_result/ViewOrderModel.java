package com.example.appbanhang.list_result;

import com.example.appbanhang.model.ItemOrder;
import com.example.appbanhang.model.User;

import java.util.List;

public class ViewOrderModel {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemOrder> getResult() {
        return result;
    }

    public void setResult(List<ItemOrder> result) {
        this.result = result;
    }

    private boolean success;
    private String message;
    private List<ItemOrder> result;
}
