package com.example.appbanhang.list_result;

import com.example.appbanhang.model.Category;

import java.util.List;

public class CategoryModel {
    boolean success;
    String messgage;
    List<Category> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessgage() {
        return messgage;
    }

    public void setMessgage(String messgage) {
        this.messgage = messgage;
    }

    public List<Category> getResult() {
//        this.result.add(new Category(1, "test", "note", "https://didongviet.vn/dchannel/wp-content/uploads/2022/01/cute-didongviet.jpg"));
        return result;
    }

    public void setResult(List<Category> result) {
        this.result = result;
    }
}
