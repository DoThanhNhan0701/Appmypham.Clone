package com.example.appbanhang.list_result;

import com.example.appbanhang.model.ViewOrder;

import java.util.List;

public class ViewOrderModel {
    private boolean success;
    private String message;
    private List<ViewOrder> result;


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

    public List<ViewOrder> getResult() {
        return result;
    }

    public void setResult(List<ViewOrder> result) {
        this.result = result;
    }
}
