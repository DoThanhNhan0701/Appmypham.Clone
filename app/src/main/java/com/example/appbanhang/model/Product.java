package com.example.appbanhang.model;

import java.util.Date;

public class Product {
    private int id;
    private String category;
    private String name;
    private String images;
    private int price;
    private String description;
    private Date create_date;
    private boolean active;
    public Product() {
    }

    public Product(int id, String category, String name, String images, int price, String description, Date create_date, boolean active) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.images = images;
        this.price = price;
        this.description = description;
        this.create_date = create_date;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
