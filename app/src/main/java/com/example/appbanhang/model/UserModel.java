package com.example.appbanhang.model;

import java.util.List;

public class UserModel {
    private boolean success;
    private String message;
    private List<User> getResult;

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

    public List<User> getGetResult() {
        return getResult;
    }

    public void setGetResult(List<User> getResult) {
        this.getResult = getResult;
    }
}
