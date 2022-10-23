package com.example.appbanhang.model;


import java.util.Date;

public class Category {
    private int id;
    private String name;
    private String decription;
    private String image;
    private Date create_date;

    public Category() {
    }

    public Category(int id, String name, String decription, String image, Date create_date) {
        this.id = id;
        this.name = name;
        this.decription = decription;
        this.image = image;
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
