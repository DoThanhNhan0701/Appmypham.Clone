package com.example.appbanhang.model;

import java.util.List;

public class ViewOrder {
    private int id;
    private int iduser;
    private String phone;
    private String tinh;
    private String quan;
    private String phuong;
    private String diachi;
    List<ProductOrder> productorder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getPhuong() {
        return phuong;
    }

    public void setPhuong(String phuong) {
        this.phuong = phuong;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public List<ProductOrder> getProductorder() {
        return productorder;
    }

    public void setProductorder(List<ProductOrder> productorder) {
        this.productorder = productorder;
    }



}
