package com.example.appbanhang.model;

public class User {
    private int id;
    private String gmail;
    private String first_name;
    private String last_name;
    private String phone;
    private String password;


    public User() {

    }

    public User(int id, String gmail, String first_name, String last_name, String phone, String password) {
        this.id = id;
        this.gmail = gmail;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
