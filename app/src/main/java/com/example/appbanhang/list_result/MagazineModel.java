package com.example.appbanhang.list_result;

import com.example.appbanhang.model.Magazine;

import java.util.List;

public class MagazineModel {
    private boolean success;
    private String message;
    private List<Magazine> result;

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

    public List<Magazine> getResult() {
        return result;
    }

    public void setResult(List<Magazine> result) {
        this.result = result;
    }
}
