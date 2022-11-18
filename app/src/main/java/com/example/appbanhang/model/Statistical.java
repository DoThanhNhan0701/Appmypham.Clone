package com.example.appbanhang.model;

public class Statistical {
    private int idproduct;
    private String name;
    private String total;

    public Statistical() {
    }

    public Statistical(int idproduct, String name, String total) {
        this.idproduct = idproduct;
        this.name = name;
        this.total = total;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
