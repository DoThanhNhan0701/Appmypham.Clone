package com.example.appbanhang.model;

public class Address {
    private int id;
    private int iduser;
    private String thanhpho;
    private String quan;
    private String phuong;
    private String address;

    public Address() {
    }

    public Address(int id, int iduser, String thanhpho, String quan, String phuong, String address) {
        this.id = id;
        this.iduser = iduser;
        this.thanhpho = thanhpho;
        this.quan = quan;
        this.phuong = phuong;
        this.address = address;
    }

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

    public String getThanhpho() {
        return thanhpho;
    }

    public void setTinh(String thanhpho) {
        this.thanhpho = thanhpho;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
