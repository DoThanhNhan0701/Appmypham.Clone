package com.example.appbanhang.list_result;

import com.example.appbanhang.model.Address;

import java.util.List;

public class AddressModel {
    private boolean success;
    private String message;
    private List<Address> result;

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

    public List<Address> getResult() {
        return result;
    }

    public void setResult(List<Address> result) {
        this.result = result;
    }

}
