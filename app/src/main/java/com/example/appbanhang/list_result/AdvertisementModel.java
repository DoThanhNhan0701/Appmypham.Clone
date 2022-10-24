package com.example.appbanhang.list_result;

import com.example.appbanhang.model.Advertisement;

import java.util.List;

public class AdvertisementModel {
    private boolean success;
    private String message;
    private List<Advertisement> result;

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

    public List<Advertisement> getResult() {
        return result;
    }

    public void setResult(List<Advertisement> result) {
        this.result = result;
    }
}
