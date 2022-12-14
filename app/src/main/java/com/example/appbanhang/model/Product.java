package com.example.appbanhang.model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private int id;
    private int category;
    private String name;
    private String images;
    private int discount;
    private int price_old;
    private String description;
    private int amount;
    private Date create_date;

    public Product() {
    }

    public Product(int id, int category, String name, String images, int discount, int price_old, String description, int amount, Date create_date) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.images = images;
        this.discount = discount;
        this.price_old = price_old;
        this.description = description;
        this.amount = amount;
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getPrice_old() {
        return price_old;
    }

    public void setPrice_old(int price_old) {
        this.price_old = price_old;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
